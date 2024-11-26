class TestDijkstra extends Test {
  static void testNode() {
    for (int i = 0; i < 16; ++i) {
      Dijkstra.Node.Wall[] walls = {
        Dijkstra.Node.Wall.RIGHT,
        Dijkstra.Node.Wall.UP,
        Dijkstra.Node.Wall.LEFT,
        Dijkstra.Node.Wall.DOWN,
      };

      boolean[] states = new boolean[]{
        (i & (1 << 0)) != 0,
        (i & (1 << 1)) != 0,
        (i & (1 << 2)) != 0,
        (i & (1 << 3)) != 0,
      };

      Dijkstra.Node node = new Dijkstra.Node(states[0], states[1], states[2], states[3]);

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
    // TODO: Populate adjacencyMatrix
    // boolean[][] adjacencyMatrix = new boolean[][] {};

    // Dijkstra.findRoute(adjacencyMatrix, 0, 3);

    // TODO: Assert
  }

  public static void main(String[] args) {
    testFindRoute();
    testNode();
  }
}
