# Activity Logger – Specification

English working specification derived from `docs/requirements.pdf`
("Mappeoppgave: Tur- og aktivitetsregister", IDATx1003 Programmering 1, autumn 2026).
The PDF is the authoritative source; these files restate it in a form that is
easier to implement against and add a proposed design.

## Files

| File | Content |
|------|---------|
| [01-overview.md](01-overview.md) | What the application is, scope, constraints, glossary (NO → EN) |
| [02-functional-requirements.md](02-functional-requirements.md) | Functional requirements, Level 1, Level 2 and own extensions, with IDs |
| [03-non-functional-requirements.md](03-non-functional-requirements.md) | Code quality, testing, exceptions, Git, Maven/library versions |
| [04-design.md](04-design.md) | Design: packages, classes, decisions D1–D8, exception strategy |
| [05-part1-entity.md](05-part1-entity.md) | Part 1: project setup, `Activity` entity, UI skeleton, tests |
| [06-part2-registers.md](06-part2-registers.md) | Part 2: registers (Level 1) and persons, groups, sharing (Level 2) |
| [07-part3-ui-and-finish.md](07-part3-ui-and-finish.md) | Part 3: text UI, extensions, refactoring, release |
| [08-checklist.md](08-checklist.md) | Final checklist before delivery |
| [09-api.md](09-api.md) | Exact public API per class and part, with exceptions |
| [10-ui.md](10-ui.md) | Exact console output, menus, prompts and messages |
| [11-demo-data.md](11-demo-data.md) | Demo loggers, groups and activities, with expected results |

## Requirement IDs

- `FR1-x`: functional requirement, Level 1 (MVP)
- `FR2-x`: functional requirement, Level 2
- `EXT-x`: optional own extension
- `NFR-x`: non-functional requirement

## Precedence

When files disagree: `09-api.md`, `10-ui.md` and `11-demo-data.md` are the most
specific and win over `04-design.md`, which wins over the part files (05–07).
The PDF wins over everything. A disagreement is a spec bug: fix the files.

## Building

The solution is generated from these files with the `/build-from-spec` skill
(see `.claude/skills/build-from-spec/SKILL.md`). Edit the spec, then rebuild.

## Order of work

Part 1 → Part 2 → Part 3. Each part builds on the previous one. Read all three
part files before writing code, as the PDF itself insists.
