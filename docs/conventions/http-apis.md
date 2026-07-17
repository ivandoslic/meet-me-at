# HTTP API conventions

These conventions become binding when the first business API is designed.

## General rules

- Public APIs are rooted under `/api` at the gateway.
- Resource names use plural nouns.
- JSON field names use `camelCase`.
- Timestamps use ISO 8601 UTC representations.
- Identifiers are opaque to clients.
- Pagination uses a consistent repository-wide strategy once selected.
- Idempotency keys are considered for retryable create operations.

## Authentication

- Clients send access tokens as Bearer tokens.
- ID tokens are not accepted as API authorization credentials.
- Services validate issuer, signature, expiry, and intended audience according
  to the final Keycloak token design.
- Resource authorization is enforced in the owning service even if the gateway
  performed coarse access checks.

## Errors

Use a consistent problem response based on RFC 9457 Problem Details. Responses
may expose a stable error code and correlation identifier, but not stack traces,
SQL, tokens, or secrets.

## Contracts

OpenAPI is the source of truth for exposed HTTP behavior. A contract change and
its implementation belong in the same change set.

