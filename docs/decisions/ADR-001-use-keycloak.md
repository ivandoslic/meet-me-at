# ADR-001: Use Keycloak as the identity provider

Status: Accepted  
Date: 2026-07-17

## Context

Meet Me @ needs users, login, sessions, roles, OAuth 2.0, and OpenID Connect. Writing
an authorization server would consume the project while providing a weaker
security baseline.

## Decision

Use Keycloak for authentication, token issuance, user administration, coarse
realm roles, and development identity federation experiments.

Meet Me @ services remain responsible for resource-specific authorization.

## Alternatives considered

- Build a custom authorization server.
- Use a hosted commercial identity provider.
- Put username/password handling in the first backend service.

## Consequences

- The project learns standards-based integration instead of password storage.
- Keycloak becomes critical local infrastructure.
- Realm configuration must be reproducible and version controlled.
- Keycloak availability is required for new logins and token refreshes.

