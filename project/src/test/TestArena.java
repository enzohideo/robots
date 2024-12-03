import deliver.Arena;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class TestArena extends Test {
  static void testFindRoute() {
    start("Arena.findRoute()");

    Arena arena = new Arena();

    double[][] answer = {
      { 15., 15. },
      { 45., 15. },
      { 45., 45. },
      { 45., 75. },
      { 75., 75. },
      { 75., 105.}
    };

    Path result = arena.findRoute(0, 0, Arena.Location.DRUGSTORE);

    check("path has right length", answer.length == result.size());

    for (int i = 0; i < result.size(); ++i) {
      Waypoint waypoint = result.get(i);

      double x = waypoint.getX();
      double y = waypoint.getY();
      double expectedX = answer[i][0];
      double expectedY = answer[i][1];

      check("coordinate X is the same", x - expectedX < 1e-8);
      check("coordinate Y is the same", y - expectedY < 1e-8);
    }

    end();
  }

  public static void main(String[] args) {
    start("Arena");
    testFindRoute();
    end();
  }
}
