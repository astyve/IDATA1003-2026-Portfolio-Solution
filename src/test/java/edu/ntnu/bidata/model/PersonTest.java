package edu.ntnu.bidata.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Person}.
 *
 * <p>Planned positive tests:
 * <ul>
 *   <li>The constructor stores the name trimmed.</li>
 *   <li>Persons with the same name, ignoring case, are equal and have the same hash code.</li>
 *   <li>Persons with different names are not equal; a person is not equal to {@code null}
 *       or another type.</li>
 *   <li>{@code toString} returns the name.</li>
 * </ul>
 *
 * <p>Planned negative tests:
 * <ul>
 *   <li>The constructor throws {@link NullPointerException} for a {@code null} name.</li>
 *   <li>The constructor throws {@link IllegalArgumentException} for a blank name.</li>
 * </ul>
 */
class PersonTest {

  /** The constructor stores the name trimmed. */
  @Test
  void constructorStoresTrimmedName() {
    // Act
    Person person = new Person("  Kari Nordmann ");

    // Assert
    assertEquals("Kari Nordmann", person.getName());
  }

  /** The constructor throws NullPointerException when the name is null. */
  @Test
  void constructorThrowsWhenNameIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> new Person(null));
  }

  /** The constructor throws IllegalArgumentException when the name is blank. */
  @Test
  void constructorThrowsWhenNameIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> new Person("   "));
  }

  /** Persons with the same name, ignoring case, are equal with equal hash codes. */
  @Test
  void equalsIgnoresCase() {
    // Arrange
    Person first = new Person("Kari Nordmann");
    Person second = new Person("kari NORDMANN");

    // Act & Assert
    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }

  /** A person is equal to itself. */
  @Test
  void equalsIsTrueForSameObject() {
    // Arrange
    Person person = new Person("Kari");

    // Act & Assert
    assertEquals(person, person);
  }

  /** Persons with different names are not equal. */
  @Test
  void equalsIsFalseForDifferentNames() {
    // Act & Assert
    assertNotEquals(new Person("Kari"), new Person("Ola"));
  }

  /** A person is not equal to null or to an object of another type. */
  @Test
  void equalsIsFalseForNullAndOtherType() {
    // Arrange
    Person person = new Person("Kari");

    // Act & Assert
    assertNotEquals(null, person);
    assertNotEquals("Kari", person);
  }

  /** {@code toString} returns the name. */
  @Test
  void toStringReturnsName() {
    // Act
    String text = new Person("Kari Nordmann").toString();

    // Assert
    assertEquals("Kari Nordmann", text);
  }
}
