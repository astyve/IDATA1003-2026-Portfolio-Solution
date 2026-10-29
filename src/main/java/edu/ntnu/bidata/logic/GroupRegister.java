package edu.ntnu.bidata.logic;

import edu.ntnu.bidata.model.Group;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The register of all groups of loggers in the application.
 *
 * <p>The groups are kept in a {@link HashMap} keyed by the name in lower case, since lookup by
 * name is the main operation. Group names are unique, ignoring case.
 */
public class GroupRegister {

  private final Map<String, Group> groups = new HashMap<>();

  /**
   * Creates an empty group register.
   */
  public GroupRegister() {
    // The register starts empty.
  }

  /**
   * Adds a group to the register.
   *
   * @param group the group to add
   * @throws NullPointerException if {@code group} is {@code null}
   * @throws IllegalStateException if a group with the same name is already registered
   */
  public void addGroup(Group group) {
    Objects.requireNonNull(group, "Group cannot be null.");
    String key = keyOf(group.getName());
    if (groups.containsKey(key)) {
      throw new IllegalStateException(
          "A group named " + group.getName() + " is already registered.");
    }
    groups.put(key, group);
  }

  /**
   * Finds a group by name, ignoring case and surrounding spaces.
   *
   * @param name the name to search for
   * @return the group, or an empty {@link Optional} if there is none
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public Optional<Group> findByName(String name) {
    Objects.requireNonNull(name, "Group name cannot be null.");
    return Optional.ofNullable(groups.get(keyOf(name)));
  }

  /**
   * Returns all groups, sorted by name ignoring case.
   *
   * @return an unmodifiable sorted list of all groups
   */
  public List<Group> getAllGroups() {
    return groups.values().stream()
        .sorted(Comparator.comparing(Group::getName, String.CASE_INSENSITIVE_ORDER))
        .toList();
  }

  /**
   * Returns the number of groups in the register.
   *
   * @return the number of groups
   */
  public int size() {
    return groups.size();
  }

  private static String keyOf(String name) {
    return name.trim().toLowerCase(Locale.ROOT);
  }
}
