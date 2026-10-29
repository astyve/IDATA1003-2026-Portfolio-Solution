package edu.ntnu.bidata.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.bidata.model.Person;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PersonRegister}.
 *
 * <p>Planned positive tests:
 * <ul>
 *   <li>A new register is empty.</li>
 *   <li>{@code addPerson} registers a person.</li>
 *   <li>{@code findByName} finds a person ignoring case and spaces, and returns empty for an
 *       unknown or blank name.</li>
 *   <li>{@code getOrCreate} returns an existing person, or creates and registers a new one.</li>
 *   <li>{@code getAllPersons} is sorted by name ignoring case and cannot be modified.</li>
 * </ul>
 *
 * <p>Planned negative tests:
 * <ul>
 *   <li>{@code addPerson} throws {@link NullPointerException} for {@code null} and
 *       {@link IllegalStateException} for a name that is already registered.</li>
 *   <li>{@code findByName} throws {@link NullPointerException} for {@code null}.</li>
 *   <li>{@code getOrCreate} throws {@link NullPointerException} for {@code null} and
 *       {@link IllegalArgumentException} for a blank name.</li>
 * </ul>
 */
class PersonRegisterTest {

  private PersonRegister register;

  /** Creates an empty register. */
  @BeforeEach
  void setUp() {
    register = new PersonRegister();
  }

  /** A new register has no persons. */
  @Test
  void newRegisterIsEmpty() {
    // Act & Assert
    assertEquals(0, register.size());
    assertTrue(register.getAllPersons().isEmpty());
  }

  /** {@code addPerson} registers the person. */
  @Test
  void addPersonRegistersPerson() {
    // Arrange
    Person kari = new Person("Kari");

    // Act
    register.addPerson(kari);

    // Assert
    assertEquals(1, register.size());
    assertEquals(List.of(kari), register.getAllPersons());
  }

  /** {@code addPerson} throws NullPointerException when the person is null. */
  @Test
  void addPersonThrowsWhenPersonIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.addPerson(null));
  }

  /** {@code addPerson} throws IllegalStateException when the name is already registered. */
  @Test
  void addPersonThrowsWhenNameExists() {
    // Arrange
    register.addPerson(new Person("Kari"));

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> register.addPerson(new Person("KARI")));
  }

  /** {@code findByName} finds the person ignoring case and surrounding spaces. */
  @Test
  void findByNameIgnoresCaseAndSpaces() {
    // Arrange
    Person kari = new Person("Kari Nordmann");
    register.addPerson(kari);

    // Act
    Optional<Person> found = register.findByName("  kari NORDMANN ");

    // Assert
    assertSame(kari, found.orElseThrow());
  }

  /** {@code findByName} returns an empty Optional for an unknown or blank name. */
  @Test
  void findByNameReturnsEmptyForUnknownOrBlankName() {
    // Arrange
    register.addPerson(new Person("Kari"));

    // Act & Assert
    assertTrue(register.findByName("Ola").isEmpty());
    assertTrue(register.findByName("  ").isEmpty());
  }

  /** {@code findByName} throws NullPointerException when the name is null. */
  @Test
  void findByNameThrowsWhenNameIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.findByName(null));
  }

  /** {@code getOrCreate} returns the existing person when the name is registered. */
  @Test
  void getOrCreateReturnsExistingPerson() {
    // Arrange
    Person kari = new Person("Kari");
    register.addPerson(kari);

    // Act
    Person result = register.getOrCreate("kari");

    // Assert
    assertSame(kari, result);
    assertEquals(1, register.size());
  }

  /** {@code getOrCreate} creates and registers a new person when the name is unknown. */
  @Test
  void getOrCreateCreatesAndRegistersNewPerson() {
    // Act
    Person result = register.getOrCreate(" Ola Hansen ");

    // Assert
    assertEquals("Ola Hansen", result.getName());
    assertSame(result, register.findByName("ola hansen").orElseThrow());
  }

  /** {@code getOrCreate} throws NullPointerException when the name is null. */
  @Test
  void getOrCreateThrowsWhenNameIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> register.getOrCreate(null));
  }

  /** {@code getOrCreate} throws IllegalArgumentException when the name is blank. */
  @Test
  void getOrCreateThrowsWhenNameIsBlank() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> register.getOrCreate(""));
  }

  /** {@code getAllPersons} returns the persons sorted by name, ignoring case. */
  @Test
  void getAllPersonsIsSortedByName() {
    // Arrange
    Person ola = register.getOrCreate("Ola");
    Person anna = register.getOrCreate("anna");
    Person kari = register.getOrCreate("Kari");

    // Act
    List<Person> persons = register.getAllPersons();

    // Assert
    assertEquals(List.of(anna, kari, ola), persons);
  }

  /** The list from {@code getAllPersons} cannot be modified. */
  @Test
  void getAllPersonsIsUnmodifiable() {
    // Act & Assert
    assertThrows(UnsupportedOperationException.class,
        () -> register.getAllPersons().add(new Person("Kari")));
  }
}
