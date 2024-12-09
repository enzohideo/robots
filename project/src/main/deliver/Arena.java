package deliver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class Arena {
  static int[] nullPath = new int[]{};

  Waypoint[] nodes;
  boolean[][] adjMatrix;

  public Arena() {
    initAdjMatrix();
    initNodes();
  }

  // TODO: Move to separate file
  public enum Location {
    MUSEUM(new int[][]{{14, 19}, {18, 19}}),
    DRUGSTORE(new int[][]{{12, 17}, {22, 17}}),
    BAKERY(new int[][]{{16, 15}, {20, 15}}),
    SCHOOL(new int[][]{{6, 5}, {10, 5}}),
    CITYHALL(new int[][]{{8, 7}, {6, 7}}),
    LIBRARY(new int[][]{{4, 9}, {8, 9}}),
    PARK(new int[][]{{22, 27}}); //{20, 25}, , {24, 29}

    private final int[][] positions;

    Location(int[][] positions) {
      this.positions = positions;
    }

    public int[][] getPositions() {
      return this.positions;
    }
  }

  void initAdjMatrix() {
    this.adjMatrix = new boolean[30][30];

    for(int i = 0; i < 30; i++) {
      for(int j = 0; j < 30; j++) {
        this.adjMatrix[i][j] = false;
      }
    }

    this.adjMatrix[0][1] = true;
    this.adjMatrix[1][0] = true;
    this.adjMatrix[1][2] = true;
    this.adjMatrix[1][6] = true;
    this.adjMatrix[2][1] = true;
    this.adjMatrix[2][3] = true;
    this.adjMatrix[3][2] = true;
    this.adjMatrix[3][4] = true;
    this.adjMatrix[3][8] = true;
    this.adjMatrix[4][3] = true;
    // 5 = School
    this.adjMatrix[6][1] = true;
    this.adjMatrix[6][11] = true;
    // 7 = City Hall
    this.adjMatrix[8][3] = true;
    this.adjMatrix[8][13] = true;
    // 9 = Library
    this.adjMatrix[10][11] = true;
    this.adjMatrix[11][6] = true;
    this.adjMatrix[11][10] = true;
    this.adjMatrix[11][12] = true;
    this.adjMatrix[11][16] = true;
    this.adjMatrix[12][11] = true;
    this.adjMatrix[12][13] = true;
    this.adjMatrix[13][8] = true;
    this.adjMatrix[13][12] = true;
    this.adjMatrix[13][14] = true;
    this.adjMatrix[13][18] = true;
    this.adjMatrix[14][13] = true;
    // 15 = Bakery
    this.adjMatrix[16][11] = true;
    this.adjMatrix[16][21] = true;
    // 17 = Drugstore
    this.adjMatrix[18][13] = true;
    this.adjMatrix[18][23] = true;
    // 19 = Museum
    this.adjMatrix[20][21] = true;
    this.adjMatrix[21][16] = true;
    this.adjMatrix[21][20] = true;
    this.adjMatrix[21][22] = true;
    this.adjMatrix[22][21] = true;
    this.adjMatrix[22][23] = true;
    this.adjMatrix[23][18] = true;
    this.adjMatrix[23][22] = true;
    this.adjMatrix[23][24] = true;
    this.adjMatrix[24][23] = true;
  }

  void initNodes() {
    this.nodes = new Waypoint[30];
    double initialXValue = 15.0;
    double initialYValue = 29.5 / 2;

    for (int i = 0; i < 5; ++i) {
      for (int j = 0; j < 6; ++j) {
        this.nodes[j*5 + i] = new Waypoint(
          initialXValue + i * 30.0,
          initialYValue + j * 29.5
        );
      }
    }
  }

  // TODO: Just store the index inside each location
  int location2Index(Location location) {
    switch(location) {
      case SCHOOL:
        return 5;
      case CITYHALL:
        return 7;
      case LIBRARY:
        return 9;
      case BAKERY:
        return 15;
      case DRUGSTORE:
        return 17;
      case MUSEUM:
        return 19;
      case PARK:
        return 27;
      }
    return -1;
  }

  // TODO: Test
  int findNearestNode(float x, float y) {
    return (int) (y / 30f);
  }

  void toggleDoor(Location location, boolean on) {
    for (int[] position : location.getPositions()) {
      this.adjMatrix[position[0]][position[1]] = on;
    }
  }

  int[] dijkstra(int start, int end) {
    int n = this.adjMatrix.length;

    int[] distances = new int[n];
    Arrays.fill(distances, Integer.MAX_VALUE);
    distances[start] = 0;

    int[] previous = new int[n];
    Arrays.fill(previous, -1);

    boolean[] visited = new boolean[n];

    List<Integer> queue = new ArrayList<>();
    queue.add(start);

    while (!queue.isEmpty()) {
      int current = -1;
      int minDistance = Integer.MAX_VALUE;
      for (int vertex : queue) {
        if (distances[vertex] >= minDistance) continue;
        minDistance = distances[vertex];
        current = vertex;
      }

      queue.remove((Integer) current);

      if (visited[current]) continue;
      visited[current] = true;

      for (int neighbor = 0; neighbor < n; neighbor++) {
        if (adjMatrix[current][neighbor] && !visited[neighbor]) {
          int newDist = distances[current] + 1;
          if (newDist < distances[neighbor]) {
            distances[neighbor] = newDist;
            previous[neighbor] = current;
            if (!queue.contains(neighbor)) {
              queue.add(neighbor);
            }
          }
        }
      }
    }

    int length = distances[end] + 1;

    if (length <= 0 || length == Integer.MAX_VALUE) {
      return nullPath;
    }

    int[] path = new int[length];

    for (int at = end, i = 1; at != -1; at = previous[at], ++i) {
      int index = length - i;
      if (index < 0) return nullPath;
      path[index] = at;
    }

    if (path[0] != start || path[length - 1] != end) {
      return nullPath;
    }

    return path;
  }

  public Path findRoute(float x, float y, Location location) {
    int start = findNearestNode(x, y);
    int end = location2Index(location);

    toggleDoor(location, true);
    int[] nodeIndexes = dijkstra(start, end);
    toggleDoor(location, false);

    Path path = new Path();

    for (int nodeIndex : nodeIndexes) {
      path.add(this.nodes[nodeIndex]);
    }

    return path;
  }
}
