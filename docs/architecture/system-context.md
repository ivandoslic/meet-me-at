# System context

Meet Me @ connects people who want to meet at short-lived, location-based Hangouts.

## Actors

- Member: creates and joins Hangouts and manages a Profile.
- Moderator: handles reported or inappropriate content.
- Administrator: manages platform-level configuration and access.

## External systems

- Keycloak: authenticates users and issues tokens.
- Future email/push providers: deliver notifications.
- Future map/place provider: supplies location metadata.
- Ollama: performs local model inference for optional AI functionality.

## Trust boundary

Web and mobile clients are untrusted public clients. Requests crossing into the
platform must be authenticated and authorized at the appropriate layer. The
internal container network is not treated as sufficient proof of identity.

