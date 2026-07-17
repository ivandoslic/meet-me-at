package com.meetmeat.api_gateway.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

class KeycloakRealmRoleConverter :
    Converter<Jwt, Collection<GrantedAuthority>> {

    private val scopeConverter = JwtGrantedAuthoritiesConverter()

    override fun convert(source: Jwt): Collection<GrantedAuthority> {
        val scopeAuthorities =
            scopeConverter.convert(source)?.toList().orEmpty()

        val realmAccess =
            source.getClaimAsMap("realm_access").orEmpty()

        val realmRoles =
            (realmAccess["roles"] as? Collection<*>)
                .orEmpty()
                .filterIsInstance<String>()

        val roleAuthorities = realmRoles
            .mapNotNull { ROLE_MAPPINGS[it] }
            .map(::SimpleGrantedAuthority)

        return (scopeAuthorities + roleAuthorities)
            .distinctBy { it.authority }
    }

    private companion object {
        val ROLE_MAPPINGS = mapOf(
            "user" to "ROLE_USER",
            "moderator" to "ROLE_MODERATOR",
            "app-admin" to "ROLE_APP_ADMIN",
        )
    }
}