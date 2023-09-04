package com.mvp.delivery

import com.mvp.delivery.domain.configuration.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing

@SpringBootApplication
@EnableR2dbcAuditing
@EnableCaching
class DeliveryApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DeliveryApplication::class.java, *args)
        }
    }

    @Bean
    fun applicationStartupRunner(): ApplicationRunner? {
        return ApplicationRunner()
    }
}