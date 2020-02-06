package dev.chiaradia.payrollservice.service.client

import feign.Headers
import feign.Param
import feign.RequestLine
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

interface PayslipProApiClient {

    @RequestLine("GET /payslips.{year}{month}.txt")
    @Headers(
        "${HttpHeaders.CONTENT_TYPE}: ${MediaType.TEXT_PLAIN_VALUE}"
    )
    fun getPayslipsByMonthAndYear(@Param("month") month: Long, @Param("year") year: Long): String
}