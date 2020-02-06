package dev.chiaradia.payrollservice.exception.message

import java.time.OffsetDateTime

data class ErrorMessage(
    val time: OffsetDateTime,
    val message: String
)