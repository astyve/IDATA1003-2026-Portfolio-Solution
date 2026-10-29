# 09 – API

The exact public API of every class, per part. Generated code must match these
signatures, rules and exceptions. Private helpers may be added freely; public
members may not be added without updating this file.

Notation:
- **NPE** `NullPointerException`, **IAE** `IllegalArgumentException`,
  **ISE** `IllegalStateException`, **NSEE** `java.util.NoSuchElementException`.
- "blank" means `null`-safe check `String.isBlank()` after the null check.
- Validation order for every method: null checks first (NPE), then value checks (IAE),
  then state checks (ISE / NSEE).
- String arguments that are stored are stored **trimmed**.
- Exception messages are full English sentences, e.g. `"Title cannot be blank."`,
  because the UI shows them to the user as `Error: <message>`.

## Versions

| Part | `pom.xml` version | `ActivityLoggerUi.VERSION` | Git tag (on request) |
|------|-------------------|----------------------------|----------------------|
| 1 | `0.1.0` | `"0.1.0"` | `v0.1.0` |
| 2 | `0.2.0` | `"0.2.0"` | `v0.2.0` |
| 3 | `1.0.0` | `"1.0.0"` | `v1.0.0` |

---

## `edu.ntnu.bidata.Main` (Part 1+)

```java
public static void main(String[] args)
```
Creates an `ActivityLoggerUi`, calls `init()`, then `start()`. Nothing else.
Final class with a private constructor is not required; keep it a plain class.

---

## `edu.ntnu.bidata.model.Activity`

### Part 1

```java
public Activity(String loggerName, String title, String description, LocalDateTime registeredAt)
public String getLoggerName()
public String getTitle()
public String getDescription()
public LocalDateTime getRegisteredAt()
public void setTitle(String title)
public void setDescription(String description)
```

| Rule | Exception |
|------|-----------|
| any argument `null` | NPE |
| `loggerName`, `title` or `description` blank | IAE |
| `registeredAt` after `LocalDateTime.now()` | IAE |
| setters: same rules as the constructor for that field | NPE / IAE |

### Part 2 (replaces Part 1 logger)

`loggerName`/`getLoggerName()` are removed and replaced by:

```java
public Activity(Person logger, String title, String description, LocalDateTime registeredAt)
public Person getLogger()

public void shareWith(Person person)
public void shareWith(Group group)
public void makePrivate()
public boolean isShared()
public boolean isSharedWith(Person person)
public boolean isVisibleTo(Person person)
public List<Person> getSharedWithPersons()
public List<Group> getSharedWithGroups()
```

| Method | Behaviour | Exceptions |
|--------|-----------|------------|
| `shareWith(Person)` | Adds to the person share set; no effect if already there | NPE; IAE if `person.equals(logger)` |
| `shareWith(Group)` | Adds to the group share set; no effect if already there | NPE |
| `makePrivate()` | Clears both share sets | – |
| `isShared()` | `true` if either share set is non-empty (D2) | – |
| `isSharedWith(p)` | `true` if `p` is in the person set or a current member of a shared group (D1). The logger is never "shared with". | NPE |
| `isVisibleTo(p)` | `p.equals(logger) || isSharedWith(p)` | NPE |
| `getSharedWithPersons()` | Unmodifiable list sorted by name, ignoring case | – |
| `getSharedWithGroups()` | Unmodifiable list sorted by name, ignoring case | – |

### Part 3 (extensions EXT-1, EXT-2, EXT-5)

The constructor gains two parameters (the Part 2 constructor is removed):

```java
public Activity(Person logger, String title, String description, LocalDateTime registeredAt,
    ActivityType type, double distanceKm)
public ActivityType getType()
public void setType(ActivityType type)
public double getDistanceKm()
public void setDistanceKm(double distanceKm)
public void unshareWith(Person person)
public void unshareWith(Group group)
```

| Rule | Exception |
|------|-----------|
| `type` `null` | NPE |
| `distanceKm` negative, `NaN` or infinite | IAE |
| `unshareWith(...)` argument `null` | NPE |
| `unshareWith(...)` with a person/group not in the share set | NSEE |

---

## `edu.ntnu.bidata.model.Person` (Part 2+)

```java
public Person(String name)
public String getName()
public boolean equals(Object other)
public int hashCode()
public String toString()          // returns getName()
```

| Rule | Exception |
|------|-----------|
| `name` `null` | NPE |
| `name` blank | IAE |

`equals`/`hashCode`: compare `name.toLowerCase(Locale.ROOT)` (name is already trimmed).

---

## `edu.ntnu.bidata.model.Group` (Part 2+)

```java
public Group(String name)
public String getName()
public void addMember(Person person)
public void removeMember(Person person)
public boolean hasMember(Person person)
public List<Person> getMembers()     // unmodifiable, sorted by name ignoring case
public int getMemberCount()
```

| Method | Exceptions |
|--------|------------|
| constructor | NPE if `null`; IAE if blank |
| `addMember` | NPE; ISE if already a member |
| `removeMember` | NPE; NSEE if not a member |
| `hasMember` | NPE |

No `equals`/`hashCode` override (identity).

---

## `edu.ntnu.bidata.model.ActivityType` (Part 3)

```java
public enum ActivityType {
  MOUNTAIN_HIKE("Mountain hike"), BIKE_RIDE("Bike ride"), CITY_WALK("City walk"),
  SKI_TRIP("Ski trip"), OTHER("Other");
  public String getDisplayName()
}
```

---

## `edu.ntnu.bidata.logic.ActivityRegister` (Part 2+)

