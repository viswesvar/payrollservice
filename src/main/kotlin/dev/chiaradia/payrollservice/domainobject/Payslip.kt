package dev.chiaradia.payrollservice.domainobject

import java.math.BigDecimal

data class Payslip(
    val id: Long,
    val idNumber: String,
    val date: String,
    val payslipLine: String,
    val grossValue: BigDecimal,
    val nationalInsuranceRate: BigDecimal,
    val nationalInsuranceValue: BigDecimal,
    val taxRate: BigDecimal,
    val taxAmount: BigDecimal,
    val netValue: BigDecimal
)