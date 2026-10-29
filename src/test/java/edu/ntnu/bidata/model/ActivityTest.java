package edu.ntnu.bidata.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Activity}.
 *
 * <p>Planned positive tests:
 * <ul>
 *   <li>The constructor stores logger, title, description and registration time, trims the
 *       texts, and creates a private activity.</li>
 *   <li>A registration time equal to now is accepted.</li>
 *   <li>{@code setTitle} and {@code setDescription} change and trim the value.</li>
 *   <li>Sharing with a person or a group makes the activity shared; sharing twice has no
 *       effect.</li>
 *   <li>{@code makePrivate} removes all sharing.</li>
 *   <li>{@code isSharedWith} is true for directly shared persons and current group members,
 *       false after leaving the group, and never true for the logger.</li>
 *   <li>{@code isVisibleTo} is true for the logger and shared persons, false for others.</li>
 *   <li>The share lists are sorted by name and cannot be modified.</li>
 * </ul>
 *
 * <p>Planned negative tests:
 * <ul>
 *   <li>The constructor throws {@link NullPointerException} for each {@code null} argument.</li>
 *   <li>The constructor throws {@link IllegalArgumentException} for a blank title or
 *       description, and for a registration time in the future.</li>
 *   <li>The setters throw {@link NullPointerException} for {@code null} and
 *       {@link IllegalArgumentException} for a blank value.</li>
 *   <li>{@code shareWith} throws {@link NullPointerException} for {@code null} and
 *       {@link IllegalArgumentException} when sharing with the logger.</li>
 *   <li>{@code isSharedWith} and {@code isVisibleTo} throw {@link NullPointerException} for
 *       {@code null}.</li>
 * </ul>
 */
class ActivityTest {

  private static final LocalDateTime REGISTERED_AT = LocalDateTime.of(2026, 8, 15, 9, 30);

  private Person kari;
  private Person ola;
  private Activity activity;

  /** Creates a logger, another person and a valid activity used by several tests. */
  @BeforeEach
  void setUp() {
    kari = new Person("Kari");
    ola = new Person("Ola");
    activity = new Activity(kari, "Hike", "A nice hike.", REGISTERED_AT);
  }

  /** The constructor stores all values, which the getters return. */
  @Test
  void constructorStoresAllValues() {
    // Arrange
    LocalDateTime time = LocalDateTime.of(2026, 1, 2, 3, 4);

    // Act
    Activity created = new Activity(ola, "Bike ride", "Along the fjord.", time);

    // Assert
    assertSame(ola, created.getLogger());
    assertEquals("Bike ride", created.getTitle());
    assertEquals("Along the fjord.", created.getDescription());
    assertEquals(time, created.getRegisteredAt());
  }

  /** The constructor stores the text values trimmed. */
  @Test
  void constructorTrimsTextValues() {
    // Act
    Activity created = new Activity(ola, " Bike ride  ", "  Along the fjord. ", REGISTERED_AT);

    // Assert
    assertEquals("Bike ride", created.getTitle());
    assertEquals("Along the fjord.", created.getDescription());
  }

  /** A new activity is private and not shared with anyone. */
  @Test
  void constructorCreatesPrivateActivity() {
    // Act
    Activity created = new Activity(ola, "Walk", "Short walk.", REGISTERED_AT);

    // Assert
    assertFalse(created.isShared());
    assertTrue(created.getSharedWithPersons().isEmpty());
    assertTrue(created.getSharedWithGroups().isEmpty());
  }

  /** A registration time of now is accepted, since it is not in the future. */
  @Test
  void constructorAcceptsRegistrationTimeNow() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();

    // Act
    Activity created = new Activity(ola, "Walk", "Short walk.", now);

