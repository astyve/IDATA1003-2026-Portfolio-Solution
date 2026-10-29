# 04 – Design

The design of the reference solution, built up gradually through Parts 1–3.
It stays within the course syllabus: no inheritance hierarchies, no custom
exceptions, no GUI, no persistence.

Exact class and method signatures are in [09-api.md](09-api.md), the user
interface in [10-ui.md](10-ui.md) and the demo data in [11-demo-data.md](11-demo-data.md).
This file explains the structure and the reasons behind the choices.

**Theme:** mountain hikes (*fjellturer*). From Part 3 an activity also has a
type, so other kinds of trips (bike rides, city walks, ski trips) can be logged too.

## Package structure (final, Part 3)

```
edu.ntnu.bidata
├── Main                         Application class: public static void main
├── model                        Entity classes (no I/O)
│   ├── Activity
│   ├── Person                   Part 2
│   ├── Group                    Part 2
│   └── ActivityType             Part 3, enum
├── logic                        Business logic / collections (no I/O)
│   ├── ActivityRegister         Part 2
│   ├── PersonRegister           Part 2
│   └── GroupRegister            Part 2
└── ui                           All user interaction (System.in / System.out)
    ├── ActivityLoggerUi         init(), start(); menu loop from Part 3
    ├── ActivityFormatter        Part 3, formats activities as text
    └── InputReader              Part 3, robust reading and parsing of input
```

Layering: `ui` → `logic` → `model`. Model and logic classes never print or
read from the console; they return values or throw exceptions, which the UI
catches and shows as messages.

## Model

### `Activity`

| Field | Type | Final | Part | Rationale |
|-------|------|-------|------|-----------|
| `loggerName` | `String` | yes | 1 only | Who logged it never changes |
| `logger` | `Person` | yes | 2+ | Replaces `loggerName`; object reference instead of text |
| `title` | `String` | no | 1+ | May be corrected |
| `description` | `String` | no | 1+ | May be corrected or extended |
| `registeredAt` | `LocalDateTime` | yes | 1+ | Date and time in one type, as the PDF recommends; set at creation |
| `sharedWithPersons` | `Set<Person>` (`HashSet`) | yes (contents change) | 2+ | No duplicates |
| `sharedWithGroups` | `Set<Group>` (`HashSet`) | yes (contents change) | 2+ | Live reference to the group (D1) |
| `type` | `ActivityType` | no | 3 | Extension EXT-1 |
| `distanceKm` | `double` | no | 3 | Extension EXT-2; kilometres with decimals |

- **No id field.** The UI shows numbered lists and works with the selected
  `Activity` object, so an id is not needed.
- **One constructor** taking `registeredAt` explicitly. The UI passes
  `LocalDateTime.now()` truncated to minutes. Tests and demo data use fixed
  times, so the class is deterministic and easy to test.
- `registeredAt` may not be in the future.
- **Printing is not the entity's job.** `Activity` has no `toString` used for
  display; printing belongs in the UI (`ActivityFormatter` from Part 3).

### `Person`

One field, `name` (`String`, final, stored trimmed). `equals`/`hashCode` use
the name trimmed and in lower case (`Locale.ROOT`), see D4. `toString()`
returns the name.

### `Group`

`name` (`String`, final) and `members` (`Set<Person>`, `HashSet`). Groups
are compared by identity (no `equals` override); `GroupRegister` makes sure
names are unique.

### `ActivityType` (Part 3)

An enum with a display name: `MOUNTAIN_HIKE("Mountain hike")`,
`BIKE_RIDE("Bike ride")`, `CITY_WALK("City walk")`, `SKI_TRIP("Ski trip")`,
`OTHER("Other")`.

## Design decisions

These are the choices made for the reference solution. Students may choose
differently, as long as the choice is justified in the report. Each decision
lists the main alternative, so it can serve as a discussion point.

### D1 – Sharing with a group is a live reference

`Activity` stores a reference to the `Group`, not a copy of its members.
Visibility is checked at read time: a person sees a group-shared activity if
they are a member **now**.

- *Why:* matches "all members of the group get access" (FR2-3); no copying,
  one source of truth; removing someone from a group also revokes their access.
- *Alternative:* snapshot – copy the members into `sharedWithPersons` at share
  time. Simpler visibility check, but group changes are not reflected.

### D2 – Private/shared is derived, not stored

An activity is **shared** if it is shared with at least one person or group,
otherwise **private**. `makePrivate()` clears both share sets; sharing with
anyone makes it shared. There is no separate visibility field or enum.

