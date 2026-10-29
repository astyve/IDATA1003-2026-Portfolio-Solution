package edu.ntnu.bidata.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.bidata.model.Activity;
import edu.ntnu.bidata.model.Group;
import edu.ntnu.bidata.model.Person;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ActivityRegister}.
 *
 * <p>Planned positive tests:
 * <ul>
 *   <li>A new register is empty.</li>
 *   <li>{@code addActivity} adds; {@code size} and {@code isEmpty} reflect it.</li>
 *   <li>{@code removeActivity} removes.</li>
 *   <li>{@code getActivitiesSorted} returns all activities oldest first, as an unmodifiable
 *       list.</li>
 *   <li>{@code findByDate} with no, one and several hits.</li>
 *   <li>{@code findBetween} includes both end dates; also for a one-day period.</li>
 *   <li>{@code findByLogger} returns only that logger's activities.</li>
 *   <li>{@code findByKeyword} matches title and description, ignoring case and surrounding
 *       spaces.</li>
 *   <li>{@code findSharedWith} includes direct and group sharing, not own activities.</li>
 *   <li>{@code countPerLogger} counts per logger, ordered by name, and is empty for an empty
 *       register.</li>
 * </ul>
 *
 * <p>Planned negative tests:
 * <ul>
 *   <li>{@code addActivity} throws {@link NullPointerException} for {@code null} and
 *       {@link IllegalStateException} for a duplicate.</li>
 *   <li>{@code removeActivity} throws {@link NullPointerException} for {@code null} and
 *       {@link NoSuchElementException} for an activity not registered.</li>
 *   <li>{@code findByDate}, {@code findByLogger} and {@code findSharedWith} throw
 *       {@link NullPointerException} for {@code null}.</li>
 *   <li>{@code findBetween} throws {@link NullPointerException} for a {@code null} date and
 *       {@link IllegalArgumentException} when from is after to.</li>
 *   <li>{@code findByKeyword} throws {@link NullPointerException} for {@code null} and
 *       {@link IllegalArgumentException} for a blank keyword.</li>
 * </ul>
 */
class ActivityRegisterTest {

  private ActivityRegister register;
  private Person kari;
  private Person ola;
  private Activity early;
  private Activity middle;
  private Activity late;

  /** Creates an empty register, two loggers and three activities not yet registered. */
  @BeforeEach
  void setUp() {
    register = new ActivityRegister();
    kari = new Person("Kari");
    ola = new Person("Ola");
    early = new Activity(kari, "Morning hike", "Up the hill before breakfast.",
        LocalDateTime.of(2026, 5, 1, 7, 0));
    middle = new Activity(ola, "Ridge walk", "A windy RIDGE.",
        LocalDateTime.of(2026, 5, 1, 18, 0));
    late = new Activity(kari, "Bike ride", "Along the fjord.",
        LocalDateTime.of(2026, 6, 10, 12, 0));
  }

  /** A new register is empty. */
  @Test
  void newRegisterIsEmpty() {
    // Act & Assert
    assertTrue(register.isEmpty());
    assertEquals(0, register.size());
  }

  /** {@code addActivity} adds the activity to the register. */
  @Test
  void addActivityAddsActivity() {
    // Act
    register.addActivity(early);

    // Assert
    assertFalse(register.isEmpty());
    assertEquals(1, register.size());
    assertEquals(List.of(early), register.getActivitiesSorted());
  }

