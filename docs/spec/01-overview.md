# 01 – Overview

## Purpose

A text-based **trip and activity register** ("tur- og aktivitetsregister") where
users log trips and activities, e.g. mountain hikes, bike rides or city walks.
The register may be themed (the PDF uses a *mountain hike register* as its
running example); the theme is a free choice.

## Key characteristics

- Stand-alone desktop application on **one** computer. No network.
- Because there is no login/network, **the name of the person logging the
  activity is entered per activity**. Different activities may have different loggers.
- Several people may use the same application locally.
- Level 2: an activity can be **shared** with selected loggers or a group.
  Sharing is purely *in-application visibility*: no network, files, e-mail etc.

## Constraints (PDF §3.4)

- Text-based UI only (no GUI; no extra credit for GUI).
- Must run from both the IDE and the command line.
- Maven is the build system and the project **must compile with Maven**.
- Single machine, no networking; sharing is local only.

## Deliveries

- Code project in GitHub (and as a ZIP in Inspera).
- Report, 2500–3000 words, Norwegian or English, from the Canvas template.
  Weighting: project 70 %, report 30 %.
- AI declaration form + AI log appendix in the report.

## Timeline

| Part | Approx. week | Focus |
|------|--------------|-------|
| Part 1 | 40 | Entity class(es), version control, unit testing |
| Part 2 | 43 | Collections/registers and business logic (Level 1 + Level 2) |
| Part 3 | 45 | Text UI, complete tests, own extensions, refactoring |
| Feedback | 47 | Last feedback session |
| Delivery | 50 | Final hand-in |

## Glossary (Norwegian → English / code)

| Norwegian | English | Suggested code name |
|-----------|---------|---------------------|
| aktivitet / tur | activity / trip | `Activity` |
| loggfører | logger (person who logs) | `Person` |
| register | register | `ActivityRegister`, `PersonRegister`, `GroupRegister` |
| gruppe | group | `Group` |
| deling / delt | sharing / shared | `share…`, `isShared()` |
| privat | private | `makePrivate()`, `!isShared()` |
| brukergrensesnitt | user interface | `…Ui` |
| enhetstest | unit test | `…Test` |
| unntak | exception | – |
| veiledningsmøte | supervision / feedback meeting | – |
| KI | AI | – |
