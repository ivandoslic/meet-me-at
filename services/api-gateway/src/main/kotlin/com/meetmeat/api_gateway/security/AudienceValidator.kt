package com.meetmeat.api_gateway.security

import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt

class AudienceValidator(
    private val requiredAudience: String,
) : OAuth2TokenValidator<Jwt> {

    override fun validate(token: Jwt): OAuth2TokenValidatorResult {
        if (requiredAudience in token.audience.orEmpty()) {
            return OAuth2TokenValidatorResult.success()
        }

        val error = OAuth2Error(
            "invalid_token",
            "Token is not intended for audience '$requiredAudience'",
            null,
        )

        return OAuth2TokenValidatorResult.failure(error)
    }
}