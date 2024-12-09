import deliver.Arena;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class TestArena extends Test {
  static void _testFindRoute(Arena arena, Arena.Location location, double[][] answer) {
    Path result = arena.findRoute(0, 0, location);

    if (!check("path has wrong length", answer.length == result.size())) {
      for (int i = 0; i < result.size(); ++i) {
        Waypoint waypoint = result.get(i);
        double x = waypoint.getX();
        double y = waypoint.getY();
        System.out.printf("x: %f, y: %f\n", x, y);
      }
      return;
    }

    for (int i = 0; i < result.size(); ++i) {
      Waypoint waypoint = result.get(i);

      double x = waypoint.getX();
      double y = waypoint.getY();
      double expectedX = answer[i][0];
      double expectedY = answer[i][1];

      check(
        String.format("coordinate X is different: %f, expected: %f", x, expectedX),
        Math.abs(x - expectedX) < 1e-8
      );
      check(
        String.format("coordinate Y is different: %f, expected: %f", y, expectedY),
        Math.abs(y - expectedY) < 1e-8
      );
    }
  }

  static void testFindRoute() {
    start("Arena.findRoute()");

    Arena arena = new Arena();

    for (Arena.Location location : Arena.Location.values()) {
      double[][] answer = {};

      switch(location) {
        case SCHOOL:
          answer = new double[][]{
            { 15.000000, 14.750000 },
            { 45.000000, 14.750000 },
            { 45.000000, 44.250000 },
            { 15.000000, 44.250000 }
          };
          break;
        case CITYHALL:
          answer = new double[][]{
            { 15.000000, 14.750000 },
            { 45.000000, 14.750000 },
            { 45.000000, 44.250000 },
            { 75.000000, 44.250000 }
          };
          break;
        case LIBRARY:
          answer = new double[][]{
            { 15.000000, 14.750000 },
            { 45.000000, 14.750000 },
            { 75.000000, 14.750000 },
            { 105.000000, 14.750000 },
            { 135.000000, 14.750000 },
            { 135.000000, 44.250000 }
          };
          break;
        case BAKERY:
          answer = new double[][]{
            { 15.000000, 14.750000 },
            { 45.000000, 14.750000 },
            { 45.000000, 44.250000 },
            { 45.000000, 73.750000 },
            { 45.000000, 103.250000 },
            { 15.000000, 103.250000 }
          };
          break;
        case DRUGSTORE:
          answer = new double[][]{
            { 15., 14.75 },
            { 45., 14.75 },
            { 45., 44.25 },
            { 45., 73.75 },
            { 75., 73.75 },
            { 75., 103.25}
          };
          break;
        case MUSEUM:
          answer = new double[][]{
            { 15.000000, 14.750000 },
            { 45.000000, 14.750000 },
            { 75.000000, 14.750000 },
            { 105.000000, 14.750000 },
            { 105.000000, 44.250000 },
            { 105.000000, 73.750000 },
            { 135.000000, 73.750000 },
            { 135.000000, 103.250000 }
          };
          break;
        case PARK:
          answer = new double[][]{
            { 15.000000, 14.750000 },
            { 45.000000, 14.750000 },
            { 45.000000, 44.250000 },
            { 45.000000, 73.750000 },
            { 45.000000, 103.250000 },
            { 45.000000, 132.750000 },
            { 75.000000, 132.750000 },
            { 75.000000, 162.250000 }
          };
          break;
        default:
      }

      start(String.format("Arena.findRoute(%s)", location.name()));
      _testFindRoute(arena, location, answer);
      end();
    }
    end();
  }

  public static void main(String[] args) {
    start("Arena");
    testFindRoute();
    end();
  }
}
