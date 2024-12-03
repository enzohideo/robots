import dijkstra.Node;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import dijkstra.Arena;

public class TestDijkstra extends Test {
  static void testNode() {
    for (int i = 0; i < 16; ++i) {
      Node.Wall[] walls = {
        Node.Wall.RIGHT,
        Node.Wall.UP,
        Node.Wall.LEFT,
        Node.Wall.DOWN,
      };

      boolean[] states = new boolean[]{
        (i & (1 << 0)) != 0,
        (i & (1 << 1)) != 0,
        (i & (1 << 2)) != 0,
        (i & (1 << 3)) != 0,
      };

      Node node = new Node(states[0], states[1], states[2], states[3]);

      for (int j = 0; j < 4; ++j) {
        String name = String.format(
          "Node [%b %b %b %b], Wall %s",
          states[0], states[1], states[2], states[3], walls[j].name()
        );
        if (!check(name, node.isOpen(walls[j]) == states[j])) return;
      }

      passed(String.format(
        "Node: %b %b %b %b",
        states[0], states[1], states[2], states[3]
      ));
    }
  }

  static void testFindRoute() {
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

    if (!check("path has right length", answer.length == result.size()))
      return;

    for (int i = 0; i < result.size(); ++i) {
      Waypoint waypoint = result.get(i);

      double x = waypoint.getX();
      double y = waypoint.getY();
      double expectedX = answer[i][0];
      double expectedY = answer[i][1];

      if (!check("coordinate X is the same", x - expectedX < 1e-8)) return;
      if (!check("coordinate Y is the same", y - expectedY < 1e-8)) return;
    }

    passed("Dijkstra finds path to drugstore");
  }

  public static void main(String[] args) {
    testFindRoute();
    testNode();
  }
}
