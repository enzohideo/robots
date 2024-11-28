package dijkstra;

import lejos.nxt.LCD;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class PathFinder {
  private Navigator navigator;
  private boolean[][] adjMatrix;
  private Waypoint[] nodes;

  public PathFinder(Navigator navigator) {
    this.navigator = navigator;
    this.nodes = init_nodes();
    this.adjMatrix = init_adjMatrix();
  }

  public static Waypoint[] init_nodes() {
    Waypoint[] points = new Waypoint[25];
    double initialValue = 15.0;

    for (int i = 0; i < 5; ++i) {
      for (int j = 0; j < 5; ++j) {
        points[i*5 + j] = new Waypoint(initialValue + j * 30.0, initialValue + i * 30.0);
      }
    }

    return points;
  }

  public static boolean[][] init_adjMatrix() {

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

  public enum Location {
    MUSEUM(new int[][]{{14, 19}, {18, 19}}),
    DRUGSTORE(new int[][]{{12, 17}, {22, 17}}),
    BAKERY(new int[][]{{16, 15}, {20, 15}}),
    SCHOOL(new int[][]{{6, 5}, {10, 5}}),
    CITYHALL(new int[][]{{8, 7}, {6, 7}}),
    LIBRARY(new int[][]{{4, 9}, {8, 9}});

    private final int[][] positions;

    Location(int[][] positions) {
      this.positions = positions;
    }

    public int[][] getPositions() {
      return positions;
    }
  }

  public void toggle_door(Location location, boolean on) {
    for (int[] position : location.getPositions()) {
      this.adjMatrix[position[0]][position[1]] = on;
    }
  }

  public Path findRoute(int initialNode, int finalNode) {
    Path waypoints = new Path();

    for (int index : Dijkstra.findRoute(this.adjMatrix, initialNode, finalNode)) {
      waypoints.add(this.nodes[index]);
    }

    return waypoints;
  }

  public int location2Index(Location location) {
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
      }
    return -1;
  }

  public int initialIndex(float y) {
    return (int) (y / 30f);
}

  public void run(float x, float y, Location location) {
    int final_index = location2Index(location);
    int initial_index = initialIndex(y); // TODO: Find nearest waypoint/node to given coordinate

    toggle_door(location, true);

    Path path = findRoute(initial_index, final_index);
    navigator.getPoseProvider().setPose(new Pose(x, y, 0));
    navigator.clearPath();
    navigator.followPath(path);

    toggle_door(location, false);
  }
}