    // Assert
    assertEquals(now, created.getRegisteredAt());
  }

  /** The constructor throws NullPointerException when the logger is null. */
  @Test
  void constructorThrowsWhenLoggerIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> new Activity(null, "Hike", "A nice hike.", REGISTERED_AT));
  }

  /** The constructor throws NullPointerException when the title is null. */
  @Test
  void constructorThrowsWhenTitleIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> new Activity(kari, null, "A nice hike.", REGISTERED_AT));
  }

  /** The constructor throws NullPointerException when the description is null. */
  @Test
  void constructorThrowsWhenDescriptionIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> new Activity(kari, "Hike", null, REGISTERED_AT));
  }

  /** The constructor throws NullPointerException when the registration time is null. */
  @Test
  void constructorThrowsWhenRegisteredAtIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> new Activity(kari, "Hike", "A nice hike.", null));
  }

  /** The constructor throws IllegalArgumentException when the title is blank. */
  @Test
  void constructorThrowsWhenTitleIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> new Activity(kari, "", "A nice hike.", REGISTERED_AT));
  }

  /** The constructor throws IllegalArgumentException when the description is blank. */
  @Test
  void constructorThrowsWhenDescriptionIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> new Activity(kari, "Hike", " ", REGISTERED_AT));
  }

  /** The constructor throws IllegalArgumentException when the time is in the future. */
  @Test
  void constructorThrowsWhenRegisteredAtIsInTheFuture() {
    // Arrange
    LocalDateTime future = LocalDateTime.now().plusDays(1);

    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> new Activity(kari, "Hike", "A nice hike.", future));
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

  /** Sharing with a person makes the activity shared with that person. */
  @Test
  void shareWithPersonMakesActivityShared() {
    // Act
    activity.shareWith(ola);

    // Assert
    assertTrue(activity.isShared());
    assertTrue(activity.isSharedWith(ola));
    assertEquals(List.of(ola), activity.getSharedWithPersons());
  }

  /** Sharing with the same person twice keeps only one entry. */
  @Test
  void shareWithSamePersonTwiceHasNoEffect() {
    // Arrange
    activity.shareWith(ola);

    // Act
    activity.shareWith(new Person("ola"));

    // Assert
    assertEquals(1, activity.getSharedWithPersons().size());
  }

  /** {@code shareWith} throws NullPointerException when the person is null. */
  @Test
  void shareWithPersonThrowsWhenPersonIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> activity.shareWith((Person) null));
  }

  /** {@code shareWith} throws IllegalArgumentException when sharing with the logger. */
  @Test
  void shareWithPersonThrowsWhenPersonIsLogger() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> activity.shareWith(new Person("KARI")));
  }

  /** Sharing with a group makes the activity shared with the group's members. */
  @Test
  void shareWithGroupMakesActivitySharedWithMembers() {
    // Arrange
    Group group = new Group("Hikers");
    group.addMember(ola);

    // Act
    activity.shareWith(group);

    // Assert
    assertTrue(activity.isShared());
    assertTrue(activity.isSharedWith(ola));
    assertEquals(List.of(group), activity.getSharedWithGroups());
  }

  /** Sharing with the same group twice keeps only one entry. */
  @Test
  void shareWithSameGroupTwiceHasNoEffect() {
    // Arrange
    Group group = new Group("Hikers");
    activity.shareWith(group);

    // Act
    activity.shareWith(group);

    // Assert
    assertEquals(1, activity.getSharedWithGroups().size());
  }

  /** {@code shareWith} throws NullPointerException when the group is null. */
  @Test
  void shareWithGroupThrowsWhenGroupIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> activity.shareWith((Group) null));
  }

  /** {@code makePrivate} removes all persons and groups the activity is shared with. */
  @Test
  void makePrivateRemovesAllSharing() {
    // Arrange
    activity.shareWith(ola);
    activity.shareWith(new Group("Hikers"));

    // Act
    activity.makePrivate();

    // Assert
    assertFalse(activity.isShared());
    assertTrue(activity.getSharedWithPersons().isEmpty());
    assertTrue(activity.getSharedWithGroups().isEmpty());
  }

  /** A person who leaves a shared group no longer has the activity shared with them. */
  @Test
  void isSharedWithIsFalseAfterLeavingGroup() {
    // Arrange
    Group group = new Group("Hikers");
    group.addMember(ola);
    activity.shareWith(group);

    // Act
    group.removeMember(ola);

    // Assert
    assertFalse(activity.isSharedWith(ola));
  }

  /** A person added to a shared group later also has the activity shared with them. */
  @Test
  void isSharedWithIsTrueAfterJoiningGroup() {
    // Arrange
    Group group = new Group("Hikers");
    activity.shareWith(group);

    // Act
    group.addMember(ola);

    // Assert
    assertTrue(activity.isSharedWith(ola));
  }

  /** The logger is never shared with, even as a member of a shared group. */
  @Test
  void isSharedWithIsFalseForLogger() {
    // Arrange
    Group group = new Group("Hikers");
    group.addMember(kari);
    activity.shareWith(group);

    // Act
    boolean shared = activity.isSharedWith(kari);

    // Assert
    assertFalse(shared);
  }

  /** {@code isSharedWith} throws NullPointerException when the person is null. */
  @Test
  void isSharedWithThrowsWhenPersonIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> activity.isSharedWith(null));
  }

  /** The activity is visible to its logger and to persons it is shared with only. */
  @Test
  void isVisibleToLoggerAndSharedPersonsOnly() {
    // Arrange
    activity.shareWith(ola);

    // Act & Assert
    assertTrue(activity.isVisibleTo(kari));
    assertTrue(activity.isVisibleTo(ola));
    assertFalse(activity.isVisibleTo(new Person("Ingrid")));
  }

  /** {@code isVisibleTo} throws NullPointerException when the person is null. */
  @Test
  void isVisibleToThrowsWhenPersonIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> activity.isVisibleTo(null));
  }

  /** The shared persons and groups are returned sorted by name, ignoring case. */
  @Test
  void sharedListsAreSortedByName() {
    // Arrange
    Person bob = new Person("bob");
    Group zebras = new Group("Zebras");
    Group apes = new Group("apes");
    activity.shareWith(ola);
    activity.shareWith(bob);
    activity.shareWith(zebras);
    activity.shareWith(apes);

    // Act
    List<Person> persons = activity.getSharedWithPersons();
    List<Group> groups = activity.getSharedWithGroups();

    // Assert
    assertEquals(List.of(bob, ola), persons);
    assertEquals(List.of(apes, zebras), groups);
  }

  /** The returned share lists cannot be modified. */
  @Test
  void sharedListsAreUnmodifiable() {
    // Act & Assert
    assertThrows(UnsupportedOperationException.class,
        () -> activity.getSharedWithPersons().add(ola));
    assertThrows(UnsupportedOperationException.class,
        () -> activity.getSharedWithGroups().add(new Group("Hikers")));
  }
}
