package edu.ntnu.bidata.model;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * An activity, such as a mountain hike, logged by a person in the Activity Logger.
 *
 * <p><b>Role:</b> the central entity of the application. An activity holds the information
 * about one logged trip or activity, and who it is shared with. It does not print itself;
 * presenting an activity is the job of the user interface.
 *
 * <p><b>Fields and types:</b>
 * <ul>
 *   <li>{@code logger} ({@link Person}): the person who logged the activity. An object
 *       reference instead of a name, so all activities by the same person refer to the same
 *       logger.</li>
 *   <li>{@code title} ({@link String}): a short title, e.g. "Trip to Preikestolen".</li>
 *   <li>{@code description} ({@link String}): a longer free text describing the activity.</li>
 *   <li>{@code registeredAt} ({@link LocalDateTime}): the date and time the activity was
 *       registered. One type holds both date and time, which makes sorting by time and
 *       searching by date simple.</li>
 *   <li>{@code sharedWithPersons} ({@link Set} of {@link Person}): the persons the activity
 *       is shared with. A set, since sharing with the same person twice has no meaning.</li>
 *   <li>{@code sharedWithGroups} ({@link Set} of {@link Group}): the groups the activity is
 *       shared with. The group itself is stored, so whoever is a member now can see the
 *       activity.</li>
 * </ul>
 *
 * <p>An activity is <i>shared</i> if it is shared with at least one person or group, and
 * otherwise <i>private</i>. This is derived from the two sets, so the two can never disagree.
 *
 * <p><b>Final and mutable fields:</b> {@code logger} and {@code registeredAt} are final,
 * since who logged an activity and when it was registered never change. {@code title} and
 * {@code description} can be changed with setters, so mistakes can be corrected. The two
 * share sets are final references, but their contents change when the activity is shared or
 * made private.
 *
 * <p><b>Invalid data:</b> the constructor and the methods validate their arguments and throw
 * an exception instead of storing dummy values, so an {@code Activity} object is always valid.
 * A {@code null} argument gives a {@link NullPointerException}; a blank text, a registration
 * time in the future or sharing with the activity's own logger gives an
 * {@link IllegalArgumentException}. Text values are stored trimmed.
 */
public class Activity {

  private static final Comparator<Person> PERSON_ORDER =
      Comparator.comparing(Person::getName, String.CASE_INSENSITIVE_ORDER);
  private static final Comparator<Group> GROUP_ORDER =
      Comparator.comparing(Group::getName, String.CASE_INSENSITIVE_ORDER);

  private final Person logger;
  private String title;
  private String description;
  private final LocalDateTime registeredAt;
  private final Set<Person> sharedWithPersons = new HashSet<>();
  private final Set<Group> sharedWithGroups = new HashSet<>();

  /**
   * Creates a new, private activity.
   *
   * @param logger the person logging the activity
   * @param title the title of the activity, not blank
   * @param description the description of the activity, not blank
   * @param registeredAt the date and time the activity was registered, not in the future
   * @throws NullPointerException if any argument is {@code null}
   * @throws IllegalArgumentException if a text is blank or {@code registeredAt} is in the
   *     future
   */
  public Activity(Person logger, String title, String description,
      LocalDateTime registeredAt) {
    Objects.requireNonNull(logger, "Logger cannot be null.");
    Objects.requireNonNull(title, "Title cannot be null.");
    Objects.requireNonNull(description, "Description cannot be null.");
    Objects.requireNonNull(registeredAt, "Registration time cannot be null.");
    this.title = requireNotBlank(title, "Title cannot be blank.");
    this.description = requireNotBlank(description, "Description cannot be blank.");
    if (registeredAt.isAfter(LocalDateTime.now())) {
      throw new IllegalArgumentException("Registration time cannot be in the future.");
    }
    this.logger = logger;
    this.registeredAt = registeredAt;
  }

  /**
   * Returns the person who logged the activity.
   *
   * @return the logger
   */
  public Person getLogger() {
    return logger;
  }

