package edu.ntnu.bidata.ui;

import edu.ntnu.bidata.logic.ActivityRegister;
import edu.ntnu.bidata.logic.GroupRegister;
import edu.ntnu.bidata.logic.PersonRegister;
import edu.ntnu.bidata.model.Activity;
import edu.ntnu.bidata.model.Group;
import edu.ntnu.bidata.model.Person;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The text user interface of the Activity Logger.
 *
 * <p>In Part 2 the user interface loads demo loggers, groups and activities into the
 * registers and runs a scripted demo of the register functionality. There is no interactive
 * menu yet.
 */
public class ActivityLoggerUi {

  /** The version of the application. */
  public static final String VERSION = "0.2.0";

  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final String SEPARATOR = "-".repeat(40);

  private ActivityRegister activityRegister;
  private PersonRegister personRegister;
  private GroupRegister groupRegister;

  /**
   * Creates the user interface. Call {@link #init()} before {@link #start()}.
   */
  public ActivityLoggerUi() {
    // The registers are created in init().
  }

  /**
   * Initialises the user interface by creating the registers and loading the demo data.
   */
  public void init() {
    activityRegister = new ActivityRegister();
    personRegister = new PersonRegister();
    groupRegister = new GroupRegister();

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
        LocalDateTime.of(2026, 8, 15, 9, 30));
    preikestolen.shareWith(mountainGang);
    activityRegister.addActivity(preikestolen);

    activityRegister.addActivity(new Activity(ola, "Romsdalseggen ridge",
        "Long ridge walk from Vengedalen to Åndalsnes. Steep in places, so take care in wet "
            + "weather.",
        LocalDateTime.of(2026, 8, 22, 8, 0)));

    Activity aksla = new Activity(ingrid, "Sunset at Aksla",
        "Evening walk up the 418 steps from the city park in Ålesund.",
        LocalDateTime.of(2026, 9, 5, 19, 15));
    aksla.shareWith(kari);
    aksla.shareWith(lars);
    activityRegister.addActivity(aksla);

    activityRegister.addActivity(new Activity(lars, "Galdhøpiggen summit",
        "Guided glacier crossing from Juvasshytta to the highest point in Norway.",
        LocalDateTime.of(2026, 9, 12, 7, 45)));
  }

  /**
   * Starts the user interface by running a scripted demo of the registers.
   */
  public void start() {
    String heading = "Activity Logger v" + VERSION;
    System.out.println(heading);
    System.out.println("=".repeat(heading.length()));

    List<Activity> all = activityRegister.getActivitiesSorted();
    printHeading("All activities (" + all.size() + "):");
    for (int i = 0; i < all.size(); i++) {
      if (i > 0) {
        System.out.println(SEPARATOR);
      }
      printDetails(all.get(i));
    }

    printHeading("Activities on 2026-08-22:");
    printSummaries(activityRegister.findByDate(LocalDate.of(2026, 8, 22)));

    printHeading("Activities from 2026-08-01 to 2026-08-31:");
    printSummaries(activityRegister.findBetween(
        LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31)));

    Person lars = personRegister.findByName("Lars Haugen").orElseThrow();
    printHeading("Activities by " + lars + ":");
    printSummaries(activityRegister.findByLogger(lars));

    printHeading("Activities containing \"ridge\":");
    printSummaries(activityRegister.findByKeyword("ridge"));

    Person kari = personRegister.findByName("Kari Nordmann").orElseThrow();
    printHeading("Activities shared with " + kari + ":");
    printSummaries(activityRegister.findSharedWith(kari));

    Person ola = personRegister.findByName("Ola Hansen").orElseThrow();
    printHeading("Activities shared with " + ola + ":");
    printSummaries(activityRegister.findSharedWith(ola));

    printHeading("Groups:");
    for (Group group : groupRegister.getAllGroups()) {
      System.out.println("  " + group.getName() + ": " + joinNames(group.getMembers()));
    }

    printHeading("Activities per logger:");
    printCountTable(activityRegister.countPerLogger());

    List<Activity> toDelete = activityRegister.findByKeyword("Romsdalseggen");
    for (Activity activity : toDelete) {
      printHeading("Deleting \"" + activity.getTitle() + "\"...");
      activityRegister.removeActivity(activity);
    }
    System.out.println("Activities in register: " + activityRegister.size());
  }

  private void printHeading(String heading) {
    System.out.println();
    System.out.println(heading);
  }

  private void printDetails(Activity activity) {
    printLine("Title:", activity.getTitle());
    printLine("Logger:", activity.getLogger().getName());
    printLine("Registered:", activity.getRegisteredAt().format(DATE_TIME_FORMAT));
    printLine("Sharing:", sharingText(activity));
    printLine("Description:", activity.getDescription());
  }

  private void printLine(String label, String value) {
    System.out.println(String.format("%-13s%s", label, value));
  }

  private String sharingText(Activity activity) {
    if (!activity.isShared()) {
      return "Private";
    }
    List<String> parts = new ArrayList<>();
    activity.getSharedWithPersons().forEach(person -> parts.add(person.getName()));
    activity.getSharedWithGroups().forEach(group -> parts.add("group " + group.getName()));
    return "Shared with " + String.join(", ", parts);
  }

  private void printSummaries(List<Activity> activities) {
    if (activities.isEmpty()) {
      System.out.println("  (none)");
    }
    for (int i = 0; i < activities.size(); i++) {
      Activity activity = activities.get(i);
      System.out.println(String.format("%3d. %s  %-15s  %s%s", i + 1,
          activity.getRegisteredAt().format(DATE_TIME_FORMAT),
          activity.getLogger().getName(), activity.getTitle(),
          activity.isShared() ? "  [shared]" : ""));
    }
  }

  private void printCountTable(Map<Person, Long> counts) {
    System.out.println(String.format("%-20s %10s", "Logger", "Activities"));
    System.out.println("-".repeat(20) + " " + "-".repeat(10));
    counts.forEach((person, count) ->
        System.out.println(String.format("%-20s %10d", person.getName(), count)));
  }

  private String joinNames(List<Person> persons) {
    return persons.stream().map(Person::getName).collect(Collectors.joining(", "));
  }
}
