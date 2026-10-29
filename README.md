# Activity Logger

A text-based application for logging trips and activities, such as mountain hikes,
bike rides and city walks. Reference solution for the portfolio assignment
"Tur- og aktivitetsregister" in IDATx1003 Programmering 1, NTNU, autumn 2026.

Several people can use the application on the same computer. Each activity has a
logger (the person who logged it) and can be private or shared with selected
loggers or with a group. There is no network, login or file storage: all data
lives in memory while the program runs, and demo data is loaded at start-up.

## Features

- **Activities:** register, list (sorted by date and time), show details, delete
- **Search:** by date, by period, by logger, by keyword in title or description, by type
- **Loggers:** list and add; choose an existing logger or add a new one when registering
- **Groups:** create, list, add and remove members
- **Sharing:** share with loggers or a group, stop sharing with one logger or group,
  make an activity private, show the activities shared with a logger
- **Statistics:** activities per logger, shared vs. private activities
- **Extensions:** activity type and distance in km

## Requirements

- Java 25
- Maven 3.9 or newer

## Build and run

```bash
mvn clean verify                          # compile, test, CheckStyle and coverage check
java -jar target/activity-logger-1.0.0.jar
```

The application can also be started from the IDE by running `edu.ntnu.bidata.Main`.

Choose from the numbered menus by typing a number and pressing Enter; `0` goes back
or exits. Dates are entered as `yyyy-MM-dd`. Invalid input is rejected with a
message, and the application never stops because of it.

## Project structure

```
src/main/java/edu/ntnu/bidata
├── Main.java                 Entry point
├── model                     Entities (no input/output)
│   ├── Activity.java
│   ├── ActivityType.java
│   ├── Group.java
│   └── Person.java
├── logic                     Registers and business logic (no input/output)
│   ├── ActivityRegister.java
│   ├── GroupRegister.java
│   └── PersonRegister.java
└── ui                        Text user interface
    ├── ActivityFormatter.java
    ├── ActivityLoggerUi.java
    └── InputReader.java
```

The layers depend on each other in one direction only: `ui` → `logic` → `model`.
Unit tests for the `model` and `logic` classes are in `src/test/java`.

## Quality checks

`mvn clean verify` fails if any of these fail:

- **Tests:** JUnit 6, with positive and negative tests for every model and logic class
- **Code style:** CheckStyle with Google Java Style on main and test code; any warning fails the build
- **Coverage:** JaCoCo line coverage of `model` and `logic` must be at least 90 %
  (report in `target/site/jacoco/index.html`)

## Versions

| Tag | Part | Content |
|-----|------|---------|
| `v0.1.0` | Part 1 | `Activity` entity, unit tests, UI skeleton |
| `v0.2.0` | Part 2 | Registers, loggers, groups and sharing, scripted demo |
| `v1.0.0` | Part 3 | Menu-based user interface, extensions, refactoring |

## Specification

The code is built from the specification in [`docs/spec/`](docs/spec/README.md),
which is based on the original assignment in [`docs/requirements.pdf`](docs/requirements.pdf).
