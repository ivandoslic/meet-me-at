# ADR-004: Use RabbitMQ for asynchronous integration

Status: Accepted  
Date: 2026-07-17

## Context

Notifications, search indexing, and analytics should not extend the latency or
availability requirements of core user transactions.

## Decision

Introduce RabbitMQ when the first asynchronous use case is implemented. Events
will describe completed domain facts and consumers will be idempotent.

## Alternatives considered

- Synchronous HTTP for every interaction
- Kafka
- Redis Pub/Sub
- An in-process event bus

## Consequences

- The system gains asynchronous delivery and retry semantics without Kafka's
  larger operational footprint.
- Event schemas require compatibility discipline.
- Delivery is at least once, so consumers must tolerate duplicates.
- Reliable publication will eventually require an outbox pattern.

