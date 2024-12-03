import deliver.Arena;
import deliver.Node;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class TestDeliver extends Test {
  static class TestNode {
    static Node.Wall[] walls = {
      Node.Wall.RIGHT,
      Node.Wall.UP,
      Node.Wall.LEFT,
      Node.Wall.DOWN,
    };

    static boolean[] initStates(int i) {
      return new boolean[]{
        (i & (1 << 0)) != 0,
        (i & (1 << 1)) != 0,
        (i & (1 << 2)) != 0,
        (i & (1 << 3)) != 0,
      };
    }

    static Node[] initNodes() {
      Node[] nodes = new Node[16];

      for (int i = 0; i < 16; ++i) {
        boolean[] states = initStates(i);
        nodes[i] = new Node(states[0], states[1], states[2], states[3]);
      }

      return nodes;
    }

    static String stateMessage(int index) {
      boolean[] states = initStates(index);
      return String.format(
        "Node %d [%b %b %b %b] ",
        index, states[0], states[1], states[2], states[3]
      );
    }

    static String wallMessage(Node.Wall w) {
      return String.format("Wall %s ", w.name());
    }

    static void testIsOpen() {
      String message = "Check Node doors. ";
      Node[] nodes = initNodes();

      for (int i = 0; i < nodes.length; ++i) {
        boolean[] states = initStates(i);

        for (int j = 0; j < 4; ++j) {
          String msg = message;
          msg += stateMessage(i);
          msg += wallMessage(walls[j]);
          if (!check(msg, nodes[i].isOpen(walls[j]) == states[j])) return;
        }
      }

      passed(message);
    }

    static void testToggle() {
      String message = "Check Node toggle. ";
      Node[] nodes = initNodes();

      for (int i = 0; i < nodes.length; ++i) {
        boolean[] states = initStates(i);
        Node node = nodes[i];

        node.toggle();

        for (int j = 0; j < walls.length; ++j) {
          String msg = message + "All doors should be closed. ";
          msg += stateMessage(i);
          msg += wallMessage(walls[j]);
          if (!check(msg, node.isOpen(walls[j]) == false)) return;
        }

        node.toggle();

        for (int j = 0; j < walls.length; ++j) {
          String msg = message + "All doors that were open previously should now be open as well. ";
          msg += stateMessage(i);
          msg += wallMessage(walls[j]);
          if (!check(msg, node.isOpen(walls[j]) == states[j])) return;
        }
      }

      passed(message);
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
    name(TestArena.class.getSimpleName());
    testFindRoute();
    TestNode.testIsOpen();
    TestNode.testToggle();
  }
}
