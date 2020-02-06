package dev.chiaradia.payrollservice.controller

import dev.chiaradia.payrollservice.datatransferobject.PayslipDto
import dev.chiaradia.payrollservice.service.PayslipService
import dev.chiaradia.payrollservice.controller.mapper.toPayslipDtoList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payslips")
class PayslipController(
    private val payslipService: PayslipService
) {

    @GetMapping
    fun getAllPayslips(
        @RequestParam year: Long,
        @RequestParam month: Long
    ): List<PayslipDto> = payslipService.getPayslips(month, year).toPayslipDtoList()

    @PutMapping
    fun updateTaxRate(
        @RequestParam taxRate: Double,
        @RequestParam year: Long,
        @RequestParam month: Long
    ): List<PayslipDto> = payslipService.updateTaxRate(month, year, taxRate).toPayslipDtoList()

}