# Contributing

This is a learning repository, but changes should follow production-minded
habits.

## Change expectations

- Keep a change scoped to one concern or vertical slice.
- Explain why a new dependency or infrastructure component is needed.
- Do not share service database tables or persistence models.
- Update OpenAPI or AsyncAPI contracts when an exposed contract changes.
- Add a migration rather than manually changing persistent data structures.
- Add or update an ADR when a decision changes a system boundary.
- Never commit passwords, tokens, private keys, or populated local `.env` files.

## Definition of done

A feature is done when its behavior is implemented, tested at the appropriate
level, observable, documented, and runnable through the documented local
workflow.

## Commit guidance

Use short imperative summaries. Conventional Commits are optional; clarity is
more important than mechanically following a format.

