# Event conventions

These conventions apply when RabbitMQ is introduced.

## Meaning

Events use past-tense names and describe facts, for example
`HangoutCreated` or `UserJoinedHangout`. Consumers must not reinterpret an event
as a request that the producer still expects them to complete.

## Envelope

The common envelope will include at least:

- Unique event identifier
- Event type and schema version
- Occurrence timestamp
- Producer name
- Correlation identifier
- Causation identifier when applicable
- Payload

## Delivery

- Assume at-least-once delivery.
- Consumers are idempotent.
- Retries are bounded.
- Poison messages eventually move to a dead-letter destination.
- Acknowledgement occurs only after successful processing.

## Evolution

Prefer additive changes. Do not change the meaning of an existing field. A
breaking semantic change requires a new event version and explicit migration
plan.

