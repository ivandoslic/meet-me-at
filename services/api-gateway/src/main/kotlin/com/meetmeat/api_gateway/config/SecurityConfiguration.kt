package com.meetmeat.api_gateway.config

import com.meetmeat.api_gateway.security.AudienceValidator
import com.meetmeat.api_gateway.security.KeycloakRealmRoleConverter
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
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter
import reactor.core.publisher.Mono


@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        @Qualifier("jwtAuthenticationConverter")
        authenticationConverter:
            Converter<Jwt, Mono<AbstractAuthenticationToken>>,
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
                authorize("/api/admin/**", hasAuthority("ROLE_APP_ADMIN"))
                authorize("/api/moderation/**", hasAuthority("ROLE_MODERATOR"))
                authorize("/api/**", hasAuthority("ROLE_USER"))
                authorize(anyExchange, permitAll)
            }

            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = authenticationConverter
                }
            }
        }

    @Bean
    fun jwtAuthenticationConverter():
        Converter<Jwt, Mono<AbstractAuthenticationToken>> {
        val converter = JwtAuthenticationConverter()

        converter.setPrincipalClaimName("preferred_username")
        converter.setJwtGrantedAuthoritiesConverter(
            KeycloakRealmRoleConverter(),
        )

        return ReactiveJwtAuthenticationConverterAdapter(converter)
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