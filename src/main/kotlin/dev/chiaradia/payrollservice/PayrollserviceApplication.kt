package dev.chiaradia.payrollservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.Collections


@EnableScheduling
@EnableCaching
@SpringBootApplication
@EnableSwagger2
class PayrollserviceApplication {

    @Bean
    fun docket(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(javaClass.getPackage().name))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
    }


    fun apiInfo(): ApiInfo? {
        return ApiInfo(
            "Payroll Service API",
            "API for gathering information regards payrolls.",
            "API TOS",
            "Terms of service",
            Contact("Luiz Felipe Chiaradia", "www.github.com/chiaradia", "luiz.fcc@gmail.com"),
            "License of API", "API license URL", Collections.emptyList())
    }

}

fun main(args: Array<String>) {
    runApplication<PayrollserviceApplication>(*args)
}

