package dev.chiaradia.payrollservice.config.feign

import dev.chiaradia.payrollservice.exception.PayslipProException
import dev.chiaradia.payrollservice.service.client.PayslipProApiClient
import feign.Response
import feign.codec.Decoder
import feign.codec.Encoder
import feign.hystrix.HystrixFeign
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus

@RefreshScope
@Configuration
class PayslipProClientConfig {

    @Bean
    fun payslipProApiClient(
        @Value(value = "\${payslippro.api.url}") url: String,
        encoder: Encoder,
        decoder: Decoder
    ): PayslipProApiClient {
        return HystrixFeign.builder()
            .encoder(encoder)
            .decoder(decoder)
            .errorDecoder { _: String?, response: Response -> PayslipProException(HttpStatus.valueOf(response.status()), response.reason()) }
            .target(PayslipProApiClient::class.java, url)
    }

}