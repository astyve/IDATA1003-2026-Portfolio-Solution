---
name: build-from-spec
description: Generate (or regenerate) the Activity Logger solution from docs/spec on a fresh git branch, part by part, until mvn clean verify passes. Use when the user runs /build-from-spec or asks to rebuild the solution from the spec.
argument-hint: "[all | part1 | part2 | part3]"
disable-model-invocation: true
---

# Build the solution from the spec

Argument: `$ARGUMENTS` (empty means `all`).

- `all` / `part1`: build from scratch, starting at `main`. `all` builds Parts 1, 2 and 3 in order.
- `part2` / `part3`: rebuild only that part, on top of the most recent build of the previous part.

The spec in `docs/spec/` and the rules in `CLAUDE.md` decide everything. Your job
is to turn them into code that passes `mvn clean verify`, not to make design choices.

## 0. Preconditions

1. If the project is not a git repository: `git init -b main`, `git add -A`,
   commit `Baseline: specification and build configuration`.
2. Switch to `main` if not on it. If `git status --short` shows changes:
   - Changes only under `docs/`, `CLAUDE.md`, `pom.xml`, `.claude/` or `.gitignore`:
     show the list and ask the user whether to commit them to `main` as
     `Update specification`. The build must start from a committed spec. Stop if they decline.
   - Changes under `src/`: stop and ask; `main` holds only the skeleton.
3. Note the start time as `<stamp>` = `date +%Y%m%d-%H%M`.

## 1. Create the build branch

- `all` / `part1`: `git switch -c build/<stamp> main`.
- `partN` (N = 2 or 3):
  1. Find the newest branch under `build/` whose history has a commit whose
     message starts with `Part <N-1>:` (`git for-each-ref --sort=-committerdate
     --format='%(refname:short)' refs/heads/build/`, then
     `git log <branch> --grep='^Part <N-1>:' --format=%H -1`).
     If none, stop and tell the user to run `/build-from-spec all` first.
  2. `git switch -c build/<stamp> <that commit>`.
  3. Bring in the current spec and build files from `main`:
     `git checkout main -- docs CLAUDE.md pom.xml .claude .gitignore`.

## 2. Build each part

For each part to build, in order:

### a. Read
`CLAUDE.md`, then `docs/spec/README.md`, `01`–`04`, `09-api.md`, `10-ui.md`,
`11-demo-data.md` and the part file (`05` for Part 1, `06` for Part 2, `07` for Part 3).
Read `docs/requirements.pdf` only to settle something the spec leaves unclear.
Keep a running list of **spec gaps**: anything unclear, missing or contradictory,
and the reading you chose.

### b. Implement
- Exactly the public API listed for this part and earlier parts in `09-api.md`,
  including replacements (e.g. Part 2 replaces `loggerName` with `Person`;
  Part 3 replaces the `Activity` constructor and moves printing into `ActivityFormatter`).
- Console output and texts exactly as in `10-ui.md`; demo data exactly as in `11-demo-data.md`.
- Version: set `<version>` in `pom.xml` and `ActivityLoggerUi.VERSION` as in
  `09-api.md` → Versions (no `-SNAPSHOT`).
- Follow every convention in `CLAUDE.md` (style, JavaDoc, exceptions, syllabus limits).

### c. Test
Write or update `<Class>Test` for every model and logic class: positive tests
for each public method, and one negative test per exception listed in
`09-api.md`, following the test conventions in `CLAUDE.md`.
When an API changes between parts, update the existing tests to match;
do not leave tests for removed members.

### d. Verify
Run `mvn -q clean verify`. On failure, read the output (CheckStyle lines,
surefire reports in `target/surefire-reports/`, coverage in
`target/site/jacoco/jacoco.csv`), fix the **code or tests**, and run again.

Never change `pom.xml` gates or thresholds, add suppressions, `@Disabled`
tests, or delete tests to get a green build, and never edit `docs/spec/`.
If it is still failing after 5 rounds, stop and report what fails and why.

### e. Run the application
Use the jar: `java -jar target/activity-logger-<version>.jar`.

- Parts 1–2: compare the output with `10-ui.md` and the expected facts in
  `11-demo-data.md` (order, search results, counts). Fix any difference.
- Part 3: pipe scripted input and check there are no stack traces, the expected
  results appear, and the program ends with `Goodbye!`:
  - `printf '1\n1\n0\n6\n1\n2\n0\n0\n'` (list activities, both statistics, exit)
  - `printf '2\n4\nridge\n0\n0\n'` (keyword search finds Romsdalseggen ridge)
  - `printf 'abc\n9\n\n0\n'` (invalid input is rejected with the `10-ui.md` messages)
  - `printf '1\n3\n'` (input ends mid-action: must print `Goodbye!` and exit)

### f. Self-review
Before committing, check and fix:
- Every requirement for this part (FR1-x, FR2-x, EXT-x listed in the part file)
  has an implementing method **and** a test (UI-only requirements: verified in step e).
- Every public class, constructor and method has JavaDoc.
- `grep -rn "System\.\(out\|in\)" src/main/java/edu/ntnu/bidata/model src/main/java/edu/ntnu/bidata/logic` finds nothing.
- No public members beyond `09-api.md`.

### g. Commit
`git add -A` and commit with the message `Part <n>: <one-line summary>`, a
short body listing the main classes added or changed, and the co-author line
from the session's attribution instructions.

## 3. Report

Finish with a short report to the user:

- Branch name and the commits made.
- Per part: number of tests (sum of `Tests run` in `target/surefire-reports/*.txt`)
  and line coverage of `model` and `logic` (from `jacoco.csv`).
- Smoke-run result for each part.
- **Spec gaps**: each unclear point and the reading chosen, with a suggested
  wording for the spec file. This is how the spec gets better between builds.
- How to compare with an earlier build: `git diff build/<old> build/<new> -- src`.

Do not tag, push or merge. If the user then asks for tags, use the versions in
`09-api.md` (`v0.1.0`, `v0.2.0`, `v1.0.0`); if those tags already exist, ask
before moving them.