  /** {@code addActivity} throws NullPointerException when the activity is null. */
  @Test
  void addActivityThrowsWhenActivityIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.addActivity(null));
  }

  /** {@code addActivity} throws IllegalStateException when the activity is already added. */
  @Test
  void addActivityThrowsWhenActivityIsDuplicate() {
    // Arrange
    register.addActivity(early);

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> register.addActivity(early));
  }

  /** {@code removeActivity} removes the activity from the register. */
  @Test
  void removeActivityRemovesActivity() {
    // Arrange
    register.addActivity(early);
    register.addActivity(late);

    // Act
    register.removeActivity(early);

    // Assert
    assertEquals(List.of(late), register.getActivitiesSorted());
  }

  /** {@code removeActivity} throws NullPointerException when the activity is null. */
  @Test
  void removeActivityThrowsWhenActivityIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.removeActivity(null));
  }

  /** {@code removeActivity} throws NoSuchElementException when the activity is not there. */
  @Test
  void removeActivityThrowsWhenActivityIsNotRegistered() {
    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> register.removeActivity(early));
  }

  /** {@code getActivitiesSorted} returns all activities sorted oldest first. */
  @Test
  void getActivitiesSortedReturnsOldestFirst() {
    // Arrange
    register.addActivity(late);
    register.addActivity(early);
    register.addActivity(middle);

    // Act
    List<Activity> sorted = register.getActivitiesSorted();

    // Assert
    assertEquals(List.of(early, middle, late), sorted);
  }

  /** The list from {@code getActivitiesSorted} cannot be modified. */
  @Test
  void getActivitiesSortedIsUnmodifiable() {
    // Arrange
    register.addActivity(early);

    // Act & Assert
    assertThrows(UnsupportedOperationException.class,
        () -> register.getActivitiesSorted().add(late));
  }

  /** {@code findByDate} returns all activities on the date, sorted. */
  @Test
  void findByDateReturnsAllActivitiesOnDate() {
    // Arrange
    register.addActivity(middle);
    register.addActivity(early);
    register.addActivity(late);

    // Act
    List<Activity> found = register.findByDate(LocalDate.of(2026, 5, 1));

    // Assert
    assertEquals(List.of(early, middle), found);
  }

  /** {@code findByDate} returns one activity when only one matches. */
  @Test
  void findByDateReturnsSingleMatch() {
    // Arrange
    register.addActivity(early);
    register.addActivity(late);

    // Act
    List<Activity> found = register.findByDate(LocalDate.of(2026, 6, 10));

    // Assert
    assertEquals(List.of(late), found);
  }

  /** {@code findByDate} returns an empty list when nothing matches. */
  @Test
  void findByDateReturnsEmptyListWhenNoMatch() {
    // Arrange
    register.addActivity(early);

    // Act
    List<Activity> found = register.findByDate(LocalDate.of(2026, 1, 1));

    // Assert
    assertTrue(found.isEmpty());
  }

  /** {@code findByDate} throws NullPointerException when the date is null. */
  @Test
  void findByDateThrowsWhenDateIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.findByDate(null));
  }

  /** {@code findBetween} includes activities on both the from and the to date. */
  @Test
  void findBetweenIncludesBothEndDates() {
    // Arrange
    register.addActivity(early);
    register.addActivity(middle);
    register.addActivity(late);

    // Act
    List<Activity> found = register.findBetween(LocalDate.of(2026, 5, 1),
        LocalDate.of(2026, 6, 10));

    // Assert
    assertEquals(List.of(early, middle, late), found);
  }

  /** {@code findBetween} with the same from and to date finds that day only. */
  @Test
  void findBetweenWithSameDateFindsThatDay() {
    // Arrange
    register.addActivity(early);
    register.addActivity(late);

    // Act
    List<Activity> found = register.findBetween(LocalDate.of(2026, 6, 10),
        LocalDate.of(2026, 6, 10));

    // Assert
    assertEquals(List.of(late), found);
  }

  /** {@code findBetween} throws NullPointerException when the from date is null. */
  @Test
  void findBetweenThrowsWhenFromIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> register.findBetween(null, LocalDate.of(2026, 1, 1)));
  }

  /** {@code findBetween} throws NullPointerException when the to date is null. */
  @Test
  void findBetweenThrowsWhenToIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> register.findBetween(LocalDate.of(2026, 1, 1), null));
  }

  /** {@code findBetween} throws IllegalArgumentException when from is after to. */
  @Test
  void findBetweenThrowsWhenFromIsAfterTo() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> register.findBetween(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 1)));
  }

  /** {@code findByLogger} returns only the activities of that logger. */
  @Test
  void findByLoggerReturnsLoggersActivities() {
    // Arrange
    register.addActivity(early);
    register.addActivity(middle);
    register.addActivity(late);

    // Act
    List<Activity> found = register.findByLogger(new Person("kari"));

    // Assert
    assertEquals(List.of(early, late), found);
  }

  /** {@code findByLogger} throws NullPointerException when the logger is null. */
  @Test
  void findByLoggerThrowsWhenLoggerIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.findByLogger(null));
  }

  /** {@code findByKeyword} matches the title, ignoring case and surrounding spaces. */
  @Test
  void findByKeywordMatchesTitle() {
    // Arrange
    register.addActivity(early);
    register.addActivity(late);

    // Act
    List<Activity> found = register.findByKeyword("  BIKE ");

    // Assert
    assertEquals(List.of(late), found);
  }

  /** {@code findByKeyword} matches the description, ignoring case. */
  @Test
  void findByKeywordMatchesDescription() {
    // Arrange
    register.addActivity(early);
    register.addActivity(late);

    // Act
    List<Activity> found = register.findByKeyword("breakfast");

    // Assert
    assertEquals(List.of(early), found);
  }

  /** {@code findByKeyword} returns an empty list when nothing matches. */
  @Test
  void findByKeywordReturnsEmptyListWhenNoMatch() {
    // Arrange
    register.addActivity(early);

    // Act
    List<Activity> found = register.findByKeyword("glacier");

    // Assert
    assertTrue(found.isEmpty());
  }

  /** {@code findByKeyword} throws NullPointerException when the keyword is null. */
  @Test
  void findByKeywordThrowsWhenKeywordIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.findByKeyword(null));
  }

  /** {@code findByKeyword} throws IllegalArgumentException when the keyword is blank. */
  @Test
  void findByKeywordThrowsWhenKeywordIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> register.findByKeyword(" "));
  }

  /** {@code findSharedWith} finds direct and group sharing but not own activities. */
  @Test
  void findSharedWithFindsDirectAndGroupSharing() {
    // Arrange
    Person ingrid = new Person("Ingrid");
    Group group = new Group("Hikers");
    group.addMember(ingrid);
    early.shareWith(ingrid);
    late.shareWith(group);
    Activity own = new Activity(ingrid, "Own trip", "My own.",
        LocalDateTime.of(2026, 4, 1, 10, 0));
    register.addActivity(early);
    register.addActivity(middle);
    register.addActivity(late);
    register.addActivity(own);

    // Act
    List<Activity> found = register.findSharedWith(ingrid);

    // Assert
    assertEquals(List.of(early, late), found);
  }

  /** {@code findSharedWith} throws NullPointerException when the person is null. */
  @Test
  void findSharedWithThrowsWhenPersonIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.findSharedWith(null));
  }

  /** {@code countPerLogger} counts activities per logger, ordered by name ignoring case. */
  @Test
  void countPerLoggerCountsAndOrdersByName() {
    // Arrange
    Person anna = new Person("anna");
    register.addActivity(late);
    register.addActivity(middle);
    register.addActivity(early);
    register.addActivity(new Activity(anna, "Walk", "Short walk.",
        LocalDateTime.of(2026, 3, 3, 3, 3)));

    // Act
    Map<Person, Long> counts = register.countPerLogger();

    // Assert
    assertEquals(List.of(anna, kari, ola), List.copyOf(counts.keySet()));
    assertEquals(1L, counts.get(anna));
    assertEquals(2L, counts.get(kari));
    assertEquals(1L, counts.get(ola));
  }

  /** {@code countPerLogger} returns an empty, unmodifiable map for an empty register. */
  @Test
  void countPerLoggerIsEmptyAndUnmodifiableForEmptyRegister() {
    // Act
    Map<Person, Long> counts = register.countPerLogger();

    // Assert
    assertTrue(counts.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> counts.put(kari, 1L));
  }
}
