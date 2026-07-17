# Container view

The word “container” here means both a deployable application boundary and,
locally, the corresponding Docker container.

```mermaid
flowchart TD
    WEB[React web app]
    ANDROID[Android app]
    KC[Keycloak]
    GW[API Gateway]
    PROFILE[Profile Service]
    HANGOUT[Hangout Service]
    SOCIAL[Social Service]
    NOTIFY[Notification Service]
    AI[Discovery AI Service]
    MQ[RabbitMQ]
    DB[(PostgreSQL)]
    OLLAMA[Ollama]

    WEB --> KC
    ANDROID --> KC
    WEB --> GW
    ANDROID --> GW
    GW --> PROFILE
    GW --> HANGOUT
    GW --> SOCIAL
    GW --> AI
    HANGOUT --> MQ
    SOCIAL --> MQ
    MQ --> NOTIFY
    MQ --> AI
    PROFILE --> DB
    HANGOUT --> DB
    SOCIAL --> DB
    NOTIFY --> DB
    AI --> DB
    AI --> OLLAMA
```

## Access rules

- Clients interact directly with Keycloak only for standard identity flows.
- Business API traffic enters through the gateway.
- Ollama is internal and is called only by the AI service.
- A single local PostgreSQL process may host isolated service databases.
- Asynchronous consumers tolerate redelivery and process messages idempotently.

