package edu.ntnu.bidata.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.bidata.model.Group;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link GroupRegister}.
 *
 * <p>Planned positive tests:
 * <ul>
 *   <li>A new register is empty.</li>
 *   <li>{@code addGroup} registers a group.</li>
 *   <li>{@code findByName} finds a group ignoring case and spaces, and returns empty for an
 *       unknown name.</li>
 *   <li>{@code getAllGroups} is sorted by name ignoring case and cannot be modified.</li>
 * </ul>
 *
 * <p>Planned negative tests:
 * <ul>
 *   <li>{@code addGroup} throws {@link NullPointerException} for {@code null} and
 *       {@link IllegalStateException} for a name that is already registered.</li>
 *   <li>{@code findByName} throws {@link NullPointerException} for {@code null}.</li>
 * </ul>
 */
class GroupRegisterTest {

  private GroupRegister register;

  /** Creates an empty register. */
  @BeforeEach
  void setUp() {
    register = new GroupRegister();
  }

  /** A new register has no groups. */
  @Test
  void newRegisterIsEmpty() {
    // Act & Assert
    assertEquals(0, register.size());
    assertTrue(register.getAllGroups().isEmpty());
  }

  /** {@code addGroup} registers the group. */
  @Test
  void addGroupRegistersGroup() {
    // Arrange
    Group hikers = new Group("Hikers");

    // Act
    register.addGroup(hikers);

    // Assert
    assertEquals(1, register.size());
    assertEquals(List.of(hikers), register.getAllGroups());
  }

  /** {@code addGroup} throws NullPointerException when the group is null. */
  @Test
  void addGroupThrowsWhenGroupIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.addGroup(null));
  }

  /** {@code addGroup} throws IllegalStateException when the name is already registered. */
  @Test
  void addGroupThrowsWhenNameExists() {
    // Arrange
    register.addGroup(new Group("Hikers"));

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> register.addGroup(new Group("HIKERS")));
  }

  /** {@code findByName} finds the group ignoring case and surrounding spaces. */
  @Test
  void findByNameIgnoresCaseAndSpaces() {
    // Arrange
    Group hikers = new Group("Hikers");
    register.addGroup(hikers);

    // Act
    Optional<Group> found = register.findByName(" hIKERS ");

    // Assert
    assertSame(hikers, found.orElseThrow());
  }

  /** {@code findByName} returns an empty Optional for an unknown name. */
  @Test
  void findByNameReturnsEmptyForUnknownName() {
    // Arrange
    register.addGroup(new Group("Hikers"));

    // Act
    Optional<Group> found = register.findByName("Bikers");

    // Assert
    assertTrue(found.isEmpty());
  }

  /** {@code findByName} throws NullPointerException when the name is null. */
  @Test
  void findByNameThrowsWhenNameIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.findByName(null));
  }

  /** {@code getAllGroups} returns the groups sorted by name, ignoring case. */
  @Test
  void getAllGroupsIsSortedByName() {
    // Arrange
    Group zebras = new Group("Zebras");
    Group apes = new Group("apes");
    Group hikers = new Group("Hikers");
    register.addGroup(zebras);
    register.addGroup(apes);
    register.addGroup(hikers);

    // Act
    List<Group> groups = register.getAllGroups();

    // Assert
    assertEquals(List.of(apes, hikers, zebras), groups);
  }

  /** The list from {@code getAllGroups} cannot be modified. */
  @Test
  void getAllGroupsIsUnmodifiable() {
    // Act & Assert
    assertThrows(UnsupportedOperationException.class,
        () -> register.getAllGroups().add(new Group("Hikers")));
  }
}
