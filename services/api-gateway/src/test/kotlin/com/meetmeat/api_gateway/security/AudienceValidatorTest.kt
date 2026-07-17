package com.meetmeat.api_gateway.security

import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.springframework.security.oauth2.jwt.Jwt

class AudienceValidatorTest {

    private val validator = AudienceValidator("meet-me-at-api")

    @Test
    fun `accepts the required audience`() {
        val result = validator.validate(
            jwtWithAudiences("account", "meet-me-at-api"),
        )

        assertFalse(result.hasErrors())
    }

    @Test
    fun `rejects a missing required audience`() {
        val result = validator.validate(
            jwtWithAudiences("account"),
        )

        assertTrue(result.hasErrors())
    }

    private fun jwtWithAudiences(vararg audiences: String): Jwt =
        Jwt.withTokenValue("test-token")
            .header("alg", "none")
            .subject("test-subject")
            .audience(audiences.toList())
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(300))
            .build()
}