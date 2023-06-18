package com.mvp.delivery.delivery.domain.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .baseUrl(BASE_URL)
            .build()
    }

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }
}