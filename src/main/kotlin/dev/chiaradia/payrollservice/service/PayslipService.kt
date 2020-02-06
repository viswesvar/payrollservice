package dev.chiaradia.payrollservice.service

import dev.chiaradia.payrollservice.domainobject.Payslip

interface PayslipService {

    fun getPayslips(month: Long, year: Long): List<Payslip>

    fun updateTaxRate(month: Long, year: Long, taxRate: Double): List<Payslip>

}