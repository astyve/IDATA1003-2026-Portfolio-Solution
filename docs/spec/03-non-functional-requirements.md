# 03 – Non-Functional Requirements

Source: PDF §3.4–3.8, §6.2, §7.

## Code quality

| ID | Requirement |
|----|-------------|
| NFR-1 | Follow **Google Java Style**; verify with **CheckStyle (Google rules)**, minimal/no violations at delivery. SonarQube/SonarLint recommended. |
| NFR-2 | All class, method and variable names in **English**, descriptive. |
| NFR-3 | All code documented in **English JavaDoc**. **Every public method** documented, including trivial getters/setters. |
| NFR-4 | Logical **package structure**, low coupling, high cohesion, responsibility-driven design, layered architecture (UI separated from business logic). |
| NFR-5 | Use **lambdas and streams** where natural. |
| NFR-6 | Stay within the course syllabus (no GUI, no custom exception classes, avoid unnecessary inheritance). |

## Entity class documentation (PDF §4.1)

The `Activity` class JavaDoc must describe:
- role/responsibility
- the information held, the chosen data type for each field **and why**
- which fields are set only at construction vs. which can change afterwards
- how the class responds to invalid data (strategy: throw exceptions vs. dummy values)

## Unit testing

| ID | Requirement |
|----|-------------|
| NFR-7 | JUnit tests for business-critical code: entity classes and registers. Not for UI classes (unless methods are testable without a user). |
| NFR-8 | Both **positive and negative** tests. |
| NFR-9 | Descriptive test names; tests documented (what is tested, expected result). Plan tests in the test class JavaDoc **before** writing them. |
| NFR-10 | Follow **Arrange-Act-Assert**. |
| NFR-11 | Reasonable coverage (check it, but 100 % is not the goal in itself). |
| NFR-12 | `mvn test` runs with all tests passing. |

## Exception handling (PDF §3.8.3)

| ID | Requirement |
|----|-------------|
| NFR-13 | Handle unwanted events balanced: not too much, not too little. |
| NFR-14 | Validate method parameters. |
| NFR-15 | Use **existing** JDK exception classes and choose sensibly among them. **Do not use `IllegalArgumentException` everywhere.** No custom exception classes. |
| NFR-16 | The UI must be robust: invalid input must never crash the application. |

## Version control

| ID | Requirement |
|----|-------------|
| NFR-17 | Local Git repository connected to a central repo on GitHub. |
| NFR-18 | Frequent commits with short, concise, descriptive messages; push regularly. |
| NFR-19 | Use branches where appropriate. |
| NFR-20 | Use **tags** for releases, consistent with the version in `pom.xml` and in the application. |

## Build and versions (PDF §3.8.5, Code listing 1)

| Item | Version |
|------|---------|
| Java | 25 (LTS) – `maven.compiler.release=25` |
| Maven | 3.9.16 |
| `maven-compiler-plugin` | 3.15.0 |
| `maven-surefire-plugin` | 3.6.0 |
| `org.junit.jupiter:junit-jupiter` | 6.1.3, scope `test` |

Always pin exact versions; never `RELEASE` or `LATEST`.

In addition, `pom.xml` enforces the quality rules during `mvn verify`:

| Plugin | Version | Effect |
|--------|---------|--------|
| `maven-checkstyle-plugin` + `checkstyle` | 3.6.0 + 14.3.0 | Google rules (`google_checks.xml`) on main and test code; **any** violation fails the build |
| `jacoco-maven-plugin` | 0.8.16 | Coverage report in `target/site/jacoco/`; build fails if line coverage of `model` or `logic` is below 90 % |
| `maven-jar-plugin` | 3.4.2 | Executable jar (`Main-Class: edu.ntnu.bidata.Main`) |

"Done" for any part means `mvn clean verify` passes.

## Documentation artefacts in the repo (PDF Appendix A.1.1)

- PlantUML sources: `src/doc/plantuml/*.puml`
- Generated diagram images: `docs/uml/*.png`

## AI usage (PDF §3.5–3.7, §6.4)

- AI is allowed and expected, but as a tutor, reviewer or adversarial tester,
  not to generate the solution.
- Comment in the code where substantial AI help was received.
- The report needs an **AI log** (phase, role, prompt, key suggestion, accepted?,
  reason), with at least one rejected/corrected suggestion per phase, plus an
  AI declaration.
