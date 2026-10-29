package edu.ntnu.bidata.ui;

import edu.ntnu.bidata.logic.ActivityRegister;
import edu.ntnu.bidata.logic.GroupRegister;
import edu.ntnu.bidata.logic.PersonRegister;
import edu.ntnu.bidata.model.Activity;
import edu.ntnu.bidata.model.ActivityType;
import edu.ntnu.bidata.model.Group;
import edu.ntnu.bidata.model.Person;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The text user interface of the Activity Logger.
 *
 * <p>Shows a numbered main menu with sub-menus and runs until the user exits or the input
 * ends. All reading of input is done by {@link InputReader} and all formatting of activities
 * by {@link ActivityFormatter}. Errors from the model and logic classes are shown as
 * {@code Error: <message>}, so invalid input never stops the application.
 */
public class ActivityLoggerUi {

  /** The version of the application. */
  public static final String VERSION = "1.0.0";

  private ActivityRegister activityRegister;
  private PersonRegister personRegister;
  private GroupRegister groupRegister;
  private InputReader input;
  private ActivityFormatter formatter;

  /**
   * Creates the user interface. Call {@link #init()} before {@link #start()}.
   */
  public ActivityLoggerUi() {
    // Everything is created in init().
  }

  /**
   * Initialises the user interface: creates the registers, the input reader and the
   * formatter, and loads the demo data.
   */
  public void init() {
    activityRegister = new ActivityRegister();
    personRegister = new PersonRegister();
    groupRegister = new GroupRegister();
    input = new InputReader(new Scanner(System.in));
    formatter = new ActivityFormatter();
    loadDemoData();
  }

  /**
   * Starts the user interface and runs the main menu until the user exits or the input ends.
   */
  public void start() {
    try {
      runMainMenu();
    } catch (NoSuchElementException e) {
      // The input has ended; finish as if the user chose to exit.
      System.out.println();
    }
    System.out.println("Goodbye!");
  }

  // ---------------------------------------------------------------- Demo data

  private void loadDemoData() {
    Person kari = personRegister.getOrCreate("Kari Nordmann");
    Person ola = personRegister.getOrCreate("Ola Hansen");
    Person ingrid = personRegister.getOrCreate("Ingrid Berg");

    Group mountainGang = new Group("Fjellgjengen");
    mountainGang.addMember(kari);
    mountainGang.addMember(ola);
    mountainGang.addMember(ingrid);
    groupRegister.addGroup(mountainGang);
    Person lars = personRegister.getOrCreate("Lars Haugen");
    Group bikeClub = new Group("Sykkelklubben");
    bikeClub.addMember(ola);
    bikeClub.addMember(lars);
    groupRegister.addGroup(bikeClub);

    Activity preikestolen = new Activity(kari, "Trip to Preikestolen",
        "Classic hike from Preikestolhytta. Clear weather and a great view over Lysefjorden.",
        LocalDateTime.of(2026, 8, 15, 9, 30), ActivityType.MOUNTAIN_HIKE, 7.6);
    preikestolen.shareWith(mountainGang);
    activityRegister.addActivity(preikestolen);

    activityRegister.addActivity(new Activity(ola, "Romsdalseggen ridge",
        "Long ridge walk from Vengedalen to Åndalsnes. Steep in places, so take care in wet "
            + "weather.",
        LocalDateTime.of(2026, 8, 22, 8, 0), ActivityType.MOUNTAIN_HIKE, 10.3));

    Activity aksla = new Activity(ingrid, "Sunset at Aksla",
        "Evening walk up the 418 steps from the city park in Ålesund.",
        LocalDateTime.of(2026, 9, 5, 19, 15), ActivityType.CITY_WALK, 3.2);
    aksla.shareWith(kari);
    aksla.shareWith(lars);
    activityRegister.addActivity(aksla);

    activityRegister.addActivity(new Activity(lars, "Galdhøpiggen summit",
        "Guided glacier crossing from Juvasshytta to the highest point in Norway.",
        LocalDateTime.of(2026, 9, 12, 7, 45), ActivityType.MOUNTAIN_HIKE, 11.0));

    Activity rallarvegen = new Activity(lars, "Rallarvegen",
        "Bike ride from Haugastøl down to Flåm along the old navvy road.",
        LocalDateTime.of(2026, 7, 20, 10, 0), ActivityType.BIKE_RIDE, 82.0);
    rallarvegen.shareWith(bikeClub);
    activityRegister.addActivity(rallarvegen);
  }

  // ---------------------------------------------------------------- Menus

