# Contracts

Contracts are stable integration boundaries, not a place for shared service
implementation models.

- `openapi/` describes synchronous HTTP APIs.
- `asyncapi/` will describe asynchronous events once RabbitMQ is introduced.

Generated code is derived from contracts and should not become the hand-edited
source of truth.

