# Observability conventions

## Logs

- Emit structured logs in server components.
- Include service name, environment, severity, and correlation identifier.
- Do not log access tokens, refresh tokens, passwords, authorization headers, or
  sensitive personal data.

## Traces

Propagate W3C Trace Context over HTTP. When messaging is introduced, propagate
trace and correlation context in event metadata without coupling consumers to a
specific tracing backend.

## Metrics

Begin with request count, duration, failures, and dependency health. Add
business metrics only when their interpretation is documented.

## Health

Distinguish liveness from readiness when the runtime supports it. A readiness
check should fail when the component cannot safely serve its intended requests.

