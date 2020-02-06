package dev.chiaradia.payrollservice.controller.mapper

import dev.chiaradia.payrollservice.datatransferobject.PayslipDto
import dev.chiaradia.payrollservice.domainobject.Payslip
import java.math.RoundingMode

fun toPayslip(payslipLine: String): Payslip =
    Payslip(
        id = payslipLine.substring(0, 12).toLong(), //12
        idNumber = payslipLine.substring(12, 21), //9
        date = payslipLine.substring(21, 29), //8
        grossValue = payslipLine.substring(29, 37).toDouble().round().toBigDecimal().setScale(2, RoundingMode.HALF_UP), //8
        nationalInsuranceRate = payslipLine.substring(37, 41).toDouble().round().toBigDecimal().setScale(2, RoundingMode.HALF_UP), //4,
        nationalInsuranceValue = payslipLine.substring(41, 49).toDouble().round().toBigDecimal().setScale(2, RoundingMode.HALF_UP), //8
        taxRate = payslipLine.substring(49, 53).toDouble().round().toBigDecimal().setScale(2, RoundingMode.HALF_UP), //4
        taxAmount = payslipLine.substring(53, 61).toDouble().round().toBigDecimal().setScale(2, RoundingMode.HALF_UP), //8
        netValue = payslipLine.substring(61, payslipLine.length).toDouble().round().toBigDecimal().setScale(2, RoundingMode.HALF_UP), //8
        payslipLine = payslipLine
    )

fun Payslip.toPayslipDto(): PayslipDto =
    PayslipDto(
        id = id,
        idNumber = idNumber,
        date = date,
        grossValue = grossValue,
        nationalInsuranceRate = nationalInsuranceRate,
        nationalInsuranceValue = nationalInsuranceValue,
        taxRate = taxRate,
        taxAmount = taxAmount,
        netValue = netValue,
        payslipLine = payslipLine
    )

fun List<Payslip>.toPayslipDtoList(): List<PayslipDto> =
    this.map {
        it.toPayslipDto()
    }

fun Double.round() = this / 100