# 02 – Functional Requirements

Source: PDF §3.3.1, §3.3.2, §3.3.3 and §5.

## Level 1 – Minimum Viable Product (PDF §3.3.1)

The solution contains exactly **one** collection (register) of activities.

| ID | The user shall be able to… |
|----|----------------------------|
| FR1-1 | Create an activity in the register containing: **logger name**, **title** (e.g. "Trip to Preikestolen"), **description** (longer text), **date and time the activity was registered** |
| FR1-2 | Print all activities in a structured, readable way, **sorted by date/time** (ascending or descending, PDF §5.1) |
| FR1-3 | Search for activities on a given **date** |
| FR1-4 | Delete activities |

Notes from PDF §5.1:
- FR1-3: the PDF lets you choose between the first match and **all** matches. Chosen: all.
- The register should also offer other useful methods (chosen: `size()`, `isEmpty()`).

## Level 2 – Extended functionality (PDF §3.3.2, §5.2)

A separate **register of loggers** (`Person`). The activity refers to its logger
by object reference, not by name text. Groups and sharing are supported.

| ID | The user shall be able to… |
|----|----------------------------|
| FR2-1 | Choose among previous loggers when creating an activity, or add a new logger (who is then stored in the logger register) |
| FR2-2 | Create and maintain **groups** of loggers (e.g. "Fjellgjengen", "Sykkelklubben") |
| FR2-2a | A group has a **name** |
| FR2-2b | A group contains **one or more** loggers |
| FR2-2c | Loggers can be **added to and removed from** a group |
| FR2-3 | Share an activity with **one or more selected loggers**, **or** with a **group** (all members get access) |
| FR2-4 | Mark an activity as **private** (visible only to its logger) or **shared** |
| FR2-5 | Show the activities that are shared with a given logger |
| FR2-6 | Search for activities in a **period** (from date, to date) and show them |
| FR2-7 | Search for activities by a given **logger** and show them |
| FR2-8 | Search for activities by a **word in title or description** and show them |
| FR2-9 | Show **statistics**: number of activities per logger (e.g. a two-column table: logger, count) |

**Clarification on sharing:** sharing only means the activity becomes visible to
other loggers *inside the same local application*. No networking, file
transfer, e-mail or similar shall be implemented.

### Interpretation of Level 2

Where the PDF leaves room for interpretation, the reference solution's choices
are recorded in [04-design.md → Design decisions](04-design.md#design-decisions)
(D1–D5): group sharing as a live reference, private/shared derived from the
share lists, empty groups allowed, loggers unique by name. Students may choose
otherwise if they justify it in the report.

## Own extensions (PDF §3.3.3, §6) – optional, counts positively

| ID | Suggested extension |
|----|---------------------|
| EXT-1 | Activity type (mountain hike, bike ride, city walk, …) |
| EXT-2 | Duration and/or distance |
| EXT-3 | Rating 1–5 |
| EXT-4 | Location/place |
| EXT-5 | Unshare an activity (make it private again) |
| EXT-6 | Statistics: number of shared vs. private activities |

Part 3 also invites own ideas beyond these, as long as the base requirements
are still met.

**Chosen for the reference solution (Part 3):** EXT-1 (type), EXT-2 (distance
only, no duration), EXT-5 (stop sharing with a single logger or group; making an
activity fully private is already part of Level 2) and EXT-6. EXT-3 (rating)
and EXT-4 (location) are left for students.
