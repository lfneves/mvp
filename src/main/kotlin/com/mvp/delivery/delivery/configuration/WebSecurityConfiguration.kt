package com.mvp.delivery.delivery.configuration

import com.mvp.delivery.delivery.controller.security.CustomAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@Configuration
@EnableWebFluxSecurity
class WebSecurityConfiguration {

    @Autowired
    private lateinit var customReactiveAuthenticationManager: ReactiveAuthenticationManager

    @Autowired
    private lateinit var customAuthenticationFilter: CustomAuthenticationFilter

    @Bean
    fun getSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.authorizeExchange().pathMatchers(
            "/api/auth/*", "/api/v1/users/signup", "/configuration/ui", "/swagger-resources/**", "/configuration/**",
            "/swagger-ui.html", "/webjars/**", "/swagger-ui/**", "/v3/api-docs/**"
        ).permitAll()
            .pathMatchers("/**").authenticated()
            .and().csrf().disable()
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authenticationManager(customReactiveAuthenticationManager)
            .addFilterAt(customAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }
}