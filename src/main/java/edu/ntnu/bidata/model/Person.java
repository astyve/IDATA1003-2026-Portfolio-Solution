package edu.ntnu.bidata.model;

import java.util.Locale;
import java.util.Objects;

/**
 * A person who logs activities, called a logger.
 *
 * <p>Two persons are equal if their names are equal, ignoring case. The name is stored
 * trimmed and shown as it was first typed.
 */
public class Person {

  private final String name;

  /**
   * Creates a new person.
   *
   * @param name the name of the person, not blank
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is blank
   */
  public Person(String name) {
    Objects.requireNonNull(name, "Name cannot be null.");
    if (name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be blank.");
    }
    this.name = name.trim();
  }

  /**
   * Returns the name of the person.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Checks whether this person is equal to another object. Two persons are equal if their
   * names are equal, ignoring case.
   *
   * @param other the object to compare with
   * @return {@code true} if {@code other} is a person with the same name, ignoring case
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Person)) {
      return false;
    }
    Person person = (Person) other;
    return name.toLowerCase(Locale.ROOT).equals(person.name.toLowerCase(Locale.ROOT));
  }

  /**
   * Returns a hash code based on the name in lower case, consistent with
   * {@link #equals(Object)}.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return name.toLowerCase(Locale.ROOT).hashCode();
  }

  /**
   * Returns the name of the person.
   *
   * @return the name
   */
  @Override
  public String toString() {
    return name;
  }
}
