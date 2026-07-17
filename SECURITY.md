# Security policy

DropIn is an educational project and is not intended for production use without
a separate security review.

## Local secrets

- Commit only `.env.example` files with safe placeholder values.
- Keep real secrets outside version control.
- Never embed a client secret in a browser or mobile application.
- Do not use development credentials outside the local environment.
- Treat exported identity configuration as configuration, not as a place for
  secrets.

## Authentication baseline

Browser and mobile clients use OpenID Connect Authorization Code Flow with PKCE
and are configured as public clients. Backend services validate access tokens;
ID tokens are not API authorization credentials.

## Reporting

Record a discovered security problem privately before publishing a detailed
exploit. If this becomes a shared repository, replace this section with an
actual private reporting channel.

