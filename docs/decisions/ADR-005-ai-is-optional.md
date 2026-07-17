# ADR-005: Keep AI outside critical business workflows

Status: Accepted  
Date: 2026-07-17

## Context

Local models may be slow, unavailable, or nondeterministic. Creating and joining
a Hangout must remain reliable.

## Decision

Isolate LLM and embedding functionality in the Discovery AI Service. The first
use case is optional semantic discovery, backed by Ollama and pgvector.

## Alternatives considered

- Call Ollama directly from each backend service.
- Make an LLM decide authorization or core state transitions.
- Exclude AI entirely.

## Consequences

- Core features continue working when AI is unavailable.
- AI-specific dependencies remain isolated in Python.
- Search indexes are derived data and can be rebuilt from source information.

