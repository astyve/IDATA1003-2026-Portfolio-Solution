package edu.ntnu.bidata.ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Reads and validates input from the user.
 *
 * <p>Each method prints a prompt, reads one line and asks again until the input is valid, so
 * invalid input never reaches the rest of the application. If the input ends, the
 * {@link NoSuchElementException} from the {@link Scanner} is passed on to the caller.
 */
public class InputReader {

  private final Scanner scanner;

  /**
   * Creates an input reader that reads from the given scanner.
   *
   * @param scanner the scanner to read lines from
   * @throws NullPointerException if {@code scanner} is {@code null}
   */
  public InputReader(Scanner scanner) {
    this.scanner = Objects.requireNonNull(scanner, "Scanner cannot be null.");
  }

  /**
   * Reads a text that is not blank.
   *
   * @param prompt the prompt to show
   * @return the text, trimmed
   * @throws NoSuchElementException if the input ends
   */
  public String readText(String prompt) {
    while (true) {
      String line = readLine(prompt);
      if (!line.isEmpty()) {
        return line;
      }
      System.out.println("Input cannot be empty. Please try again.");
    }
  }

  /**
   * Reads a whole number within a range.
   *
   * @param prompt the prompt to show
   * @param min the smallest number allowed
   * @param max the largest number allowed
   * @return the number
   * @throws NoSuchElementException if the input ends
   */
  public int readInt(String prompt, int min, int max) {
    while (true) {
      String line = readLine(prompt);
      try {
        int number = Integer.parseInt(line);
        if (number >= min && number <= max) {
          return number;
        }
        System.out.println("Please enter a number between " + min + " and " + max + ".");
      } catch (NumberFormatException e) {
        System.out.println("Please enter a whole number.");
      }
    }
  }

  /**
   * Reads a decimal number that is at least a given minimum. Both a point and a comma are
   * accepted as the decimal separator.
   *
   * @param prompt the prompt to show
   * @param min the smallest number allowed
   * @return the number
   * @throws NoSuchElementException if the input ends
   */
  public double readDouble(String prompt, double min) {
    while (true) {
      String line = readLine(prompt).replace(',', '.');
      try {
        double number = Double.parseDouble(line);
        if (!Double.isFinite(number)) {
          System.out.println("Please enter a number, e.g. 7.5.");
        } else if (number < min) {
          System.out.println("Please enter a number of at least " + formatNumber(min) + ".");
        } else {
          return number;
        }
      } catch (NumberFormatException e) {
        System.out.println("Please enter a number, e.g. 7.5.");
      }
    }
  }

  /**
   * Reads a date in the format {@code yyyy-MM-dd}.
   *
   * @param prompt the prompt to show
   * @return the date
   * @throws NoSuchElementException if the input ends
   */
  public LocalDate readDate(String prompt) {
    while (true) {
      String line = readLine(prompt);
      try {
        return LocalDate.parse(line);
      } catch (DateTimeParseException e) {
        System.out.println("Invalid date. Use the format yyyy-MM-dd, e.g. 2026-10-29.");
      }
    }
  }

  /**
   * Reads a yes or no answer: {@code y}, {@code yes}, {@code n} or {@code no} in any case.
   *
   * @param prompt the prompt to show
   * @return {@code true} for yes, {@code false} for no
   * @throws NoSuchElementException if the input ends
   */
  public boolean readYesNo(String prompt) {
    while (true) {
      String answer = readLine(prompt).toLowerCase(Locale.ROOT);
      if (answer.equals("y") || answer.equals("yes")) {
        return true;
      }
      if (answer.equals("n") || answer.equals("no")) {
        return false;
      }
      System.out.println("Please answer y or n.");
    }
  }

  private String readLine(String prompt) {
    System.out.print(prompt);
    return scanner.nextLine().trim();
  }

  private static String formatNumber(double number) {
    if (number == Math.rint(number)) {
      return String.valueOf((long) number);
    }
    return String.valueOf(number);
  }
}
