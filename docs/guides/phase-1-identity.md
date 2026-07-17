# Phase 1 guide: Identity

Phase 1 establishes authentication infrastructure and proves that tokens can be
obtained and understood. It does not implement the API Gateway, Profile Service,
web UI, or Android UI.

## Goal

At the end of the phase, a developer can start PostgreSQL and Keycloak through
the root Compose project, log into the Keycloak account interface with a safe
development user, obtain tokens through a standards-based public-client flow,
inspect the token claims, and reproduce the realm from version-controlled
configuration.

## Mental model

Keycloak is the Authorization Server and OpenID Provider. The future web and
Android applications are public OIDC clients. Future backend APIs are resource
servers.

OAuth 2.0 delegates access. OpenID Connect adds user authentication and identity
claims. Keycloak performs login and issues tokens; it does not replace Meet Me @'s
domain authorization.

## Recommended implementation sequence

### 1. Add PostgreSQL

Give Keycloak a dedicated database and database principal. Persist database data
in a named volume. Add a real readiness check and avoid exposing PostgreSQL to
the host unless local administration requires it.

Do not create application-service schemas yet.

### 2. Add Keycloak

Pin a specific Keycloak container image instead of using `latest`. Start in
development mode only for the local environment. Add a health check, explicit
hostname/port settings as needed, and dependency on PostgreSQL readiness.

Bootstrap administrator credentials through local environment variables. They
are development bootstrap values, not application-user credentials.

### 3. Create the `meet-me-at` realm

Do not build the application inside the `master` realm. The master realm is for
administering Keycloak itself.

Define:

- Realm name: `meet-me-at`
- Registration and email-verification policy for local development
- Login and session lifetimes intentionally rather than leaving them unexplored
- Brute-force protection experiments
- A simple password policy appropriate for development learning

### 4. Define coarse realm roles

Begin with:

- `user`
- `moderator`
- `admin`

Assign `user` as a normal default role through an appropriate role mapping.
Avoid creating roles such as `hangout-owner`; ownership is resource-specific and
belongs in the Hangout Service.

### 5. Create public clients

Create separate clients for:

- `meet-me-at-web`
- `meet-me-at-android`

Both clients are public: client authentication is disabled because browser and
mobile binaries cannot protect a secret.

Enable Standard Flow and require PKCE with `S256`. Disable flows that are not
needed, especially Direct Access Grants and Implicit Flow. Configure exact
redirect URIs and web origins; avoid broad wildcards beyond tightly controlled
local development.

The Android redirect URI will eventually use an application-controlled custom
scheme or verified app link. Decide the final application identifier before
locking it down.

### 6. Design tokens deliberately

Inspect access-token and ID-token claims and understand the difference. Keep
access tokens small. Do not put mutable profile data or large permission lists in
tokens.

The stable Keycloak `sub` claim becomes the identity link stored by Meet Me @
services. Usernames and email addresses can change and should not be used as
foreign keys.

Plan an API audience before the first resource server exists. When Phase 2
begins, services must validate issuer, signature, expiry, and audience rather
than merely decode a JWT.

### 7. Create development users

Create at least:

- A normal user
- A moderator
- An administrator

Use obviously fake local identities. Test role assignments and confirm that the
resulting tokens contain only the claims you intended.

### 8. Make configuration reproducible

Treat clicks in the Admin Console as exploration, not the final setup method.
Export or automate the realm configuration and verify that a clean local
environment can recreate it.

Do not commit secrets, active sessions, or production user data. Prefer a
separate safe development realm representation.

### 9. Verify behavior

Verify at minimum:

- Keycloak starts only after PostgreSQL is ready.
- Realm discovery metadata is reachable.
- Authorization Code + PKCE succeeds for an appropriate test client/tool.
- Invalid redirect URIs are rejected.
- Direct Access Grants are disabled.
- Tokens have the expected issuer, subject, audience, expiry, scopes, and roles.
- Refresh and logout behavior is understood.
- Restarting containers preserves data.
- Recreating from the stored realm configuration produces the same setup.

## Important traps

- Do not use the Resource Owner Password Credentials flow as the application
  login mechanism, even if it appears convenient for a script.
- Do not store a client secret in React or Android code.
- Do not send an ID token to a business API in place of an access token.
- Do not use the Keycloak username as the durable application identity.
- Do not trust a JWT merely because it can be Base64-decoded.
- Do not give every user broad administration roles for convenience.
- Do not customize the login theme before the protocol behavior works.
- Do not allow permissive redirect URI wildcards without understanding the
  security consequence.

## Phase completion checklist

- [ ] PostgreSQL and Keycloak are defined in Compose with pinned images.
- [ ] Keycloak uses its own persistent PostgreSQL database.
- [ ] The `meet-me-at` realm exists outside the master realm.
- [ ] Realm roles are defined and tested.
- [ ] Web and Android public clients exist.
- [ ] Authorization Code Flow with PKCE S256 works.
- [ ] Unused or unsafe flows are disabled.
- [ ] Development users cover normal, moderator, and admin cases.
- [ ] Token claims have been inspected and documented.
- [ ] Realm configuration is reproducible from the repository.
- [ ] No secret or real personal data is committed.

## Primary references

- Keycloak getting started with containers:
  https://www.keycloak.org/getting-started/getting-started-docker
- Keycloak server configuration:
  https://www.keycloak.org/server/configuration
- Keycloak server administration guide:
  https://www.keycloak.org/docs/latest/server_admin/
- Keycloak OpenID Connect endpoints and flows:
  https://www.keycloak.org/securing-apps/oidc-layers
- OAuth 2.0 Security Best Current Practice, RFC 9700:
  https://www.rfc-editor.org/rfc/rfc9700.html
- Proof Key for Code Exchange, RFC 7636:
  https://www.rfc-editor.org/rfc/rfc7636.html
- OpenID Connect Core:
  https://openid.net/specs/openid-connect-core-1_0.html
- OAuth 2.0 for native apps, RFC 8252:
  https://www.rfc-editor.org/rfc/rfc8252.html

Read the Keycloak getting-started material first, then use the standards to
understand why the settings exist rather than copying a realm configuration
blindly.

