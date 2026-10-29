# 06 – Part 2: Registers, Loggers, Groups and Sharing

Source: PDF §5. Approx. week 43.

## Level 1 – Activity register (PDF §5.1)

### `ActivityRegister` (`logic` package)
- [ ] `ActivityRegister` backed by an `ArrayList` (D6); justify the choice in the report.
- [ ] `addActivity` (FR1-1)
- [ ] `findByDate(LocalDate)` returning **all** activities on that date (FR1-3)
- [ ] `removeActivity` (FR1-4)
- [ ] Access to all activities **sorted by date/time** (FR1-2), without exposing
      the internal collection. Describe the sorting solution in the report:
      how it was found and what the code does in your own words.
- [ ] Extra methods `size()` and `isEmpty()` (see `09-api.md`).

### `ActivityRegisterTest`
- [ ] Plan tests in the class JavaDoc first.
- [ ] Positive: add, find by date (0, 1, many hits), remove, sorted order.
- [ ] Negative: add null, add duplicate, remove non-existing, search with null date.
- [ ] Check coverage.

### Update UI
- [ ] `init()`: create the register and add A1–A4.
- [ ] `start()`: exercise the register and print an overview with a separate
      print method. **No interactive menu yet.** (Superseded by Level 2 below.)

## Level 2 – Loggers, groups and sharing (PDF §5.2)

### New classes
- [ ] `Person` (logger). `Activity` now refers to a `Person` object instead of a `String`.
- [ ] `PersonRegister`, with get-or-create when an activity has a new logger (FR2-1).
- [ ] `Group` with name and members; add/remove members (FR2-2).
- [ ] `GroupRegister`.
- [ ] Sharing on `Activity`: share with persons or a group (FR2-3),
      private/shared (FR2-4), visibility check. Follow D1–D5 in `04-design.md`.

### `ActivityRegister` additions
- [ ] Activities shared with a given person (FR2-5)
- [ ] Search in a period from–to (FR2-6)
- [ ] Search by logger (FR2-7)
- [ ] Search by keyword in title/description (FR2-8)
- [ ] Count per logger (FR2-9)

### Tests
- [ ] `PersonTest`, `GroupTest`, `PersonRegisterTest`, `GroupRegisterTest`,
      updated `ActivityTest` and `ActivityRegisterTest` (sharing, visibility, new searches).
- [ ] Positive and negative tests; plan in JavaDoc first; check coverage.

### Update UI
- [ ] Set version `0.2.0` in `pom.xml` and `ActivityLoggerUi.VERSION`.
- [ ] `init()`: create all three registers and load loggers, groups and
      activities A1–A4 from `11-demo-data.md` (A2, A4 private; A1, A3 shared).
- [ ] `start()`: run the scripted demo in `10-ui.md` → Part 2, which shows the
      logger (`Person`) and the **sharing status**.
- [ ] Still no interactive menu.

## Report (Part 2)
- Justify the collection choices.
- Explain the sorted-list solution and its origin.
- Document how logger reference and sharing are modelled and why.
- Update the class diagram and the AI log.
