package dev.chiaradia.payrollservice.config.feign

import feign.Request
import feign.codec.Decoder
import feign.codec.Encoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig(
    var messageConverters: ObjectFactory<HttpMessageConverters>
) {
    @Bean
    fun springEncoder(): Encoder = SpringEncoder(messageConverters)

    @Bean
    fun springDecoder(): Decoder = ResponseEntityDecoder(SpringDecoder(messageConverters))

    @Bean
    fun options(): Request.Options = Request.Options(CONNECTION_TIMEOUT, READ_TIMEOUT)

    companion object {
        private const val CONNECTION_TIMEOUT = 60000
        private const val READ_TIMEOUT = 60000
    }

}