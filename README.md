# Meet Me @

Meet Me @ is a mock corporate social platform for creating temporary real-world
hangouts. A user can announce that they are at a venue, invite others, discover
nearby hangouts, join them, and receive notifications.

The project exists primarily as a learning environment for identity, mobile and
web clients, microservices, event-driven integration, observability, local LLMs,
and production-minded engineering practices.

## Current status

This repository contains **Phase 0: repository and architecture foundation**.
No application service has been implemented yet.

The next milestone is **Phase 1: Identity**, described in
[`docs/guides/phase-1-identity.md`](docs/guides/phase-1-identity.md).

## Intended platform

- Keycloak for OAuth 2.0 and OpenID Connect
- Kotlin and Spring Boot backend services
- Kotlin and Jetpack Compose Android application
- React and TypeScript web application
- PostgreSQL for relational persistence
- RabbitMQ for asynchronous integration
- Redis for caching and short-lived state when justified
- MinIO for S3-compatible object storage
- Python, FastAPI, Ollama, and pgvector for AI-assisted discovery
- OpenTelemetry and the Grafana stack for observability
- Docker Compose for local orchestration

Technology versions are deliberately not pinned during Phase 0. Each runtime is
introduced and pinned in the milestone that first uses it.

## Repository map

```text
apps/              User-facing applications
services/          Independently deployable backend services
contracts/         HTTP and event contract descriptions
infrastructure/    Local platform configuration
docs/              Architecture, decisions, conventions, and guides
```

See [`docs/conventions/repository.md`](docs/conventions/repository.md) for the
full ownership rules.

## Local orchestration

The root [`compose.yaml`](compose.yaml) is intentionally empty in Phase 0. The
first containers—PostgreSQL and Keycloak—are added during Phase 1.

The long-term developer experience is one root command that starts a collection
of cooperating containers. “One Docker setup” means one Compose project, not one
container containing the entire platform.

Mobile applications run on their host IDE/emulator and connect to the platform;
they do not run inside Docker.

## Engineering rules

1. A service owns its data and migrations.
2. Clients enter through the API gateway, except for the OIDC authorization
   interaction with Keycloak.
3. Authentication belongs to Keycloak; resource authorization belongs to the
   service that owns the resource.
4. Services do not query another service's tables.
5. HTTP is used for immediate answers; events describe facts that already
   happened.
6. AI must remain outside critical business workflows.
7. Every important architectural choice is recorded as an ADR.
8. Local simplicity comes before simulated enterprise scale.

## Suggested workflow

Before changing a major boundary, add or update an ADR. Keep pull requests
focused on one vertical slice and update relevant contracts and documentation in
the same change.

