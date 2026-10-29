package edu.ntnu.bidata.ui;

import edu.ntnu.bidata.model.Activity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The text user interface of the Activity Logger.
 *
 * <p>In Part 1 the user interface creates a few demo activities and prints them.
 */
public class ActivityLoggerUi {

  /** The version of the application. */
  public static final String VERSION = "0.1.0";

  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final String SEPARATOR = "-".repeat(40);

  private final List<Activity> activities = new ArrayList<>();

  /**
   * Creates the user interface. Call {@link #init()} before {@link #start()}.
   */
  public ActivityLoggerUi() {
    // Nothing to set up until init() is called.
  }

  /**
   * Initialises the user interface by creating the demo activities.
   */
  public void init() {
    activities.add(new Activity("Kari Nordmann", "Trip to Preikestolen",
        "Classic hike from Preikestolhytta. Clear weather and a great view over Lysefjorden.",
        LocalDateTime.of(2026, 8, 15, 9, 30)));
    activities.add(new Activity("Ola Hansen", "Romsdalseggen ridge",
        "Long ridge walk from Vengedalen to Åndalsnes. Steep in places, so take care in wet "
            + "weather.",
        LocalDateTime.of(2026, 8, 22, 8, 0)));
    activities.add(new Activity("Ingrid Berg", "Sunset at Aksla",
        "Evening walk up the 418 steps from the city park in Ålesund.",
        LocalDateTime.of(2026, 9, 5, 19, 15)));
    activities.add(new Activity("Lars Haugen", "Galdhøpiggen summit",
        "Guided glacier crossing from Juvasshytta to the highest point in Norway.",
        LocalDateTime.of(2026, 9, 12, 7, 45)));
  }

  /**
   * Starts the user interface by printing all demo activities.
   */
  public void start() {
    String heading = "Activity Logger v" + VERSION;
    System.out.println(heading);
    System.out.println("=".repeat(heading.length()));
    System.out.println();
    for (int i = 0; i < activities.size(); i++) {
      if (i > 0) {
        System.out.println(SEPARATOR);
      }
      printDetails(activities.get(i));
    }
  }

  private void printDetails(Activity activity) {
    printLine("Title:", activity.getTitle());
    printLine("Logger:", activity.getLoggerName());
    printLine("Registered:", activity.getRegisteredAt().format(DATE_TIME_FORMAT));
    printLine("Description:", activity.getDescription());
  }

  private void printLine(String label, String value) {
    System.out.println(String.format("%-13s%s", label, value));
  }
}