  private void runMainMenu() {
    boolean running = true;
    while (running) {
      int choice = showMenu("=== Activity Logger v" + VERSION + " ===",
          "Activities", "Search", "Loggers", "Groups", "Sharing", "Statistics", "Exit");
      switch (choice) {
        case 1 -> runActivitiesMenu();
        case 2 -> runSearchMenu();
        case 3 -> runLoggersMenu();
        case 4 -> runGroupsMenu();
        case 5 -> runSharingMenu();
        case 6 -> runStatisticsMenu();
        default -> running = false;
      }
    }
  }

  private void runActivitiesMenu() {
    boolean running = true;
    while (running) {
      int choice = showMenu("--- Activities ---", "List all activities",
          "Show activity details", "Register new activity", "Delete activity", "Back");
      switch (choice) {
        case 1 -> listAllActivities();
        case 2 -> showActivityDetails();
        case 3 -> registerActivity();
        case 4 -> deleteActivity();
        default -> running = false;
      }
    }
  }

  private void runSearchMenu() {
    boolean running = true;
    while (running) {
      int choice = showMenu("--- Search ---", "By date", "By period", "By logger",
          "By keyword", "By type", "Back");
      switch (choice) {
        case 1 -> searchByDate();
        case 2 -> searchByPeriod();
        case 3 -> searchByLogger();
        case 4 -> searchByKeyword();
        case 5 -> searchByType();
        default -> running = false;
      }
    }
  }

  private void runLoggersMenu() {
    boolean running = true;
    while (running) {
      int choice = showMenu("--- Loggers ---", "List loggers", "Add logger", "Back");
      switch (choice) {
        case 1 -> listLoggers();
        case 2 -> addLogger();
        default -> running = false;
      }
    }
  }

  private void runGroupsMenu() {
    boolean running = true;
    while (running) {
      int choice = showMenu("--- Groups ---", "List groups", "Create group",
          "Add member to group", "Remove member from group", "Back");
      switch (choice) {
        case 1 -> listGroups();
        case 2 -> createGroup();
        case 3 -> addMemberToGroup();
        case 4 -> removeMemberFromGroup();
        default -> running = false;
      }
    }
  }

  private void runSharingMenu() {
    boolean running = true;
    while (running) {
      int choice = showMenu("--- Sharing ---", "Share activity with loggers",
          "Share activity with group", "Stop sharing with a logger", "Stop sharing with a group",
          "Make activity private", "Show activities shared with a logger", "Back");
      switch (choice) {
        case 1 -> shareWithLoggers();
        case 2 -> shareWithGroup();
        case 3 -> stopSharingWithLogger();
        case 4 -> stopSharingWithGroup();
        case 5 -> makeActivityPrivate();
        case 6 -> showActivitiesSharedWithLogger();
        default -> running = false;
      }
    }
  }

  private void runStatisticsMenu() {
    boolean running = true;
    while (running) {
      int choice = showMenu("--- Statistics ---", "Activities per logger",
          "Shared vs. private activities", "Back");
      switch (choice) {
        case 1 -> showCountPerLogger();
        case 2 -> showSharedVsPrivate();
        default -> running = false;
      }
    }
  }

  /**
   * Prints a menu and reads the choice. The last option is numbered 0 (back or exit).
   */
  private int showMenu(String title, String... options) {
    System.out.println();
    System.out.println(title);
    int last = options.length - 1;
    for (int i = 0; i < last; i++) {
      System.out.println((i + 1) + ". " + options[i]);
    }
    System.out.println("0. " + options[last]);
    return input.readInt("Choose an option: ", 0, last);
  }

  // ---------------------------------------------------------------- Activities

  private void listAllActivities() {
    List<Activity> activities = activityRegister.getActivitiesSorted();
    if (activities.isEmpty()) {
      System.out.println("No activities registered.");
      return;
    }
    System.out.println("All activities (" + activities.size() + "):");
    printSummaries(activities);
  }

  private void showActivityDetails() {
    chooseActivity().ifPresent(activity -> System.out.println(formatter.formatDetails(activity)));
  }

  private void registerActivity() {
    Optional<Person> logger = chooseOrCreateLogger();
    if (logger.isEmpty()) {
      return;
    }
    String title = input.readText("Title: ");
    String description = input.readText("Description: ");
    Optional<ActivityType> type = chooseType();
    if (type.isEmpty()) {
      return;
    }
    double distanceKm = input.readDouble("Distance in km: ", 0);
    LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    attempt(() -> {
      activityRegister.addActivity(new Activity(logger.get(), title, description,
          registeredAt, type.get(), distanceKm));
      System.out.println("Activity registered.");
    });
  }

  private void deleteActivity() {
    Optional<Activity> activity = chooseActivity();
    if (activity.isEmpty()) {
      return;
    }
    if (input.readYesNo("Delete \"" + activity.get().getTitle() + "\"? (y/n): ")) {
      attempt(() -> {
        activityRegister.removeActivity(activity.get());
        System.out.println("Activity deleted.");
      });
    } else {
      System.out.println("Cancelled.");
    }
  }

