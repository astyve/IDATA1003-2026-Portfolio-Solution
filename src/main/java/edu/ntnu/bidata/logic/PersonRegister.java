package edu.ntnu.bidata.logic;

import edu.ntnu.bidata.model.Person;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The register of all loggers (persons) in the application.
 *
 * <p>The persons are kept in a {@link HashMap} keyed by the name in lower case, since the name
 * identifies a logger and lookup by name is the main operation. Names are unique, ignoring
 * case.
 */
public class PersonRegister {

  private final Map<String, Person> persons = new HashMap<>();

  /**
   * Creates an empty person register.
   */
  public PersonRegister() {
    // The register starts empty.
  }

  /**
   * Adds a person to the register.
   *
   * @param person the person to add
   * @throws NullPointerException if {@code person} is {@code null}
   * @throws IllegalStateException if a person with the same name is already registered
   */
  public void addPerson(Person person) {
    Objects.requireNonNull(person, "Person cannot be null.");
    String key = keyOf(person.getName());
    if (persons.containsKey(key)) {
      throw new IllegalStateException(
          "A logger named " + person.getName() + " is already registered.");
    }
    persons.put(key, person);
  }

  /**
   * Finds a person by name, ignoring case and surrounding spaces.
   *
   * @param name the name to search for
   * @return the person, or an empty {@link Optional} if there is none
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public Optional<Person> findByName(String name) {
    Objects.requireNonNull(name, "Name cannot be null.");
    return Optional.ofNullable(persons.get(keyOf(name)));
  }

  /**
   * Returns the person with the given name, registering a new person if there is none.
   *
   * @param name the name of the person, not blank
   * @return the existing or newly registered person
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is blank
   */
  public Person getOrCreate(String name) {
    Objects.requireNonNull(name, "Name cannot be null.");
    if (name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be blank.");
    }
    return persons.computeIfAbsent(keyOf(name), key -> new Person(name));
  }

  /**
   * Returns all persons, sorted by name ignoring case.
   *
   * @return an unmodifiable sorted list of all persons
   */
  public List<Person> getAllPersons() {
    return persons.values().stream()
        .sorted(Comparator.comparing(Person::getName, String.CASE_INSENSITIVE_ORDER))
        .toList();
  }

  /**
   * Returns the number of persons in the register.
   *
   * @return the number of persons
   */
  public int size() {
    return persons.size();
  }

  private static String keyOf(String name) {
    return name.trim().toLowerCase(Locale.ROOT);
  }
}
