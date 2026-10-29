package edu.ntnu.bidata.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ActivityType}.
 *
 * <p>Planned positive tests:
 * <ul>
 *   <li>Each type has the display name shown to the user.</li>
 * </ul>
 *
 * <p>Planned negative tests: none, since {@code getDisplayName} takes no arguments and
 * cannot fail.
 */
class ActivityTypeTest {

  /** Each activity type returns its display name. */
  @Test
  void getDisplayNameReturnsNameShownToUser() {
    // Act & Assert
    assertEquals("Mountain hike", ActivityType.MOUNTAIN_HIKE.getDisplayName());
    assertEquals("Bike ride", ActivityType.BIKE_RIDE.getDisplayName());
    assertEquals("City walk", ActivityType.CITY_WALK.getDisplayName());
    assertEquals("Ski trip", ActivityType.SKI_TRIP.getDisplayName());
    assertEquals("Other", ActivityType.OTHER.getDisplayName());
  }
}
