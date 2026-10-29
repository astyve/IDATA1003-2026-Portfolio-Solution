# Activity Logger – reference solution

Reference solution for the IDATx1003 Programmering 1 portfolio assignment
"Tur- og aktivitetsregister" (autumn 2026). A text-based Java application for
logging trips and activities, built in three parts.

## Source of truth

- `docs/spec/` is the source of truth. The code is generated from it and must
  match it. If code and spec disagree, the code is wrong.
- Start with `docs/spec/README.md`. The most specific files are
  `09-api.md` (signatures and exceptions), `10-ui.md` (exact console text) and
  `11-demo-data.md` (demo data and expected results).
- `docs/requirements.pdf` is the original assignment (Norwegian) and overrides the spec.
- **Do not edit `docs/spec/` while building.** If the spec is unclear,
  incomplete or contradictory, choose the simplest reading that satisfies the
  PDF, and report it as a spec gap at the end so the spec can be fixed.
- Do not add public classes, methods or behaviour beyond the spec.

## Commands

```bash
mvn clean verify                                  # compile, test, CheckStyle, coverage gate: "done" = this passes
mvn -q test -Dtest=ActivityTest                   # run one test class
java -jar target/activity-logger-<version>.jar    # run the application
printf '0\n' | java -jar target/activity-logger-<version>.jar   # Part 3 smoke test
```

Coverage report: `target/site/jacoco/index.html` (CSV: `target/site/jacoco/jacoco.csv`).

## Build gates (in `pom.xml`, never weaken them)

- CheckStyle with `google_checks.xml` on main **and** test code; any warning fails the build.
- JaCoCo: line coverage of `edu.ntnu.bidata.model` and `edu.ntnu.bidata.logic` ≥ 90 %.
- Never lower thresholds, add suppressions, skip tests or delete tests to make the build pass.

## Code conventions

- Java 25, Maven, Google Java Style: 2-space indent, 100-column lines, no wildcard imports,
  imports in one ASCII-sorted block, braces always.
- Packages: `edu.ntnu.bidata` (`Main`), `.model`, `.logic`, `.ui`. Layering
  `ui` → `logic` → `model`; model and logic never use `System.in`/`System.out`.
- All names, comments and JavaDoc in English.
- JavaDoc on every class and **every** public constructor and method, including
  getters and setters, with `@param`, `@return` and `@throws` where they apply.
  The `Activity` class JavaDoc covers role, fields with chosen types and why,
  final vs. mutable fields, and the invalid-data strategy (PDF §4.1).
- Course syllabus only: no inheritance hierarchies (enums are fine), no custom
  exception classes, no external libraries other than JUnit, no records, no
  `var`, no GUI, no files or persistence.
- Exceptions exactly as in `docs/spec/04-design.md` → Exception strategy:
  `Objects.requireNonNull` for `null` (NPE), `IllegalArgumentException` for
  invalid values, `IllegalStateException` for duplicates,
  `NoSuchElementException` for removing something absent. Messages are full
  sentences, since the UI shows them to the user.
- Use streams and lambdas for searching, sorting and counting in registers.
- Return unmodifiable, sorted copies of collections, never internal state.

## Test conventions

- JUnit Jupiter 6 (`org.junit.jupiter.api.*`). One test class per model and
  logic class, same package, named `<Class>Test`. No tests for `ui` classes.
- The test class JavaDoc lists the planned positive and negative tests first (PDF §5.1.1).
- Every public method gets positive tests and, for each exception in
  `09-api.md`, a negative test using `assertThrows` with the exact exception type.
- Arrange-Act-Assert, with `// Arrange`, `// Act`, `// Assert` comments
  (`// Act & Assert` for `assertThrows`).
- Descriptive camelCase names, e.g. `constructorThrowsWhenTitleIsBlank`.
  Each test method has a one-line JavaDoc stating what is tested and the expected result.
- Use fixed `LocalDateTime` values; only "in the future" tests use `LocalDateTime.now()`.
- Tests create their own data and never depend on the demo data.

## Git

- One commit per part, message `Part <n>: <summary>`, ending with the
  co-author line. Builds happen on branches named `build/<yyyyMMdd-HHmm>`.
- Never push, add remotes or create tags unless the user asks.
