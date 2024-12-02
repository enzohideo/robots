import java.util.List;

import dijkstra.*;

public class TestDijkstra extends Test {
  static boolean[][] adjMatrix() {
    boolean[][] matrix = new boolean[25][25];
    for(int i = 0; i < 25; i++) {
      for(int j = 0; j < 25; j++) {
        matrix[i][j] = false;
      }
    }
    matrix[0][1] = true;
    matrix[1][0] = true;
    matrix[1][2] = true;
    matrix[1][6] = true;
    matrix[2][1] = true;
    matrix[2][3] = true;
    matrix[3][2] = true;
    matrix[3][4] = true;
    matrix[3][8] = true;
    matrix[4][3] = true;
    // 5 = School
    matrix[6][1] = true;
    matrix[6][11] = true;
    // 7 = City Hall
    matrix[8][3] = true;
    matrix[8][13] = true;
    // 9 = Library
    matrix[10][11] = true;
    matrix[11][6] = true;
    matrix[11][10] = true;
    matrix[11][12] = true;
    matrix[11][16] = true;
    matrix[12][11] = true;
    matrix[12][13] = true;
    matrix[13][8] = true;
    matrix[13][12] = true;
    matrix[13][14] = true;
    matrix[13][18] = true;
    matrix[14][13] = true;
    // 15 = Bakery
    matrix[16][11] = true;
    matrix[16][21] = true;
    // 17 = Drugstore
    matrix[18][13] = true;
    matrix[18][23] = true;
    // 19 = Museum
    matrix[20][21] = true;
    matrix[21][16] = true;
    matrix[21][20] = true;
    matrix[21][22] = true;
    matrix[22][21] = true;
    matrix[22][23] = true;
    matrix[23][18] = true;
    matrix[23][22] = true;
    matrix[23][24] = true;
    matrix[24][23] = true;
    return matrix;
  }

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
    boolean[][] adjacencyMatrix = adjMatrix();

    adjacencyMatrix[12][17] = true;
    adjacencyMatrix[22][17] = true;

    List<Integer> result = Dijkstra.findRoute(adjacencyMatrix, 0, 17);

    adjacencyMatrix[12][17] = false;
    adjacencyMatrix[22][17] = false;

    int[] answer = {
      0, 1, 6, 11, 12, 17
    };

    if (!check("answer has same length", answer.length == result.size()))
      return;

    for (int i = 0; i < answer.length; ++i) {
      if (!check("node is the same", answer[i] == result.get(i)))
        return;
    }

    passed("Dijkstra finds path to drugstore");
  }

  public static void main(String[] args) {
    testFindRoute();
    testNode();
  }
}
