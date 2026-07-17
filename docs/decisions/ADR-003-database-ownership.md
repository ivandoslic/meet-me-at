# ADR-003: Enforce database ownership per service

Status: Accepted  
Date: 2026-07-17

## Context

Independent services become tightly coupled when they read and modify a shared
schema.

## Decision

Each service owns its database or schema, database principal, migrations, and
persistence model. One PostgreSQL container may host these isolated databases
locally.

## Alternatives considered

- One shared application schema
- A PostgreSQL container per service from the first milestone
- A different database technology for every service

## Consequences

- Local resource consumption remains manageable.
- Service boundaries are enforced logically rather than by many local database
  processes.
- Cross-service joins are forbidden.
- Some read models will intentionally duplicate data.

