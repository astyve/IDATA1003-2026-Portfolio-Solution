package edu.ntnu.bidata.model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * A named group of loggers, such as "Fjellgjengen".
 *
 * <p>A group starts without members; members can be added and removed afterwards, and a
 * group may become empty again. Activities shared with a group are visible to its current
 * members. Groups are compared by identity; the group register keeps the names unique.
 */
public class Group {

  private final String name;
  private final Set<Person> members = new HashSet<>();

  /**
   * Creates a new group without members.
   *
   * @param name the name of the group, not blank
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is blank
   */
  public Group(String name) {
    Objects.requireNonNull(name, "Group name cannot be null.");
    if (name.isBlank()) {
      throw new IllegalArgumentException("Group name cannot be blank.");
    }
    this.name = name.trim();
  }

  /**
   * Returns the name of the group.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Adds a person as a member of the group.
   *
   * @param person the person to add
   * @throws NullPointerException if {@code person} is {@code null}
   * @throws IllegalStateException if the person is already a member
   */
  public void addMember(Person person) {
    Objects.requireNonNull(person, "Person cannot be null.");
    if (members.contains(person)) {
      throw new IllegalStateException(person + " is already a member of " + name + ".");
    }
    members.add(person);
  }

  /**
   * Removes a member from the group.
   *
   * @param person the member to remove
   * @throws NullPointerException if {@code person} is {@code null}
   * @throws NoSuchElementException if the person is not a member
   */
  public void removeMember(Person person) {
    Objects.requireNonNull(person, "Person cannot be null.");
    if (!members.remove(person)) {
      throw new NoSuchElementException(person + " is not a member of " + name + ".");
    }
  }

  /**
   * Checks whether a person is a member of the group.
   *
   * @param person the person to check
   * @return {@code true} if the person is a member
   * @throws NullPointerException if {@code person} is {@code null}
   */
  public boolean hasMember(Person person) {
    Objects.requireNonNull(person, "Person cannot be null.");
    return members.contains(person);
  }

  /**
   * Returns the members of the group, sorted by name ignoring case.
   *
   * @return an unmodifiable list of the members
   */
  public List<Person> getMembers() {
    return members.stream()
        .sorted(Comparator.comparing(Person::getName, String.CASE_INSENSITIVE_ORDER))
        .toList();
  }

  /**
   * Returns the number of members in the group.
   *
   * @return the number of members
   */
  public int getMemberCount() {
    return members.size();
  }
}
