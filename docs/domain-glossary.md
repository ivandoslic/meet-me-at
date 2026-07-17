# Domain glossary

## User

An authenticated person known to Keycloak. A User becomes visible within the
product through a Profile.

## Profile

Application-owned social information associated with a Keycloak subject. It
does not contain passwords or authentication credentials.

## Hangout

A temporary invitation to meet at a physical venue or location. A Hangout has
an owner, time window, visibility, capacity, lifecycle state, and participants.

## Participant

A User who joined a Hangout. The owner and participant are distinct roles even
if the owner is automatically considered present.

## Connection

A social relationship between Users. The exact friend-versus-follow model is
deferred until the Social Service milestone.

## Invitation

A directed request for a User to join a Hangout. Invitations are different from
discovering a publicly visible Hangout.

## Notification

An application record informing a User that a relevant domain event occurred.

## Venue

A named physical place associated with coordinates and optional external place
metadata. Venue data is initially embedded in a Hangout; extracting a Venue
Service requires demonstrated need.

