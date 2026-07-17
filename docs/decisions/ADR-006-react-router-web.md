# ADR-006: Use React Router Framework Mode for the web client

Status: Accepted  
Date: 2026-07-17

## Context

The web application needs a current React architecture, while business APIs are
already provided by separate backend services.

## Decision

Use React, TypeScript, and React Router Framework Mode. Avoid introducing a
Next.js server unless a later requirement genuinely needs that deployment model.

## Alternatives considered

- Next.js
- A manually assembled React SPA with only declarative routing
- Angular
- Vue

## Consequences

- The client gains structured routing, data loading, actions, error boundaries,
  and code splitting.
- The architecture avoids an accidental second business backend.
- Server-side rendering remains possible but is not mandatory for the first
  authenticated application.

