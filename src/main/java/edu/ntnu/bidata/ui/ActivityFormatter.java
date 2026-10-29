package edu.ntnu.bidata.ui;

import edu.ntnu.bidata.model.Activity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Formats activities as text for the user interface.
 *
 * <p>The methods only build text and never print, so the formatting is kept in one place and
 * separate from the menu logic.
 */
public class ActivityFormatter {

  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  /**
   * Creates a new activity formatter.
   */
  public ActivityFormatter() {
    // No state to set up.
  }

  /**
   * Formats an activity as a numbered one-line summary, e.g.
   * {@code "  1. 2026-08-15 09:30  Kari Nordmann    Trip to Preikestolen  [shared]"}.
   *
   * @param number the number to show in front of the activity
   * @param activity the activity to format
   * @return the summary line
   * @throws NullPointerException if {@code activity} is {@code null}
   */
  public String formatSummary(int number, Activity activity) {
    Objects.requireNonNull(activity, "Activity cannot be null.");
    return String.format("%3d. %s  %-15s  %s%s", number,
        formatDateTime(activity.getRegisteredAt()), activity.getLogger().getName(),
        activity.getTitle(), activity.isShared() ? "  [shared]" : "");
  }

  /**
   * Formats all details of an activity as a block of labelled lines.
   *
   * @param activity the activity to format
   * @return the details, one line per field
   * @throws NullPointerException if {@code activity} is {@code null}
   */
  public String formatDetails(Activity activity) {
    Objects.requireNonNull(activity, "Activity cannot be null.");
    List<String> lines = List.of(
        line("Title:", activity.getTitle()),
        line("Logger:", activity.getLogger().getName()),
        line("Registered:", formatDateTime(activity.getRegisteredAt())),
        line("Type:", activity.getType().getDisplayName()),
        line("Distance:", String.format(Locale.ROOT, "%.1f km", activity.getDistanceKm())),
        line("Sharing:", sharingText(activity)),
        line("Description:", activity.getDescription()));
    return String.join(System.lineSeparator(), lines);
  }

  /**
   * Formats a date and time as {@code yyyy-MM-dd HH:mm}.
   *
   * @param dateTime the date and time to format
   * @return the formatted date and time
   * @throws NullPointerException if {@code dateTime} is {@code null}
   */
  public String formatDateTime(LocalDateTime dateTime) {
    Objects.requireNonNull(dateTime, "Date and time cannot be null.");
    return dateTime.format(DATE_TIME_FORMAT);
  }

  private String line(String label, String value) {
    return String.format("%-13s%s", label, value);
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
}