  // ---------------------------------------------------------------- Search

  private void searchByDate() {
    LocalDate date = input.readDate("Date (yyyy-MM-dd): ");
    attempt(() -> printSearchResult(activityRegister.findByDate(date)));
  }

  private void searchByPeriod() {
    LocalDate from = input.readDate("From date (yyyy-MM-dd): ");
    LocalDate to = input.readDate("To date (yyyy-MM-dd): ");
    attempt(() -> printSearchResult(activityRegister.findBetween(from, to)));
  }

  private void searchByLogger() {
    chooseLogger().ifPresent(logger ->
        attempt(() -> printSearchResult(activityRegister.findByLogger(logger))));
  }

  private void searchByKeyword() {
    String keyword = input.readText("Keyword: ");
    attempt(() -> printSearchResult(activityRegister.findByKeyword(keyword)));
  }

  private void searchByType() {
    chooseType().ifPresent(type ->
        attempt(() -> printSearchResult(activityRegister.findByType(type))));
  }

  // ---------------------------------------------------------------- Loggers

  private void listLoggers() {
    List<Person> persons = personRegister.getAllPersons();
    if (persons.isEmpty()) {
      System.out.println("No loggers registered.");
      return;
    }
    System.out.println("Loggers (" + persons.size() + "):");
    printNumbered(persons, Person::getName);
  }

  private void addLogger() {
    String name = input.readText("Name: ");
    attempt(() -> {
      personRegister.addPerson(new Person(name));
      System.out.println("Logger added.");
    });
  }

  // ---------------------------------------------------------------- Groups

  private void listGroups() {
    List<Group> groups = groupRegister.getAllGroups();
    if (groups.isEmpty()) {
      System.out.println("No groups registered.");
      return;
    }
    for (Group group : groups) {
      int count = group.getMemberCount();
      String members = count == 0 ? "-" : joinNames(group.getMembers());
      System.out.println("  " + group.getName() + " (" + count
          + (count == 1 ? " member" : " members") + "): " + members);
    }
  }

  private void createGroup() {
    String name = input.readText("Group name: ");
    attempt(() -> {
      groupRegister.addGroup(new Group(name));
      System.out.println("Group created.");
    });
  }

  private void addMemberToGroup() {
    Optional<Group> group = chooseGroup();
    if (group.isEmpty()) {
      return;
    }
    chooseLogger().ifPresent(person -> attempt(() -> {
      group.get().addMember(person);
      System.out.println(person.getName() + " added to " + group.get().getName() + ".");
    }));
  }

  private void removeMemberFromGroup() {
    Optional<Group> group = chooseGroup();
    if (group.isEmpty()) {
      return;
    }
    Optional<Person> member = choose("Members:", group.get().getMembers(), Person::getName,
        "member", "The group has no members.");
    member.ifPresent(person -> attempt(() -> {
      group.get().removeMember(person);
      System.out.println(person.getName() + " removed from " + group.get().getName() + ".");
    }));
  }

  // ---------------------------------------------------------------- Sharing

  private void shareWithLoggers() {
    Optional<Activity> activity = chooseActivity();
    if (activity.isEmpty()) {
      return;
    }
    List<Person> persons = personRegister.getAllPersons();
    if (persons.isEmpty()) {
      System.out.println("No loggers registered.");
      return;
    }
    System.out.println("Loggers:");
    printNumbered(persons, Person::getName);
    int choice = input.readInt("Choose logger (0 when done): ", 0, persons.size());
    while (choice != 0) {
      Person person = persons.get(choice - 1);
      attempt(() -> {
        activity.get().shareWith(person);
        System.out.println("Shared with " + person.getName() + ".");
      });
      choice = input.readInt("Choose logger (0 when done): ", 0, persons.size());
    }
  }

  private void shareWithGroup() {
    Optional<Activity> activity = chooseActivity();
    if (activity.isEmpty()) {
      return;
    }
    chooseGroup().ifPresent(group -> attempt(() -> {
      activity.get().shareWith(group);
      System.out.println("Shared with group " + group.getName() + ".");
    }));
  }

  private void stopSharingWithLogger() {
    Optional<Activity> activity = chooseActivity();
    if (activity.isEmpty()) {
      return;
    }
    Optional<Person> person = choose("Shared with loggers:",
        activity.get().getSharedWithPersons(), Person::getName, "logger",
        "This activity is not shared with any loggers.");
    person.ifPresent(chosen -> attempt(() -> {
      activity.get().unshareWith(chosen);
      System.out.println("Stopped sharing with " + chosen.getName() + ".");
    }));
  }

