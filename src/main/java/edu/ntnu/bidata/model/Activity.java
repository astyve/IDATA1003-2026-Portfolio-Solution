package edu.ntnu.bidata.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * An activity, such as a mountain hike, logged by a person in the Activity Logger.
 *
 * <p><b>Role:</b> the central entity of the application. An activity holds the information
 * about one logged trip or activity. It does not print itself; presenting an activity is the
 * job of the user interface.
 *
 * <p><b>Fields and types:</b>
 * <ul>
 *   <li>{@code loggerName} ({@link String}): the name of the person who logged the activity.
 *       The application has no login, so the name is entered per activity as text.</li>
 *   <li>{@code title} ({@link String}): a short title, e.g. "Trip to Preikestolen".</li>
 *   <li>{@code description} ({@link String}): a longer free text describing the activity.</li>
 *   <li>{@code registeredAt} ({@link LocalDateTime}): the date and time the activity was
 *       registered. One type holds both date and time, which makes sorting by time and
 *       searching by date simple.</li>
 * </ul>
 *
 * <p><b>Final and mutable fields:</b> {@code loggerName} and {@code registeredAt} are final,
 * since who logged an activity and when it was registered never change. {@code title} and
 * {@code description} can be changed with setters, so mistakes can be corrected.
 *
 * <p><b>Invalid data:</b> the constructor and the setters validate their arguments and throw
 * an exception instead of storing dummy values, so an {@code Activity} object is always valid.
 * A {@code null} argument gives a {@link NullPointerException}; a blank text or a registration
 * time in the future gives an {@link IllegalArgumentException}. Text values are stored trimmed.
 */
public class Activity {

  private final String loggerName;
  private String title;
  private String description;
  private final LocalDateTime registeredAt;

  /**
   * Creates a new activity.
   *
   * @param loggerName the name of the person logging the activity, not blank
   * @param title the title of the activity, not blank
   * @param description the description of the activity, not blank
   * @param registeredAt the date and time the activity was registered, not in the future
   * @throws NullPointerException if any argument is {@code null}
   * @throws IllegalArgumentException if a text is blank or {@code registeredAt} is in the
   *     future
   */
  public Activity(String loggerName, String title, String description,
      LocalDateTime registeredAt) {
    Objects.requireNonNull(loggerName, "Logger name cannot be null.");
    Objects.requireNonNull(title, "Title cannot be null.");
    Objects.requireNonNull(description, "Description cannot be null.");
    Objects.requireNonNull(registeredAt, "Registration time cannot be null.");
    this.loggerName = requireNotBlank(loggerName, "Logger name cannot be blank.");
    this.title = requireNotBlank(title, "Title cannot be blank.");
    this.description = requireNotBlank(description, "Description cannot be blank.");
    if (registeredAt.isAfter(LocalDateTime.now())) {
      throw new IllegalArgumentException("Registration time cannot be in the future.");
    }
    this.registeredAt = registeredAt;
  }

  /**
   * Returns the name of the person who logged the activity.
   *
   * @return the logger name
   */
  public String getLoggerName() {
    return loggerName;
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

  private static String requireNotBlank(String text, String message) {
    if (text.isBlank()) {
      throw new IllegalArgumentException(message);
    }
    return text.trim();
  }
}
