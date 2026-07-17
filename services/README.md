# Services

Each child directory represents an independently deployable process and owns its
runtime configuration, tests, container image, persistence migrations, and
public contracts.

Services must not import another service's domain or persistence model. Small
technical libraries may eventually be shared for concerns such as telemetry,
but shared business-model libraries are intentionally avoided.