  /**
   * Returns the title of the activity.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the description of the activity.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the date and time the activity was registered.
   *
   * @return the registration time
   */
  public LocalDateTime getRegisteredAt() {
    return registeredAt;
  }

  /**
   * Changes the title of the activity.
   *
   * @param title the new title, not blank
   * @throws NullPointerException if {@code title} is {@code null}
   * @throws IllegalArgumentException if {@code title} is blank
   */
  public void setTitle(String title) {
    Objects.requireNonNull(title, "Title cannot be null.");
    this.title = requireNotBlank(title, "Title cannot be blank.");
  }

  /**
   * Changes the description of the activity.
   *
   * @param description the new description, not blank
   * @throws NullPointerException if {@code description} is {@code null}
   * @throws IllegalArgumentException if {@code description} is blank
   */
  public void setDescription(String description) {
    Objects.requireNonNull(description, "Description cannot be null.");
    this.description = requireNotBlank(description, "Description cannot be blank.");
  }

  /**
   * Shares the activity with a person. Sharing with the same person again has no effect.
   *
   * @param person the person to share with
   * @throws NullPointerException if {@code person} is {@code null}
   * @throws IllegalArgumentException if {@code person} is the logger of the activity
   */
  public void shareWith(Person person) {
    Objects.requireNonNull(person, "Person cannot be null.");
    if (person.equals(logger)) {
      throw new IllegalArgumentException("An activity cannot be shared with its own logger.");
    }
    sharedWithPersons.add(person);
  }

  /**
   * Shares the activity with a group, so all current members of the group can see it.
   * Sharing with the same group again has no effect.
   *
   * @param group the group to share with
   * @throws NullPointerException if {@code group} is {@code null}
   */
  public void shareWith(Group group) {
    Objects.requireNonNull(group, "Group cannot be null.");
    sharedWithGroups.add(group);
  }

  /**
   * Makes the activity private by removing all persons and groups it is shared with.
   */
  public void makePrivate() {
    sharedWithPersons.clear();
    sharedWithGroups.clear();
  }

  /**
   * Checks whether the activity is shared with at least one person or group.
   *
   * @return {@code true} if the activity is shared, {@code false} if it is private
   */
  public boolean isShared() {
    return !sharedWithPersons.isEmpty() || !sharedWithGroups.isEmpty();
  }

  /**
   * Checks whether the activity is shared with a person, either directly or through a group
   * the person is a member of now. The logger is never counted as shared with.
   *
   * @param person the person to check
   * @return {@code true} if the activity is shared with the person
   * @throws NullPointerException if {@code person} is {@code null}
   */
  public boolean isSharedWith(Person person) {
    Objects.requireNonNull(person, "Person cannot be null.");
    if (person.equals(logger)) {
      return false;
    }
    return sharedWithPersons.contains(person)
        || sharedWithGroups.stream().anyMatch(group -> group.hasMember(person));
  }

  /**
   * Checks whether a person can see the activity: the logger, or someone it is shared with.
   *
   * @param person the person to check
   * @return {@code true} if the person can see the activity
   * @throws NullPointerException if {@code person} is {@code null}
   */
  public boolean isVisibleTo(Person person) {
    Objects.requireNonNull(person, "Person cannot be null.");
    return person.equals(logger) || isSharedWith(person);
  }

  /**
   * Returns the persons the activity is shared with directly, sorted by name ignoring case.
   *
   * @return an unmodifiable list of persons
   */
  public List<Person> getSharedWithPersons() {
    return sharedWithPersons.stream().sorted(PERSON_ORDER).toList();
  }

  /**
   * Returns the groups the activity is shared with, sorted by name ignoring case.
   *
   * @return an unmodifiable list of groups
   */
  public List<Group> getSharedWithGroups() {
    return sharedWithGroups.stream().sorted(GROUP_ORDER).toList();
  }

  private static String requireNotBlank(String text, String message) {
    if (text.isBlank()) {
      throw new IllegalArgumentException(message);
    }
    return text.trim();
  }
}
