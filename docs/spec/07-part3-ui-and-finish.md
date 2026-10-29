# 07 – Part 3: User Interface, Extensions and Finishing

Source: PDF §6. Approx. week 45, feedback week 47, delivery week 50.

## Refactor first
- [ ] Apply the feedback from Part 2 and refactor without introducing new bugs
      (the test suite must stay green throughout).
- [ ] Planned refactoring for the reference solution: move activity printing
      from `ActivityLoggerUi` into a new `ActivityFormatter`, and input handling
      into a new `InputReader` (cohesion). Set version `1.0.0`.
- [ ] Document original vs. refactored classes/functions **and why** in the report.

## Text user interface
- [ ] A numbered menu, exactly as in `10-ui.md` → Part 3 (chosen over zuul-style
      commands, see `04-design.md`).
- [ ] Prioritise usability and robustness over looks. Invalid input must never crash.
- [ ] Cover all functional requirements:
  - Activities: register (FR1-1/FR2-1), list sorted (FR1-2), show details, delete (FR1-4)
  - Search: by date (FR1-3), period (FR2-6), logger (FR2-7), keyword (FR2-8), type (EXT-1)
  - Loggers: list, add
  - Groups: create, list, add/remove members (FR2-2)
  - Sharing: share with persons/group (FR2-3), set private/shared (FR2-4),
    stop sharing with one logger/group (EXT-5), show activities shared with a person (FR2-5)
  - Statistics: activities per logger (FR2-9), shared vs. private (EXT-6)
- [ ] Separate input handling (`InputReader`) from menu logic for cohesion.
- [ ] No unit tests required for UI classes.

## Own extensions
- [ ] Implement EXT-1, EXT-2, EXT-5 and EXT-6 as specified in `09-api.md`;
      add A5 to the demo data. Tests for all new model/logic code.
- [ ] Justify in the report why they make the application more useful.

## Design review (report material)
- Coupling and cohesion: is responsibility well distributed between activity,
  logger, group, registers and UI? Should more classes or delegated methods be introduced?
- Layered architecture: how is UI separated from business logic?
- Responsibility-driven design and modularisation.

## Code requirements (still apply)
- Google style, CheckStyle with no or minimal violations.
- English names and English JavaDoc everywhere.
- Maven project that compiles and tests with `mvn clean verify`.

## Git and release
- [ ] Frequent commits and pushes; branches where useful.
- [ ] Tag releases (e.g. `v1.0.0`) consistent with `pom.xml` version and the version shown in the app.
- [ ] Push everything to GitHub; produce a ZIP for Inspera.

## Report (final)
Structure: Introduction → Theory → Method → Results (UML: use case, class,
sequence) → Discussion → Conclusion (max ½ page). 2500–3000 words.
Keep a red thread: every theory must be referenced in Results/Discussion.
Appendices: AI log, AI declaration.
