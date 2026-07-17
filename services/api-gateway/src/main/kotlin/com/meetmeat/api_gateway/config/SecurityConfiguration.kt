package com.meetmeat.api_gateway.config

import com.meetmeat.api_gateway.security.AudienceValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
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

    @Bean
    fun jwtDecoder(
        @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
        issuerUri: String,
        @Value("\${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
        jwkSetUri: String,
        @Value("\${meet-me-at.security.jwt.required-audience}")
        requiredAudience: String,
    ): ReactiveJwtDecoder {
        val decoder = NimbusReactiveJwtDecoder
            .withJwkSetUri(jwkSetUri)
            .build()

        val validator = DelegatingOAuth2TokenValidator<Jwt>(
            JwtValidators.createDefaultWithIssuer(issuerUri),
            AudienceValidator(requiredAudience),
        )

        decoder.setJwtValidator(validator)

        return decoder
    }
}