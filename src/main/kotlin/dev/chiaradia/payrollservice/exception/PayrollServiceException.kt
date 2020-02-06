package dev.chiaradia.payrollservice.exception

import com.netflix.hystrix.exception.ExceptionNotWrappedByHystrix
import org.springframework.http.HttpStatus

class PayrollServiceException(var httpStatus: HttpStatus, message: String) : RuntimeException(message), ExceptionNotWrappedByHystrix {

}