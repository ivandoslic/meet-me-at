package com.meetmeat.api_gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
    ): SecurityWebFilterChain =
        http {
            csrf {
                disable()
            }

            httpBasic {
                disable()
            }

            formLogin {
                disable()
            }

            logout {
                disable()
            }

            authorizeExchange {
                authorize("/actuator/health", permitAll)
                authorize("/actuator/health/**", permitAll)
                authorize("/api/**", authenticated)
                authorize(anyExchange, permitAll)
            }

            oauth2ResourceServer {
                jwt { }
            }
        }
}