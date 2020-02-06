package dev.chiaradia.payrollservice.exception

import com.netflix.hystrix.exception.ExceptionNotWrappedByHystrix
import org.springframework.http.HttpStatus

class PayslipProException(var httpStatus: HttpStatus, message: String) : RuntimeException(message), ExceptionNotWrappedByHystrix {

}