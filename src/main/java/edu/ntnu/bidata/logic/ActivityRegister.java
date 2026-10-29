package edu.ntnu.bidata.logic;

import edu.ntnu.bidata.model.Activity;
import edu.ntnu.bidata.model.Person;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The register of all activities in the application.
 *
 * <p>The activities are kept in an {@link ArrayList}, since an activity has no natural unique
 * key. Every method that returns activities returns a new, unmodifiable list sorted by
 * registration time, oldest first, so the internal list is never exposed.
 */
public class ActivityRegister {

  private static final Comparator<Activity> BY_TIME =
      Comparator.comparing(Activity::getRegisteredAt);

  private final List<Activity> activities = new ArrayList<>();

  /**
   * Creates an empty activity register.
   */
  public ActivityRegister() {
    // The register starts empty.
  }

  /**
   * Adds an activity to the register.
   *
   * @param activity the activity to add
   * @throws NullPointerException if {@code activity} is {@code null}
   * @throws IllegalStateException if the activity is already registered
   */
  public void addActivity(Activity activity) {
    Objects.requireNonNull(activity, "Activity cannot be null.");
    if (activities.contains(activity)) {
      throw new IllegalStateException("The activity is already registered.");
    }
    activities.add(activity);
  }

  /**
   * Removes an activity from the register.
   *
   * @param activity the activity to remove
   * @throws NullPointerException if {@code activity} is {@code null}
   * @throws NoSuchElementException if the activity is not registered
   */
  public void removeActivity(Activity activity) {
    Objects.requireNonNull(activity, "Activity cannot be null.");
    if (!activities.remove(activity)) {
      throw new NoSuchElementException("The activity is not registered.");
    }
  }

  /**
   * Returns all activities, sorted by registration time, oldest first.
   *
   * @return an unmodifiable sorted list of all activities
   */
  public List<Activity> getActivitiesSorted() {
    return find(activity -> true);
  }

  /**
   * Finds all activities registered on a given date.
   *
   * @param date the date to search for
   * @return an unmodifiable sorted list of the activities on that date, empty if none
   * @throws NullPointerException if {@code date} is {@code null}
   */
  public List<Activity> findByDate(LocalDate date) {
    Objects.requireNonNull(date, "Date cannot be null.");
    return find(activity -> activity.getRegisteredAt().toLocalDate().equals(date));
  }

  /**
   * Finds all activities registered in a period. Both dates are included.
   *
   * @param from the first date of the period
   * @param to the last date of the period
   * @return an unmodifiable sorted list of the activities in the period
   * @throws NullPointerException if {@code from} or {@code to} is {@code null}
   * @throws IllegalArgumentException if {@code from} is after {@code to}
   */
  public List<Activity> findBetween(LocalDate from, LocalDate to) {
    Objects.requireNonNull(from, "From date cannot be null.");
    Objects.requireNonNull(to, "To date cannot be null.");
    if (from.isAfter(to)) {
      throw new IllegalArgumentException("From date cannot be after to date.");
    }
    return find(activity -> {
      LocalDate date = activity.getRegisteredAt().toLocalDate();
      return !date.isBefore(from) && !date.isAfter(to);
    });
  }

  /**
   * Finds all activities logged by a given person.
   *
   * @param logger the logger to search for
   * @return an unmodifiable sorted list of the activities by that logger
   * @throws NullPointerException if {@code logger} is {@code null}
   */
  public List<Activity> findByLogger(Person logger) {
    Objects.requireNonNull(logger, "Logger cannot be null.");
    return find(activity -> activity.getLogger().equals(logger));
  }

  /**
   * Finds all activities whose title or description contains a keyword, ignoring case.
   *
   * @param keyword the word to search for, not blank
   * @return an unmodifiable sorted list of the matching activities
   * @throws NullPointerException if {@code keyword} is {@code null}
   * @throws IllegalArgumentException if {@code keyword} is blank
   */
  public List<Activity> findByKeyword(String keyword) {
    Objects.requireNonNull(keyword, "Keyword cannot be null.");
    if (keyword.isBlank()) {
      throw new IllegalArgumentException("Keyword cannot be blank.");
    }
    String word = keyword.trim().toLowerCase(Locale.ROOT);
    return find(activity -> activity.getTitle().toLowerCase(Locale.ROOT).contains(word)
        || activity.getDescription().toLowerCase(Locale.ROOT).contains(word));
  }

  /**
   * Finds all activities shared with a person, directly or through a group. The person's
   * own activities are not included.
   *
   * @param person the person to search for
   * @return an unmodifiable sorted list of the activities shared with the person
   * @throws NullPointerException if {@code person} is {@code null}
   */
  public List<Activity> findSharedWith(Person person) {
    Objects.requireNonNull(person, "Person cannot be null.");
    return find(activity -> activity.isSharedWith(person));
  }

  /**
   * Counts the activities per logger. Only loggers with at least one activity are included.
   *
   * @return an unmodifiable map from logger to number of activities, ordered by logger name
   *     ignoring case
   */
  public Map<Person, Long> countPerLogger() {
    Map<Person, Long> counts = activities.stream()
        .collect(Collectors.groupingBy(Activity::getLogger, Collectors.counting()));
    Map<Person, Long> sorted = counts.entrySet().stream()
        .sorted(Map.Entry.comparingByKey(
            Comparator.comparing(Person::getName, String.CASE_INSENSITIVE_ORDER)))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (first, second) -> first, LinkedHashMap::new));
    return Collections.unmodifiableMap(sorted);
  }

  /**
   * Returns the number of activities in the register.
   *
   * @return the number of activities
   */
  public int size() {
    return activities.size();
  }

  /**
   * Checks whether the register has no activities.
   *
   * @return {@code true} if the register is empty
   */
  public boolean isEmpty() {
    return activities.isEmpty();
  }

  private List<Activity> find(Predicate<Activity> condition) {
    return activities.stream().filter(condition).sorted(BY_TIME).toList();
  }
}
