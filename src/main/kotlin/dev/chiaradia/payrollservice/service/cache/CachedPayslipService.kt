package dev.chiaradia.payrollservice.service.cache

import dev.chiaradia.payrollservice.controller.mapper.toPayslip
import dev.chiaradia.payrollservice.domainobject.Payslip
import dev.chiaradia.payrollservice.exception.PayslipParsingException
import dev.chiaradia.payrollservice.exception.PayslipsNotFoundException
import dev.chiaradia.payrollservice.service.PayslipService
import dev.chiaradia.payrollservice.service.client.PayslipProApiClient
import dev.chiaradia.payrollservice.service.extension.createPayslipLine
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.math.RoundingMode

@Service
class CachedPayslipService(
    private val payslipProApiClient: PayslipProApiClient
) : PayslipService {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Cacheable(cacheNames = [CACHE_PAYSLIPS], sync = true, key = "{ #month, #year }")
    override fun getPayslips(month: Long, year: Long): List<Payslip> = getPayslipList(month, year)


    @Cacheable(cacheNames = [CACHE_SIMULATED_PAYSLIPS], sync = true, key = "{ #month, #year, #taxRate }")
    override fun updateTaxRate(month: Long, year: Long, taxRate: Double): List<Payslip> = getPayslipList(month, year).map { payslip ->
        val newPayslip = payslip.copy(
            taxRate = taxRate.toBigDecimal().setScale(2, RoundingMode.HALF_UP),
            taxAmount = updateTaxAmountValue(payslip.grossValue.toDouble(), taxRate).toBigDecimal().setScale(2, RoundingMode.HALF_UP),
            netValue = updateNetValue(payslip)
        )
        newPayslip.copy(
            payslipLine = newPayslip.createPayslipLine()
        )
    }


    @Scheduled(fixedRateString = "\${spring.cache.cache_companies.ttl:900000}")
    @CacheEvict(cacheNames = [CACHE_PAYSLIPS, CACHE_SIMULATED_PAYSLIPS], allEntries = true)
    fun evictCaches() {
        logger.debug("Evicting $CACHE_PAYSLIPS and $CACHE_SIMULATED_PAYSLIPS caches")
    }

    private fun getPayslipList(month: Long, year: Long): List<Payslip> =
        getPayslipsFromPayslipPro(month, year).map {
            toPayslip(it)
        }

    private fun getPayslipsFromPayslipPro(month: Long, year: Long): List<String> {
        logger.info("Calling PayslipPro API for getting payslips from $month/$year")
        val payslipsString = payslipProApiClient
            .getPayslipsByMonthAndYear(month, year)
            .replace("\\s".toRegex(), "")
        return when {
            payslipsString.isEmpty() -> {
                logger.warn("There are no records to be updated for the period $month/$year")
                throw PayslipsNotFoundException("Could not find payslips for the period $month/$year")
            }
            payslipsString.length % 69 == 0 -> {
                payslipsString.chunked(69)
            }
            else -> {
                throw PayslipParsingException("Invalid length of payslips string: ${payslipsString.length}")
            }
        }
    }

    private fun updateNetValue(payslip: Payslip) = payslip.grossValue - payslip.taxAmount

    private fun updateTaxAmountValue(value: Double, rate: Double) = value * (rate / 100.0)


    companion object {
        const val CACHE_PAYSLIPS = "cache_payslips"
        const val CACHE_SIMULATED_PAYSLIPS = "cache_simulated_payslips"
    }

}