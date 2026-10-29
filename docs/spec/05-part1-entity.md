# 05 – Part 1: Project Setup and Entity Class

Source: PDF §4. Approx. week 40.

## Goals

- Understand the assignment.
- Maven project (done: `edu.ntnu.bidata:activity-logger`, Java 25).
- Version control: local Git + GitHub.
- Entity class `Activity` with a test class `ActivityTest`.
- UI class skeleton with `init()` and `start()`.
- Application class with `main`.
- Start on the report.

## Tasks

### Project setup
- [x] `pom.xml` matches PDF Code listing 1, plus CheckStyle and JaCoCo (see `03`).
- [ ] Git repository; GitHub remote is added by the user (not by the build).
- [ ] Set version `0.1.0` in `pom.xml` and `ActivityLoggerUi.VERSION`.

### `Activity` entity (`model` package)
- [ ] Exactly the Part 1 API in `09-api.md`: logger name (`String`), title,
      description, `registeredAt` (`LocalDateTime`). No id.
- [ ] Logger name and `registeredAt` are final; title and description have setters.
- [ ] Validate in constructor and setters (see exception strategy in `04-design.md`).
- [ ] Thorough class JavaDoc covering role, fields + types + reasons,
      immutable vs. mutable, and invalid-data strategy.
- [ ] JavaDoc on all public methods.

### `ActivityTest`
- [ ] Plan tests in the class JavaDoc first.
- [ ] Positive: valid construction, getters return values, setters change values.
- [ ] Negative: null/blank logger, title, description; null `registeredAt`;
      invalid values in setters, each asserting the specific exception type.
- [ ] Arrange-Act-Assert, descriptive names (`@DisplayName` optional).

### UI class (`ui` package)
- [ ] `ActivityLoggerUi` with `init()` and `start()`.
- [ ] `init()`: create activities A1–A4 from `11-demo-data.md`.
- [ ] `start()`: print them exactly as in `10-ui.md` → Part 1.
- [ ] Separate private method for printing the details of one activity.

### Application class
- [ ] `Main.main(String[] args)`: create the UI object, call `init()` then `start()`.

### Quality
- [ ] Run CheckStyle (Google) and fix violations.
- [ ] `mvn clean verify` passes (tests, CheckStyle, coverage).
- [ ] Commit regularly with good messages; push.

## Report (Part 1)
- Front page, introduction.
- Use case diagram.
- Start theory and method chapters.
- Results: short description of planned classes + class diagram.
- Start the AI log.

## Delivery to the feedback meeting
- Code with implemented classes per Part 1.
- Unit tests (positive and negative) for all classes except UI.
- Report with at least: introduction, requirements specification, AI log started.
