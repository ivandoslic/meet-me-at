# ADR-002: Use Kotlin and Spring Boot for backend services

Status: Accepted  
Date: 2026-07-17

## Context

The backend should expose realistic corporate patterns while remaining
approachable to a single developer already using Kotlin for Android.

## Decision

Use Kotlin with Spring Boot for JVM backend services and Spring Cloud Gateway for
the API gateway.

## Alternatives considered

- TypeScript with NestJS
- Java with Spring Boot
- Go services
- One backend language per service

## Consequences

- Kotlin knowledge transfers between Android and backend work.
- Spring provides mature security, persistence, messaging, and observability
  integrations.
- Services may share build conventions but must not share domain models.
- JVM resource use is higher than some alternatives, which matters on a laptop.

