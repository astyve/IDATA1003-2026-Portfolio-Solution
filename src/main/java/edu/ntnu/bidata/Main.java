package edu.ntnu.bidata;

import edu.ntnu.bidata.ui.ActivityLoggerUi;

/**
 * Entry point for the Activity Logger application.
 */
public class Main {

  /**
   * Starts the application by initialising and starting the user interface.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    ActivityLoggerUi ui = new ActivityLoggerUi();
    ui.init();
    ui.start();
  }
}
