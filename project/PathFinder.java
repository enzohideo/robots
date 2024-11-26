import dijkstra.Dijkstra;
import lejos.nxt.LCD;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

class PathFinder {
  private Navigator navigator;
  private boolean[][] adjMatrix;
  private Waypoint[] nodes;

  public PathFinder(Navigator navigator) {
    this.navigator = navigator;
    this.nodes = init_list_of_nodes();
    this.adjMatrix = init_adjMatrix();
  }

  public static Waypoint[] init_list_of_nodes() {
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

  public void close_museum() {
    this.adjMatrix[14][19] = false;
    this.adjMatrix[18][19] = false;
  }

  public void open_museum() {
    this.adjMatrix[14][19] = true;
    this.adjMatrix[18][19] = true;
  }

  public void close_drugstore() {
    this.adjMatrix[12][17] = false;
    this.adjMatrix[22][17] = false;
  }

  public void open_drugstore() {
    this.adjMatrix[12][17] = true;
    this.adjMatrix[22][17] = true;
  }

  public void close_bakery() {
    this.adjMatrix[16][15] = false;
    this.adjMatrix[20][15] = false;
  }

  public void open_bakery() {
    this.adjMatrix[16][15] = true;
    this.adjMatrix[20][15] = true;
  }

  public void close_school() {
    this.adjMatrix[6][5] = false;
    this.adjMatrix[10][5] = false;
  }

  public void open_school() {
    this.adjMatrix[6][5] = true;
    this.adjMatrix[10][5] = true;
  }

  public void close_cityhall() {
    this.adjMatrix[8][7] = false;
    this.adjMatrix[6][7] = false;
  }

  public void open_cityhall() {
    this.adjMatrix[8][7] = true;
    this.adjMatrix[6][7] = true;
  }

  public void close_library() {
    this.adjMatrix[4][9] = false;
    this.adjMatrix[8][9] = false;
  }

  public void open_library() {
    this.adjMatrix[4][9] = true;
    this.adjMatrix[8][9] = true;
  }

  public Path findRoute(int initialNode, int finalNode) {
    Path waypoints = new Path();

    for (int index : Dijkstra.findRoute(this.adjMatrix, initialNode, finalNode)) {
      waypoints.add(this.nodes[index]);
    }

    return waypoints;
  }

  public int mapColor2Index(int color) {
    switch(color) {
      case 0: // school
        return 5;
      case 1: // city hall
        return 7;
      case 2: // library 
        return 9;
      case 3: // bakery
        return 15;
      case 4: // drugstore
        return 17;
      case 5: // museum
        return 19;
      }
    return -1;
  }

  public void run(float x, float y) {
    int final_index = mapColor2Index(4);
    int initial_index = 0; // TODO: Find nearest waypoint/node to given coordinate

    open_drugstore();

    Path path = findRoute(initial_index, final_index);
    navigator.getPoseProvider().setPose(new Pose(x, y, 0));
    navigator.clearPath();
    navigator.followPath(path);

    close_drugstore();

    LCD.drawString("FINISHED", 0, 0);
  }
}
