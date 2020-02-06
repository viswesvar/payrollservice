package dev.chiaradia.payrollservice.exception

import dev.chiaradia.payrollservice.exception.message.ErrorMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.OffsetDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(value = [
        PayslipProException::class,
        PayrollServiceException::class
    ])
    fun handlePayslipProException(ex: PayslipProException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(logAndCreateErrorMessage(ex), ex.httpStatus)
    }

    @ExceptionHandler(value = [
        PayslipsNotFoundException::class
    ])
    fun handleNotFoundExceptions(ex: Exception): ResponseEntity<ErrorMessage> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndCreateErrorMessage(ex))
    }

    @ExceptionHandler(value = [
        NullPointerException::class,
        IllegalArgumentException::class
    ])
    fun handleBadRequestExceptions(ex: Exception): ResponseEntity<ErrorMessage> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(logAndCreateErrorMessage(ex))
    }

    @ExceptionHandler(value = [
        PayslipParsingException::class,
        Exception::class
    ])
    fun handleException(ex: Exception): ResponseEntity<ErrorMessage> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(logAndCreateErrorMessage(ex))
    }

    private fun logAndCreateErrorMessage(throwable: Throwable?): ErrorMessage {
        logger.error("Error catch by the GlobalExceptionHandler", throwable)
        return ErrorMessage(
            time = OffsetDateTime.now(),
            message = throwable?.message ?: ""
        )
    }

}