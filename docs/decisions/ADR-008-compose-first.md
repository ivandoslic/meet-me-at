# ADR-008: Use Docker Compose before Kubernetes

Status: Accepted  
Date: 2026-07-17

## Context

The platform needs reproducible local infrastructure, but Kubernetes would add
deployment complexity before any business slice exists.

## Decision

Use one root Docker Compose project with optional profiles. Introduce Kubernetes
only after services, contracts, health checks, and observability exist.

## Alternatives considered

- Start directly with Kubernetes and Helm.
- Run every dependency manually on the host.
- Put the complete platform in one container.

## Consequences

- The full local platform can be managed as a unit.
- Each server component remains an independent container.
- Mobile clients continue running outside Docker.
- A later Kubernetes phase becomes a deployment exercise rather than a way to
  discover basic service boundaries.

