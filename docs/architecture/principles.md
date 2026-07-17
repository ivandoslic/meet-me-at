# Architecture principles

## Prefer a vertical slice

A working path from client through authentication, API, service, and database is
more valuable than many empty services.

## Keep service boundaries real

A directory is not a microservice boundary by itself. A service must own a
cohesive capability, data, migrations, runtime, and contract.

## Separate identity from profile

Keycloak manages login, credentials, sessions, and coarse roles. Meet Me @ manages
social profiles and resource-specific permissions.

## Own data locally

No service reads another service's persistence structures. Duplication through
events is acceptable when it reduces runtime coupling.

## Make optional systems optional

Loss of Ollama, notification delivery, analytics, or observability visualization
must not prevent core Hangout operations.

## Avoid premature infrastructure

RabbitMQ, Redis, MinIO, PostGIS, and Kubernetes are added when a concrete
milestone uses them, not merely because they appear in an architecture diagram.

## Design for diagnosis

Every request and message should eventually be traceable across boundaries.
Errors must be actionable and must not expose secrets or internal stack traces
to clients.

