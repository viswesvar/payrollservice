package dev.chiaradia.payrollservice.controller

import dev.chiaradia.payrollservice.domainobject.Payslip
import dev.chiaradia.payrollservice.exception.PayslipParsingException
import dev.chiaradia.payrollservice.exception.PayslipsNotFoundException
import dev.chiaradia.payrollservice.service.PayslipService
import dev.chiaradia.payrollservice.service.client.PayslipProApiClient
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@RunWith(SpringRunner::class)
@WebMvcTest(PayslipController::class)
class PayslipControllerTest {

    @MockBean
    private lateinit var payslipProApiClient: PayslipProApiClient

    @MockBean
    private lateinit var payslipService: PayslipService

    @Autowired
    private lateinit var mvc: MockMvc

    @After
    fun after() {
        verifyNoMoreInteractions(payslipService)
    }

    @Test
    fun `Should return the list of payslips from PayslipPRO`() {

        val month = 12L

        val year = 2018L

        Mockito.`when`(payslipService.getPayslips(month, year)).thenReturn(createPayslips())

        mvc.perform(
            MockMvcRequestBuilders.get("/api/payslips")
                .param("year", year.toString())
                .param("month", month.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.`is`("20181231")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].idNumber", Matchers.`is`("97084172E")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.`is`(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].grossValue", Matchers.`is`(2486.00)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].nationalInsuranceRate", Matchers.`is`(5.00)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].nationalInsuranceValue", Matchers.`is`(124.30)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].taxRate", Matchers.`is`(12.00)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].taxAmount", Matchers.`is`(298.32)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].netValue", Matchers.`is`(2063.37)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].payslipLine", Matchers.`is`("00000000000197084172E201812310024860005000001243012000002983200206337")))

        verify(payslipService).getPayslips(month = month, year = year)
    }

    @Test
    fun `Should return HTTP Status 500 when parsing exception`() {

        val month = 12L

        val year = 2018L

        Mockito.`when`(payslipService.getPayslips(month, year)).thenThrow(PayslipParsingException("Error parsing"))

        mvc.perform(
            MockMvcRequestBuilders.get("/api/payslips")
                .param("year", year.toString())
                .param("month", month.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)

        verify(payslipService).getPayslips(month = month, year = year)
    }

    @Test
    fun `Should return HTTP Status 404 when payslip not found exception`() {

        val month = 12L

        val year = 2018L

        Mockito.`when`(payslipService.getPayslips(month, year)).thenThrow(PayslipsNotFoundException("Not found"))

        mvc.perform(
            MockMvcRequestBuilders.get("/api/payslips")
                .param("year", year.toString())
                .param("month", month.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

        verify(payslipService).getPayslips(month = month, year = year)
    }

    @Test
    fun `Should return the list of payslips updated after update tax rate`() {

        val month = 12L

        val year = 2018L

        val taxRate = 10.00

        Mockito.`when`(payslipService.updateTaxRate(month, year, 10.00)).thenReturn(createUpdatedPayslips())

        mvc.perform(
            MockMvcRequestBuilders.put("/api/payslips")
                .param("year", year.toString())
                .param("month", month.toString())
                .param("taxRate", taxRate.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.`is`("20181231")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].idNumber", Matchers.`is`("97084172E")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.`is`(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].grossValue", Matchers.`is`(2486.00)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].nationalInsuranceRate", Matchers.`is`(5.00)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].nationalInsuranceValue", Matchers.`is`(124.30)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].taxRate", Matchers.`is`(10.00)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].taxAmount", Matchers.`is`(248.60)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].netValue", Matchers.`is`(2187.68)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].payslipLine", Matchers.`is`("00000000000197084172E201812310024860005000001243012000002983200206337")))

        verify(payslipService).updateTaxRate(month = month, year = year, taxRate = taxRate)
    }

    private fun createPayslips() =
        listOf(
            Payslip(
                id = 1,
                idNumber = "97084172E",
                date = "20181231",
                payslipLine = "00000000000197084172E201812310024860005000001243012000002983200206337",
                grossValue = 2486.00.toBigDecimal().setScale(2),
                nationalInsuranceRate = 5.00.toBigDecimal().setScale(2),
                nationalInsuranceValue = 124.30.toBigDecimal().setScale(2),
                taxRate = 12.00.toBigDecimal().setScale(2),
                taxAmount = 298.32.toBigDecimal().setScale(2),
                netValue = 2063.37.toBigDecimal().setScale(2)
            )
        )

    private fun createUpdatedPayslips() =
        listOf(
            Payslip(
                id = 1,
                idNumber = "97084172E",
                date = "20181231",
                payslipLine = "00000000000197084172E201812310024860005000001243012000002983200206337",
                grossValue = 2486.00.toBigDecimal().setScale(2),
                nationalInsuranceRate = 5.00.toBigDecimal().setScale(2),
                nationalInsuranceValue = 124.30.toBigDecimal().setScale(2),
                taxRate = 10.00.toBigDecimal().setScale(2),
                taxAmount = 248.60.toBigDecimal().setScale(2),
                netValue = 2187.68.toBigDecimal().setScale(2)
            )
        )

}