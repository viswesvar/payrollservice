package dev.chiaradia.payrollservice.service.extension

import dev.chiaradia.payrollservice.domainobject.Payslip
import java.math.BigDecimal

fun Payslip.createPayslipLine(): String =
    StringBuilder().apply {
        append(id.createFieldFromLong(12))
        append(idNumber)
        append(date)
        append(grossValue.createFieldFromBigDecimal(8))
        append(nationalInsuranceRate.createFieldFromBigDecimal(4))
        append(nationalInsuranceValue.createFieldFromBigDecimal(8))
        append(taxRate.createFieldFromBigDecimal(4))
        append(taxAmount.createFieldFromBigDecimal(8))
        append(netValue.createFieldFromBigDecimal(8))
    }.run {
        this.toString()
    }

private fun BigDecimal.createFieldFromBigDecimal(fieldSize: Int) =
    "0".repeat(fieldSize - this.toString().replace(".", "").length)
        .plus(this.toString().replace(".", ""))

private fun Long.createFieldFromLong(fieldSize: Int) =
    "0".repeat(fieldSize - this.toString().replace(".", "").length)
        .plus(this.toString().replace(".", ""))
