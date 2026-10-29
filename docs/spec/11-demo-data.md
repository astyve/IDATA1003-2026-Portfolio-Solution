# 11 – Demo Data

Data created in code by `ActivityLoggerUi.init()`. Used for the scripted demos
in Parts 1–2 and as starting data for the menu in Part 3. Tests must **not**
depend on this data; they create their own.

## Loggers (Part 2+)

| # | Name |
|---|------|
| P1 | Kari Nordmann |
| P2 | Ola Hansen |
| P3 | Ingrid Berg |
| P4 | Lars Haugen |

In Part 1 the same names are used as plain strings.

## Groups (Part 2+)

| Group | Members |
|-------|---------|
| Fjellgjengen | Kari Nordmann, Ola Hansen, Ingrid Berg |
| Sykkelklubben | Ola Hansen, Lars Haugen |

## Activities

| # | Logger | Title | Description | Registered | Shared with (Part 2+) | Type (Part 3) | Distance km (Part 3) |
|---|--------|-------|-------------|------------|-----------------------|---------------|----------------------|
| A1 | Kari Nordmann | Trip to Preikestolen | Classic hike from Preikestolhytta. Clear weather and a great view over Lysefjorden. | 2026-08-15 09:30 | group Fjellgjengen | MOUNTAIN_HIKE | 7.6 |
| A2 | Ola Hansen | Romsdalseggen ridge | Long ridge walk from Vengedalen to Åndalsnes. Steep in places, so take care in wet weather. | 2026-08-22 08:00 | – (private) | MOUNTAIN_HIKE | 10.3 |
| A3 | Ingrid Berg | Sunset at Aksla | Evening walk up the 418 steps from the city park in Ålesund. | 2026-09-05 19:15 | Kari Nordmann, Lars Haugen | CITY_WALK | 3.2 |
| A4 | Lars Haugen | Galdhøpiggen summit | Guided glacier crossing from Juvasshytta to the highest point in Norway. | 2026-09-12 07:45 | – (private) | MOUNTAIN_HIKE | 11.0 |
| A5 | Lars Haugen | Rallarvegen | Bike ride from Haugastøl down to Flåm along the old navvy road. | 2026-07-20 10:00 | group Sykkelklubben | BIKE_RIDE | 82.0 |

- Parts 1 and 2 use **A1–A4** (the PDF asks for 3–4). Part 3 adds **A5**.
- Part 2 requirement "at least one private and one shared": A2, A4 private;
  A1 shared with a group, A3 shared with selected persons.

## Expected facts (useful for checking the demo output)

| Question | Answer |
|----------|--------|
| Sorted order (Parts 1–2) | A1, A2, A3, A4 |
| Sorted order (Part 3) | A5, A1, A2, A3, A4 |
| Activities on 2026-08-22 | A2 |
| Activities 2026-08-01 … 2026-08-31 | A1, A2 |
| Activities by Lars Haugen (Part 2) | A4 |
| Keyword `ridge` | A2 |
| Shared with Kari Nordmann | A3 (A1 is her own) |
| Shared with Ola Hansen | A1 (via Fjellgjengen) |
| Count per logger (Part 2) | Ingrid Berg 1, Kari Nordmann 1, Lars Haugen 1, Ola Hansen 1 |
| Count per logger (Part 3) | Ingrid Berg 1, Kari Nordmann 1, Lars Haugen 2, Ola Hansen 1 |
| Shared / private (Part 3) | 3 / 2 |