  private void stopSharingWithGroup() {
    Optional<Activity> activity = chooseActivity();
    if (activity.isEmpty()) {
      return;
    }
    Optional<Group> group = choose("Shared with groups:", activity.get().getSharedWithGroups(),
        Group::getName, "group", "This activity is not shared with any groups.");
    group.ifPresent(chosen -> attempt(() -> {
      activity.get().unshareWith(chosen);
      System.out.println("Stopped sharing with " + chosen.getName() + ".");
    }));
  }

  private void makeActivityPrivate() {
    chooseActivity().ifPresent(activity -> {
      activity.makePrivate();
      System.out.println("Activity is now private.");
    });
  }

  private void showActivitiesSharedWithLogger() {
    chooseLogger().ifPresent(person ->
        attempt(() -> printSearchResult(activityRegister.findSharedWith(person))));
  }

  // ---------------------------------------------------------------- Statistics

  private void showCountPerLogger() {
    Map<Person, Long> counts = activityRegister.countPerLogger();
    if (counts.isEmpty()) {
      System.out.println("No activities registered.");
      return;
    }
    System.out.println(String.format("%-20s %10s", "Logger", "Activities"));
    System.out.println("-".repeat(20) + " " + "-".repeat(10));
    counts.forEach((person, count) ->
        System.out.println(String.format("%-20s %10d", person.getName(), count)));
  }

  private void showSharedVsPrivate() {
    System.out.println("Shared activities:  " + activityRegister.countShared());
    System.out.println("Private activities: " + activityRegister.countPrivate());
  }

  // ---------------------------------------------------------------- Choosing from lists

  private Optional<Activity> chooseActivity() {
    List<Activity> activities = activityRegister.getActivitiesSorted();
    if (activities.isEmpty()) {
      System.out.println("No activities registered.");
      return Optional.empty();
    }
    System.out.println("Activities:");
    printSummaries(activities);
    int choice = input.readInt("Choose activity (0 to cancel): ", 0, activities.size());
    return choice == 0 ? Optional.empty() : Optional.of(activities.get(choice - 1));
  }

  private Optional<Person> chooseLogger() {
    return choose("Loggers:", personRegister.getAllPersons(), Person::getName, "logger",
        "No loggers registered.");
  }

  private Optional<Group> chooseGroup() {
    return choose("Groups:", groupRegister.getAllGroups(), Group::getName, "group",
        "No groups registered.");
  }

  private Optional<ActivityType> chooseType() {
    return choose("Activity types:", Arrays.asList(ActivityType.values()),
        ActivityType::getDisplayName, "type", "No activity types available.");
  }

  /**
   * Lets the user choose a logger from the register or add a new one (FR2-1).
   */
  private Optional<Person> chooseOrCreateLogger() {
    List<Person> persons = personRegister.getAllPersons();
    List<String> labels = new ArrayList<>(persons.stream().map(Person::getName).toList());
    labels.add("<New logger>");
    System.out.println("Loggers:");
    printNumbered(labels, Function.identity());
    int choice = input.readInt("Choose logger (0 to cancel): ", 0, labels.size());
    if (choice == 0) {
      return Optional.empty();
    }
    if (choice <= persons.size()) {
      return Optional.of(persons.get(choice - 1));
    }
    String name = input.readText("Name of new logger: ");
    return Optional.of(personRegister.getOrCreate(name));
  }

  private <T> Optional<T> choose(String heading, List<T> items, Function<T, String> label,
      String thing, String emptyMessage) {
    if (items.isEmpty()) {
      System.out.println(emptyMessage);
      return Optional.empty();
    }
    System.out.println(heading);
    printNumbered(items, label);
    int choice = input.readInt("Choose " + thing + " (0 to cancel): ", 0, items.size());
    return choice == 0 ? Optional.empty() : Optional.of(items.get(choice - 1));
  }

  // ---------------------------------------------------------------- Output helpers

  private <T> void printNumbered(List<T> items, Function<T, String> label) {
    for (int i = 0; i < items.size(); i++) {
      System.out.println("  " + (i + 1) + ". " + label.apply(items.get(i)));
    }
  }

  private void printSummaries(List<Activity> activities) {
    for (int i = 0; i < activities.size(); i++) {
      System.out.println(formatter.formatSummary(i + 1, activities.get(i)));
    }
  }

  private void printSearchResult(List<Activity> activities) {
    if (activities.isEmpty()) {
      System.out.println("No activities found.");
      return;
    }
    System.out.println("Found " + activities.size()
        + (activities.size() == 1 ? " activity:" : " activities:"));
    printSummaries(activities);
  }

  private String joinNames(List<Person> persons) {
    return persons.stream().map(Person::getName).collect(Collectors.joining(", "));
  }

  /**
   * Runs an action that calls the model or logic classes, and shows any error from them as
   * {@code Error: <message>}. Input is never read inside the action, so the end of input
   * is not mistaken for an error.
   */
  private void attempt(Runnable action) {
    try {
      action.run();
    } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException
             | NullPointerException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}
