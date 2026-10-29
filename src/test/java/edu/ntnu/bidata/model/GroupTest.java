package edu.ntnu.bidata.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Group}.
 *
 * <p>Planned positive tests:
 * <ul>
 *   <li>The constructor stores the name trimmed and creates an empty group.</li>
 *   <li>{@code addMember} adds a member; {@code hasMember} and {@code getMemberCount} reflect
 *       it.</li>
 *   <li>{@code removeMember} removes a member; the last member may be removed.</li>
 *   <li>{@code getMembers} is sorted by name ignoring case and cannot be modified.</li>
 * </ul>
 *
 * <p>Planned negative tests:
 * <ul>
 *   <li>The constructor throws {@link NullPointerException} for {@code null} and
 *       {@link IllegalArgumentException} for a blank name.</li>
 *   <li>{@code addMember} throws {@link NullPointerException} for {@code null} and
 *       {@link IllegalStateException} for an existing member.</li>
 *   <li>{@code removeMember} throws {@link NullPointerException} for {@code null} and
 *       {@link NoSuchElementException} for a person who is not a member.</li>
 *   <li>{@code hasMember} throws {@link NullPointerException} for {@code null}.</li>
 * </ul>
 */
class GroupTest {

  private Group group;
  private Person kari;

  /** Creates an empty group and a person used by several tests. */
  @BeforeEach
  void setUp() {
    group = new Group("Fjellgjengen");
    kari = new Person("Kari");
  }

  /** The constructor stores the name trimmed and the group has no members. */
  @Test
  void constructorCreatesEmptyGroupWithTrimmedName() {
    // Act
    Group created = new Group("  Hikers ");

    // Assert
    assertEquals("Hikers", created.getName());
    assertEquals(0, created.getMemberCount());
    assertTrue(created.getMembers().isEmpty());
  }

  /** The constructor throws NullPointerException when the name is null. */
  @Test
  void constructorThrowsWhenNameIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> new Group(null));
  }

  /** The constructor throws IllegalArgumentException when the name is blank. */
  @Test
  void constructorThrowsWhenNameIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> new Group(" "));
  }

  /** {@code addMember} adds the person as a member. */
  @Test
  void addMemberAddsPerson() {
    // Act
    group.addMember(kari);

    // Assert
    assertTrue(group.hasMember(kari));
    assertEquals(1, group.getMemberCount());
  }

  /** {@code addMember} throws NullPointerException when the person is null. */
  @Test
  void addMemberThrowsWhenPersonIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> group.addMember(null));
  }

  /** {@code addMember} throws IllegalStateException when the person is already a member. */
  @Test
  void addMemberThrowsWhenAlreadyMember() {
    // Arrange
    group.addMember(kari);

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> group.addMember(new Person("KARI")));
  }

  /** {@code removeMember} removes the member, also when it is the last one. */
  @Test
  void removeMemberRemovesLastMember() {
    // Arrange
    group.addMember(kari);

    // Act
    group.removeMember(kari);

    // Assert
    assertFalse(group.hasMember(kari));
    assertEquals(0, group.getMemberCount());
  }

  /** {@code removeMember} throws NullPointerException when the person is null. */
  @Test
  void removeMemberThrowsWhenPersonIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> group.removeMember(null));
  }

  /** {@code removeMember} throws NoSuchElementException when the person is not a member. */
  @Test
  void removeMemberThrowsWhenNotMember() {
    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> group.removeMember(kari));
  }

  /** {@code hasMember} is false for a person who is not a member. */
  @Test
  void hasMemberIsFalseForNonMember() {
    // Act
    boolean member = group.hasMember(kari);

    // Assert
    assertFalse(member);
  }

  /** {@code hasMember} throws NullPointerException when the person is null. */
  @Test
  void hasMemberThrowsWhenPersonIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> group.hasMember(null));
  }

  /** {@code getMembers} returns the members sorted by name, ignoring case. */
  @Test
  void getMembersIsSortedByName() {
    // Arrange
    Person ola = new Person("Ola");
    Person ingrid = new Person("ingrid");
    group.addMember(ola);
    group.addMember(kari);
    group.addMember(ingrid);

    // Act
    List<Person> members = group.getMembers();

    // Assert
    assertEquals(List.of(ingrid, kari, ola), members);
  }

  /** The list returned by {@code getMembers} cannot be modified. */
  @Test
  void getMembersIsUnmodifiable() {
    // Act & Assert
    assertThrows(UnsupportedOperationException.class, () -> group.getMembers().add(kari));
  }
}