- *Why:* impossible to get inconsistent states such as "private but with a
  share list" or "shared with nobody". Fewer fields, fewer tests.
- *Alternative:* an explicit visibility enum, keeping share lists while private
  so sharing can be toggled back on. Needs rules for the inconsistent states.
- EXT-5 (unshare) is then partly built in; removing a *single* person or group
  from the share list is added in Part 3.

### D3 – Groups may be empty

A group is created with a name and no members. Members are added afterwards,
and removing the last member is allowed.

- *Why:* "one or more" (FR2-2b) is read as *capacity* (not limited to one), not
  as an invariant. Enforcing ≥ 1 member complicates both creation and removal
  for little gain; an empty group simply grants access to nobody.
- *Alternative:* require a first member in the constructor and refuse to
  remove the last one (`IllegalStateException`).

### D4 – Loggers are unique by name

Two loggers are the same if their names match after trimming, ignoring case.
`PersonRegister.getOrCreate(name)` returns the existing person or registers a
new one (FR2-1). The name is shown as first typed.

- *Why:* in a single local application, the name is what users type and
  recognise; it makes "choose an existing logger or add a new one" natural.
- *Alternative:* a numeric id per person, allowing two people with the same name.

### D5 – Further rules following from D1–D4

- An activity cannot be shared with its own logger (the logger always sees it):
  `IllegalArgumentException`.
- Sharing with the same person or group twice has no effect (sets).
- `isVisibleTo(p)` = `p` is the logger, **or** in `sharedWithPersons`, **or**
  a current member of any group in `sharedWithGroups`.
- Deleting loggers and deleting groups are not required by the specification
  and are left out: deleting either would leave references in activities or
  groups that would also need cleaning up.

### D6 – Activity register uses an `ArrayList`

`ActivityRegister` stores activities in an `ArrayList<Activity>`.

- *Why:* no natural unique key exists (no id, see above); a list keeps all
  activities, and sorting and searching are done with streams when asked for.
  Duplicates (the same object added twice) are refused explicitly.
- *Alternative:* `HashMap<Integer, Activity>` with a generated id, giving fast
  lookup by id at the cost of an extra field and id generation.

### D7 – Person and group registers use a `HashMap`

`PersonRegister` and `GroupRegister` use `HashMap<String, …>` keyed by the
name trimmed and in lower case (`Locale.ROOT`).

- *Why:* names are the natural key (D4); lookup by name is the main operation
  and a map makes uniqueness simple to enforce.

### D8 – All lists returned are sorted, unmodifiable copies

Every method returning a collection returns a new, unmodifiable `List`
(`Stream.toList()`), sorted as stated in [09-api.md](09-api.md). Activities are
sorted by `registeredAt`, oldest first; persons and groups by name, ignoring case.

- *Why:* callers (the UI) can never change a register's internal state, and
  output is always in a predictable order.

## Exception strategy (NFR-13–16)

Only standard JDK exceptions are used, chosen by the kind of error:

| Situation | Exception |
|-----------|-----------|
| `null` argument | `NullPointerException` via `Objects.requireNonNull(x, "<name> cannot be null")` |
| Invalid value: blank string, negative or non-finite distance, `registeredAt` in the future, `from` after `to`, sharing with the activity's own logger | `IllegalArgumentException` |
| Adding something that is already there: the same activity twice, a person or group whose name exists, a member already in the group | `IllegalStateException` |
| Removing something that is not there: an activity not in the register, a person not in the group, unsharing with someone not shared with | `java.util.NoSuchElementException` |
| Lookup by name with no match | No exception: returns `Optional.empty()` |
| Bad user input (not a number, wrong date format, out of range) | Caught in `InputReader` (`NumberFormatException`, `DateTimeParseException`); the user is asked again |

All exceptions from model and logic are caught in the UI and shown as
`Error: <message>`. The application never crashes on user input.

## UI

A numbered main menu with sub-menus; see [10-ui.md](10-ui.md). A menu was
chosen over zuul-style commands because it is easier to make robust and shows
the user what is possible at every step. Demo data is loaded in `init()`.

## Diagrams to produce (report)

- Use case diagram (Part 1)
- Class diagram (Part 1 draft, updated in Parts 2 and 3)
- Sequence diagram for "share activity with group" (Part 3)

PlantUML sources go in `src/doc/plantuml/`, generated images in `docs/uml/`.
