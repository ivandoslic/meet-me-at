# Repository conventions

## Deployable boundaries

Anything under `services/` is expected to become independently buildable,
testable, configurable, and deployable. Anything under `apps/` is a user-facing
client.

## Build systems

This is a polyglot monorepo, not a single-build-tool monorepo.

- Backend JVM services: Gradle Kotlin DSL multi-project build
- Android: its own Gradle build
- Web: its own Node package and lockfile
- AI service: its own Python project and uv lockfile

The root coordinates common development tasks but does not hide the native tools
used by each ecosystem.

## Sharing

Allowed shared assets:

- API and event contracts
- Formatting and static-analysis configuration
- Telemetry conventions
- Test fixtures explicitly designed as contract fixtures

Avoid sharing:

- Persistence entities
- Domain aggregates
- Service-internal DTOs
- Business logic libraries spanning service boundaries

## Generated files

Generated outputs should be reproducible. Commit generated clients only when
the chosen toolchain or consumer workflow demonstrates that this is valuable;
record that choice in an ADR.

