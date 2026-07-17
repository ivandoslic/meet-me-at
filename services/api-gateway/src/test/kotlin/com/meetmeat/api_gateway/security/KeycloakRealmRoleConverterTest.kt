package com.meetmeat.api_gateway.security

import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import org.springframework.security.oauth2.jwt.Jwt

class KeycloakRealmRoleConverterTest {

    private val converter = KeycloakRealmRoleConverter()

    @Test
    fun `maps recognized realm roles and preserves scopes`() {
        val authorities = converter
            .convert(
                jwt(
                    "user",
                    "moderator",
                    "app-admin",
                    "offline_access",
                ),
            )
            .map { it.authority }
            .toSet()

        assertEquals(
            setOf(
                "SCOPE_openid",
                "SCOPE_profile",
                "ROLE_USER",
                "ROLE_MODERATOR",
                "ROLE_APP_ADMIN",
            ),
            authorities,
        )
    }

    @Test
    fun `ignores unrecognized Keycloak roles`() {
        val authorities = converter
            .convert(
                jwt(
                    "offline_access",
                    "uma_authorization",
                    "default-roles-meet-me-at",
                ),
            )
            .map { it.authority }
            .toSet()

        assertEquals(
            setOf(
                "SCOPE_openid",
                "SCOPE_profile",
            ),
            authorities,
        )
    }

    private fun jwt(vararg roles: String): Jwt =
        Jwt.withTokenValue("test-token")
            .header("alg", "none")
            .subject("test-subject")
            .claim("scope", "openid profile")
            .claim(
                "realm_access",
                mapOf("roles" to roles.toList()),
            )
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(300))
            .build()
}