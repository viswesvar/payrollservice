package dev.chiaradia.payrollservice.service

import dev.chiaradia.payrollservice.exception.PayslipParsingException
import dev.chiaradia.payrollservice.exception.PayslipsNotFoundException
import dev.chiaradia.payrollservice.service.cache.CachedPayslipService
import dev.chiaradia.payrollservice.service.client.PayslipProApiClient
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class CachedPayslipServiceTest {

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    @Mock
    lateinit var payslipProApiClient: PayslipProApiClient

    @InjectMocks
    lateinit var defaultPayslipService: CachedPayslipService

    @Test
    fun `Given a request, when getting payslips, should return Payslip list`() {
        val payslipLine = "00000000000197084172E201812310024860005000001243012000002983200206337"

        `when`(payslipProApiClient.getPayslipsByMonthAndYear(anyLong(), anyLong()))
            .thenReturn(payslipLine)

        val payslips = defaultPayslipService.getPayslips(12, 2018)

        assertThat(payslips, notNullValue())
        assertEquals(1, payslips.size)
        assertEquals(1, payslips[0].id)
        assertEquals("97084172E", payslips[0].idNumber)
        assertEquals("20181231", payslips[0].date)
        assertEquals("00000000000197084172E201812310024860005000001243012000002983200206337", payslips[0].payslipLine)
        assertEquals(2486.00.toBigDecimal().setScale(2), payslips[0].grossValue)
        assertEquals(5.00.toBigDecimal().setScale(2), payslips[0].nationalInsuranceRate)
        assertEquals(124.30.toBigDecimal().setScale(2), payslips[0].nationalInsuranceValue)
        assertEquals(12.00.toBigDecimal().setScale(2), payslips[0].taxRate)
        assertEquals(298.32.toBigDecimal().setScale(2), payslips[0].taxAmount)
        assertEquals(2063.37.toBigDecimal().setScale(2), payslips[0].netValue)
    }

    @Test
    fun `Given a request, when updating taxRate, should return Payslip list updated`() {
        val payslipLine = "00000000000197084172E201812310024860005000001243012000002983200206337"

        `when`(payslipProApiClient.getPayslipsByMonthAndYear(anyLong(), anyLong()))
            .thenReturn(payslipLine)

        val payslips = defaultPayslipService.updateTaxRate(12, 2018, 10.0)

        assertThat(payslips, notNullValue())
        assertEquals(1, payslips.size)
        assertEquals(1, payslips[0].id)
        assertEquals("97084172E", payslips[0].idNumber)
        assertEquals("20181231", payslips[0].date)
        assertEquals("00000000000197084172E201812310024860005000001243010000002486000218768", payslips[0].payslipLine)
        assertEquals(2486.00.toBigDecimal().setScale(2), payslips[0].grossValue)
        assertEquals(5.00.toBigDecimal().setScale(2), payslips[0].nationalInsuranceRate)
        assertEquals(124.30.toBigDecimal().setScale(2), payslips[0].nationalInsuranceValue)
        assertEquals(10.00.toBigDecimal().setScale(2), payslips[0].taxRate)
        assertEquals(248.60.toBigDecimal().setScale(2), payslips[0].taxAmount)
        assertEquals(2187.68.toBigDecimal().setScale(2), payslips[0].netValue)
    }

    @Test
    fun `Given a request, when receive invalid payslips, should throw PayslipParsingException`() {
        val wrongPayslipLine = "00000000000197084172E20181231002486000500000124301200000298320"

        `when`(payslipProApiClient.getPayslipsByMonthAndYear(anyLong(), anyLong()))
            .thenReturn(wrongPayslipLine)

        expectedException.expect(PayslipParsingException::class.java)
        defaultPayslipService.getPayslips(12, 2018)

    }

    @Test
    fun `Given a request, when receive empty payslips, should throw PayslipsNotFoundException`() {
        val wrongPayslipLine = ""

        `when`(payslipProApiClient.getPayslipsByMonthAndYear(anyLong(), anyLong()))
            .thenReturn(wrongPayslipLine)

        expectedException.expect(PayslipsNotFoundException::class.java)
        defaultPayslipService.getPayslips(12, 2018)

    }

}