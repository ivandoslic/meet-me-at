# Configuration and secrets

## Sources

Configuration follows this precedence conceptually:

1. Safe application defaults
2. Version-controlled environment-specific non-secret configuration
3. Environment variables
4. Secret provider or local uncommitted secret values

## Naming

- Use uppercase snake case for environment variables.
- Prefix component-specific variables with the component name where ambiguity is
  possible.
- Use explicit units in names such as `_SECONDS`, `_MILLIS`, or `_BYTES`.

## Local environment

Commit `.env.example` with placeholders and explanations. Never commit `.env`.
Compose health checks must represent readiness to accept the dependency's
intended traffic rather than merely prove that a process exists.

## Production direction

The local `.env` approach is not a production secret-management design. The
deployment milestone will select an external secret mechanism.

