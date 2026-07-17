# Delivery roadmap

## Phase 0 — Foundation

- Repository structure
- Architecture description
- Engineering conventions
- Architecture Decision Records
- Root Compose skeleton

## Phase 1 — Identity

- PostgreSQL and Keycloak containers
- Meet Me @ realm
- Roles, scopes, and public clients
- Authorization Code Flow with PKCE
- Reproducible realm configuration
- Manual token inspection and validation experiments

## Phase 2 — First vertical slice

- API Gateway
- Profile Service
- Web login
- Android login
- View and edit current profile

## Phase 3 — Core product

- Create, list, view, join, leave, cancel, and expire Hangouts

## Phase 4 — Event-driven behavior

- RabbitMQ
- Domain event publication
- Notification Service
- In-app notifications

## Phase 5 — Social functionality

- Connections, invitations, blocking, and visibility rules

## Phase 6 — AI discovery

- Ollama
- Embeddings
- Semantic indexing and natural-language Hangout search

## Phase 7 — Media and location

- MinIO
- Uploaded images
- Radius search and possible PostGIS adoption

## Phase 8 — Observability and resilience

- OpenTelemetry
- Metrics, traces, and centralized logs
- Timeouts, retries, circuit breaking, and rate limiting

## Phase 9 — Production-like deployment

- Kubernetes learning environment
- Helm
- Environment promotion and external secret management

