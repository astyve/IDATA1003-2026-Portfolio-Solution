package edu.ntnu.bidata.model;

/**
 * The type of an activity, such as a mountain hike or a bike ride.
 */
public enum ActivityType {
  MOUNTAIN_HIKE("Mountain hike"),
  BIKE_RIDE("Bike ride"),
  CITY_WALK("City walk"),
  SKI_TRIP("Ski trip"),
  OTHER("Other");

  private final String displayName;

  ActivityType(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Returns the name of the type as shown to the user, e.g. "Mountain hike".
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }
}