Storage: `private final List<Activity> activities = new ArrayList<>();` (D6).
All returned lists are new unmodifiable lists sorted by `registeredAt`, oldest first (D8).

### Part 2, Level 1

Built first against the Part 1 `Activity`, then kept when `Activity` switches to `Person`.

```java
public ActivityRegister()
public void addActivity(Activity activity)
public void removeActivity(Activity activity)
public List<Activity> getActivitiesSorted()
public List<Activity> findByDate(LocalDate date)
public int size()
public boolean isEmpty()
```

| Method | Behaviour | Exceptions |
|--------|-----------|------------|
| `addActivity` | Adds | NPE; ISE if the same object is already registered |
| `removeActivity` | Removes | NPE; NSEE if not registered |
| `getActivitiesSorted` | All activities | – |
| `findByDate` | Activities whose `registeredAt.toLocalDate()` equals `date`; empty list if none | NPE |

### Part 2, Level 2

```java
public List<Activity> findBetween(LocalDate from, LocalDate to)
public List<Activity> findByLogger(Person logger)
public List<Activity> findByKeyword(String keyword)
public List<Activity> findSharedWith(Person person)
public Map<Person, Long> countPerLogger()
```

| Method | Behaviour | Exceptions |
|--------|-----------|------------|
| `findBetween` | Date part within `[from, to]`, both inclusive | NPE; IAE if `from` is after `to` |
| `findByLogger` | `activity.getLogger().equals(logger)` | NPE |
| `findByKeyword` | Trimmed keyword contained in title or description, ignoring case (`Locale.ROOT`) | NPE; IAE if blank |
| `findSharedWith` | `activity.isSharedWith(person)` (own activities not included) | NPE |
| `countPerLogger` | Number of activities per logger; only loggers with ≥ 1 activity; a `LinkedHashMap` ordered by logger name, ignoring case; unmodifiable | – |

### Part 3

```java
public List<Activity> findByType(ActivityType type)
public long countShared()
public long countPrivate()
```

| Method | Behaviour | Exceptions |
|--------|-----------|------------|
| `findByType` | Activities of the given type | NPE |
| `countShared` / `countPrivate` | Number of activities where `isShared()` is true / false (EXT-6) | – |

---

## `edu.ntnu.bidata.logic.PersonRegister` (Part 2+)

Storage: `private final Map<String, Person> persons = new HashMap<>();` keyed by
`name.toLowerCase(Locale.ROOT)` (D7).

```java
public PersonRegister()
public void addPerson(Person person)
public Optional<Person> findByName(String name)
public Person getOrCreate(String name)
public List<Person> getAllPersons()   // unmodifiable, sorted by name ignoring case
public int size()
```

| Method | Behaviour | Exceptions |
|--------|-----------|------------|
| `addPerson` | Registers | NPE; ISE if a person with the same name exists |
| `findByName` | Trimmed, case-insensitive lookup; `Optional.empty()` if none (also for blank) | NPE |
| `getOrCreate` | Existing person, or a new registered `Person(name)` | NPE; IAE if blank |

---

## `edu.ntnu.bidata.logic.GroupRegister` (Part 2+)

Storage: `private final Map<String, Group> groups = new HashMap<>();` keyed by
`name.toLowerCase(Locale.ROOT)` (D7).

```java
public GroupRegister()
public void addGroup(Group group)
public Optional<Group> findByName(String name)
public List<Group> getAllGroups()     // unmodifiable, sorted by name ignoring case
public int size()
```

| Method | Behaviour | Exceptions |
|--------|-----------|------------|
| `addGroup` | Registers | NPE; ISE if a group with the same name exists |
| `findByName` | Trimmed, case-insensitive lookup; `Optional.empty()` if none | NPE |

---

## `edu.ntnu.bidata.ui.ActivityLoggerUi`

```java
public static final String VERSION = "<see Versions>";
public ActivityLoggerUi()
public void init()
public void start()
```

| Part | `init()` | `start()` |
|------|----------|-----------|
| 1 | Creates the demo `Activity` objects (kept in a private `List<Activity>` field) | Prints them as described in [10-ui.md](10-ui.md) |
| 2 | Creates the three registers and fills them with demo data | Runs the scripted demo in [10-ui.md](10-ui.md) |
| 3 | Creates registers, `InputReader` (on `System.in`), `ActivityFormatter`, and loads demo data | Runs the menu loop until the user exits |

In Parts 1–2, the printing of one activity is a private method in this class.
In Part 3 it moves to `ActivityFormatter` (refactoring for cohesion; document in the report).

## `edu.ntnu.bidata.ui.ActivityFormatter` (Part 3)

```java
public ActivityFormatter()
public String formatSummary(int number, Activity activity)
public String formatDetails(Activity activity)
public String formatDateTime(LocalDateTime dateTime)
```
Formats exactly as in [10-ui.md](10-ui.md). Pure functions of their input (no
printing), so they *may* be unit tested; tests are optional (UI layer). NPE on `null`.

## `edu.ntnu.bidata.ui.InputReader` (Part 3)

```java
public InputReader(Scanner scanner)
public String readText(String prompt)
public int readInt(String prompt, int min, int max)
public double readDouble(String prompt, double min)
public LocalDate readDate(String prompt)
public boolean readYesNo(String prompt)
```

Each method prints the prompt, reads one line and repeats until the input is
valid, printing the messages from [10-ui.md](10-ui.md). `readText` rejects blank input.
If the input stream ends, `Scanner.nextLine()` throws `NoSuchElementException`;
`InputReader` lets it propagate. Whatever the menu was doing, the application
must then print `Goodbye!` and end normally: no stack trace, no endless loop.
This is what makes `printf '0\n' | java -jar …` and piped test scripts work.
