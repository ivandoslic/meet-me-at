# ADR-007: Defer iOS development

Status: Accepted  
Date: 2026-07-17

## Context

The current development environment is Windows. Apple requires macOS and Xcode
for local iOS compilation and simulation, including Compose Multiplatform iOS
targets.

## Decision

Implement the Android client first and reserve an iOS application directory.
Prefer native SwiftUI when macOS access becomes available; evaluate sharing
non-UI Kotlin code separately.

## Alternatives considered

- Attempt unsupported local iOS development on Windows.
- Depend on a remote Mac from the first milestone.
- Replace the Android plan with a cross-platform JavaScript application.

## Consequences

- Android work remains straightforward on the available environment.
- iOS parity is delayed.
- Public API contracts must remain platform-neutral.

