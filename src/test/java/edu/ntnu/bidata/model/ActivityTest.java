package edu.ntnu.bidata.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Activity}.
 *
 * <p>Planned positive tests:
 * <ul>
 *   <li>The constructor stores logger name, title, description and registration time.</li>
 *   <li>The constructor trims the text values.</li>
 *   <li>A registration time equal to now is accepted.</li>
 *   <li>{@code setTitle} and {@code setDescription} change and trim the value.</li>
 * </ul>
 *
 * <p>Planned negative tests:
 * <ul>
 *   <li>The constructor throws {@link NullPointerException} for a {@code null} logger name,
 *       title, description or registration time.</li>
 *   <li>The constructor throws {@link IllegalArgumentException} for a blank logger name,
 *       title or description, and for a registration time in the future.</li>
 *   <li>The setters throw {@link NullPointerException} for {@code null} and
 *       {@link IllegalArgumentException} for a blank value.</li>
 * </ul>
 */
class ActivityTest {

  private static final LocalDateTime REGISTERED_AT = LocalDateTime.of(2026, 8, 15, 9, 30);

  private Activity activity;

  /** Creates a valid activity used by several tests. */
  @BeforeEach
  void setUp() {
    activity = new Activity("Kari", "Hike", "A nice hike.", REGISTERED_AT);
  }

  /** The constructor stores all values, which the getters return. */
  @Test
  void constructorStoresAllValues() {
    // Arrange
    LocalDateTime time = LocalDateTime.of(2026, 1, 2, 3, 4);

    // Act
    Activity created = new Activity("Ola", "Bike ride", "Along the fjord.", time);

    // Assert
    assertEquals("Ola", created.getLoggerName());
    assertEquals("Bike ride", created.getTitle());
    assertEquals("Along the fjord.", created.getDescription());
    assertEquals(time, created.getRegisteredAt());
  }

  /** The constructor stores the text values trimmed. */
  @Test
  void constructorTrimsTextValues() {
    // Arrange
    String loggerName = "  Ola ";
    String title = " Bike ride  ";
    String description = "  Along the fjord. ";

    // Act
    Activity created = new Activity(loggerName, title, description, REGISTERED_AT);

    // Assert
    assertEquals("Ola", created.getLoggerName());
    assertEquals("Bike ride", created.getTitle());
    assertEquals("Along the fjord.", created.getDescription());
  }

  /** A registration time of now is accepted, since it is not in the future. */
  @Test
  void constructorAcceptsRegistrationTimeNow() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();

    // Act
    Activity created = new Activity("Ola", "Walk", "Short walk.", now);

    // Assert
    assertEquals(now, created.getRegisteredAt());
  }

  /** The constructor throws NullPointerException when the logger name is null. */
  @Test
  void constructorThrowsWhenLoggerNameIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> new Activity(null, "Hike", "A nice hike.", REGISTERED_AT));
  }

  /** The constructor throws NullPointerException when the title is null. */
  @Test
  void constructorThrowsWhenTitleIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> new Activity("Kari", null, "A nice hike.", REGISTERED_AT));
  }

  /** The constructor throws NullPointerException when the description is null. */
  @Test
  void constructorThrowsWhenDescriptionIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> new Activity("Kari", "Hike", null, REGISTERED_AT));
  }

  /** The constructor throws NullPointerException when the registration time is null. */
  @Test
  void constructorThrowsWhenRegisteredAtIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> new Activity("Kari", "Hike", "A nice hike.", null));
  }

  /** The constructor throws IllegalArgumentException when the logger name is blank. */
  @Test
  void constructorThrowsWhenLoggerNameIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> new Activity("  ", "Hike", "A nice hike.", REGISTERED_AT));
  }

  /** The constructor throws IllegalArgumentException when the title is blank. */
  @Test
  void constructorThrowsWhenTitleIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> new Activity("Kari", "", "A nice hike.", REGISTERED_AT));
  }

  /** The constructor throws IllegalArgumentException when the description is blank. */
  @Test
  void constructorThrowsWhenDescriptionIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> new Activity("Kari", "Hike", " ", REGISTERED_AT));
  }

  /** The constructor throws IllegalArgumentException when the time is in the future. */
  @Test
  void constructorThrowsWhenRegisteredAtIsInTheFuture() {
    // Arrange
    LocalDateTime future = LocalDateTime.now().plusDays(1);

    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> new Activity("Kari", "Hike", "A nice hike.", future));
  }

  /** {@code setTitle} changes the title and stores it trimmed. */
  @Test
  void setTitleChangesTitle() {
    // Act
    activity.setTitle(" New title ");

    // Assert
    assertEquals("New title", activity.getTitle());
  }

  /** {@code setTitle} throws NullPointerException when the title is null. */
  @Test
  void setTitleThrowsWhenTitleIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> activity.setTitle(null));
  }

  /** {@code setTitle} throws IllegalArgumentException when the title is blank. */
  @Test
  void setTitleThrowsWhenTitleIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> activity.setTitle("  "));
  }

  /** {@code setDescription} changes the description and stores it trimmed. */
  @Test
  void setDescriptionChangesDescription() {
    // Act
    activity.setDescription(" New description. ");

    // Assert
    assertEquals("New description.", activity.getDescription());
  }

  /** {@code setDescription} throws NullPointerException when the description is null. */
  @Test
  void setDescriptionThrowsWhenDescriptionIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> activity.setDescription(null));
  }

  /** {@code setDescription} throws IllegalArgumentException when the description is blank. */
  @Test
  void setDescriptionThrowsWhenDescriptionIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> activity.setDescription(""));
  }
}
