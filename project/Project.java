import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LCD;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

interface IState {
  public void run();
}

class Sonar {
  public enum Pipe {
    SHORT, TALL
  }

  private UltrasonicSensor sonar;
  private NXTMotor lMotor;
  private NXTMotor rMotor;
  private float shortPipeThreshold = 30.0f;
  private float tallPipeThreshold = 15.0f;
  private int power = 25;
  private int samplingPeriod = 100;

  public Sonar(UltrasonicSensor sonar, NXTMotor lMotor, NXTMotor rMotor) {
    this.sonar = sonar;
    this.lMotor = lMotor;
    this.rMotor = rMotor;
  }

  public Pipe run() {
    this.rMotor.setPower(power);
    this.lMotor.setPower(power);

    Pipe result;

    while (true) {
      float distance = this.sonar.getDistance();

      LCD.drawString("Sonar: " + Float.toString(distance), 0, 0);

      if (distance < this.tallPipeThreshold) {
        result = Pipe.TALL;
        break;
      } else if (distance < this.shortPipeThreshold) {
        result = Pipe.SHORT;
        break;
      }

      try {
        Thread.sleep(samplingPeriod);
      } catch (InterruptedException error) {
        LCD.drawString("Sonar ERROR: interrupted", 0, 0);
      }
    }

    this.rMotor.setPower(0);
    this.lMotor.setPower(0);

    return result;
  }
}

class Claw {
  private NXTRegulatedMotor motor;
  static int closeAngle = -45;

  public Claw(NXTRegulatedMotor motor) {
    this.motor = motor;
    this.motor.setSpeed(80);
  }

  public void run(boolean close) {
    if (close) {
      this.motor.rotateTo(closeAngle);
    } else {
      this.motor.rotateTo(0);
    }
  }

}

class PathFinder {

  //private boolean[][] adjacency_matrix;


  // class Node {
  //   public static enum Wall {
  //     RIGHT,
  //     UP,
  //     LEFT,
  //     DOWN
  //   }

  //   static int wall_to_state(Wall wall) {
  //     switch(wall) {
  //       case RIGHT:
  //         return 1;
  //       case UP:
  //         return 1 << 2;
  //       case LEFT:
  //         return 1 << 4;
  //       case DOWN:
  //         return 1 << 6;
  //     }
  //     return 0;
  //   }

  //   static byte create_state(
  //     boolean right,
  //     boolean up,
  //     boolean left,
  //     boolean down
  //   ) {
  //     byte state = 0;

  //     if (right) state += wall_to_state(Wall.RIGHT);
  //     if (up) state += wall_to_state(Wall.UP);
  //     if (left) state += wall_to_state(Wall.LEFT);
  //     if (down) state += wall_to_state(Wall.DOWN);

  //     return state;
  //   }

  //   public boolean is_closed() {

  //   }

  //   private byte state;

  //   public Node(
  //     boolean right,
  //     boolean up,
  //     boolean left,
  //     boolean down
  //   ) {
  //     state = create_state(right, up, left, down);
  //   }
  // }

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

  public static List<Integer> dijkstra(boolean[][] adjMatrix, int start, int end) {
    int n = adjMatrix.length;

    // Distâncias mínimas inicializadas com infinito
    int[] distances = new int[n];
    Arrays.fill(distances, Integer.MAX_VALUE);
    distances[start] = 0;

    // Array para rastrear os vértices anteriores no menor caminho
    int[] previous = new int[n];
    Arrays.fill(previous, -1);

    // Conjunto de vértices visitados
    boolean[] visited = new boolean[n];

    List<Integer> queue = new ArrayList<>();
    queue.add(start);

    while (!queue.isEmpty()) {
      // Encontrar o vértice com a menor distância na lista
      int current = -1;
      int minDistance = Integer.MAX_VALUE;
      for (int vertex : queue) {
        if (distances[vertex] >= minDistance) continue;
        minDistance = distances[vertex];
        current = vertex;
      }

      // Remover o vértice atual da lista
      queue.remove(current);

      // Se o vértice já foi visitado, ignorá-lo
      if (visited[current]) continue;
      visited[current] = true;

      // Verificar todos os vizinhos
      for (int neighbor = 0; neighbor < n; neighbor++) {
        if (adjMatrix[current][neighbor] && !visited[neighbor]) {
          int newDist = distances[current] + 1; // Peso uniforme (1)
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

    List<Integer> path = new ArrayList<>();
    for (int at = end; at != -1; at = previous[at]) {
      path.add(at);
    }

    if (path.isEmpty() || path.get(0) != start) {
      return new ArrayList<>();
    }

    for (int i = 0; i < path.size() / 2; ++i) {
      int j = path.size() - i - 1;
      path.set(i, path.get(i) ^ path.get(j));
      path.set(j, path.get(i) ^ path.get(j));
      path.set(i, path.get(i) ^ path.get(j));
    }

    return path;
  }

  public Path findRoute(int initialNode, int finalNode) {
    Path waypoints = new Path();

    for (int index : dijkstra(this.adjMatrix, initialNode, finalNode)) {
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


class ColorPID {
  public static float uLine = 0;
  public static float kP = 2.0f;
  public static float kI = 0f;
  public static float kD = 1f;

  ColorSensor sensor;
  NXTMotor motor;

  public int white = 60;
  public int prevError = 0;
  public int accError = 0;

  public ColorPID(ColorSensor sensor, NXTMotor motor, int white) {
    this.sensor = sensor;
    this.motor = motor;
    this.white = white;
  }

  public int getError(int value, int middle) {
    LCD.drawString("error: " + value + " " + middle, 0, 0);
    return (int) ((value - middle) / 1024f * 100f);
  }

  public float proportional(int error) {
    // Stop motor before crossing the line
    float tmp = kP * error;
    return tmp < 0 ? error : tmp;
  }

  public float integral(int error) {
    if (accError * error < 0) accError = 0;
    accError = accError + error;
    return kI * accError;
  }

  public float derivative(int error) {
    return kD * (error - prevError);
  }

  public void turn(float t) {
    float u = uLine + t;
    this.motor.setPower(clamp(u, -100, 100));
  }

  public int clamp(float value, float lower, float upper) {
    return (int) (value > upper ? upper : (value < lower ? lower : value));
  }

  public void run(int middle) {
    int value = sensor.getRawLightValue();
    int error = getError(value, middle);

    turn((value < this.white)
      ? proportional(error) + integral(error) + derivative(error)
      : 25
    );

    prevError = error;
  }
}

class Align {
  ColorPID lColorPID;
  ColorPID rColorPID;

  public void run(int lMiddle, int rMiddle) {
    while(true) {
      lColorPID.run(lMiddle);
      rColorPID.run(rMiddle);
    }
  }

  public Align(
    ColorPID lColorPID,
    ColorPID rColorPID
  ) {
    this.lColorPID = lColorPID;
    this.rColorPID = rColorPID;
  }
}

public class Project {
  static SensorPort ultrasonicSensorPort = SensorPort.S2;

  static MotorPort lMotorPort = MotorPort.A;
  static MotorPort rMotorPort = MotorPort.C;
  static MotorPort clawMotorPort = MotorPort.B;

  static SensorPort lColorSensorPort = SensorPort.S3;
  static SensorPort rColorSensorPort = SensorPort.S4;

  static Align align;
  static Sonar sonar;
  static Claw claw;
  static PathFinder pathFinder;

  static double wheelDiameter = 6.0;
  static double trackWidth = 12.0;

  static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      LCD.drawString("Failed to sleep", 0, 0);
    }
  }

  public static void main(String[] args) {
    UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(ultrasonicSensorPort);

    NXTMotor lMotor = new NXTMotor(lMotorPort);
    NXTMotor rMotor = new NXTMotor(rMotorPort);

    ColorSensor lColorSensor = new ColorSensor(lColorSensorPort);
    ColorSensor rColorSensor = new ColorSensor(rColorSensorPort);

    NXTRegulatedMotor lRegulatedMotor = new NXTRegulatedMotor(lMotorPort);
    NXTRegulatedMotor rRegulatedMotor = new NXTRegulatedMotor(rMotorPort);
    NXTRegulatedMotor clawMotor = new NXTRegulatedMotor(clawMotorPort);

    DifferentialPilot pilot = new DifferentialPilot(
      wheelDiameter, trackWidth, lRegulatedMotor, rRegulatedMotor, false
    );
    Navigator navigator = new Navigator(pilot);

    DifferentialPilot reversePilot = new DifferentialPilot(
      wheelDiameter, trackWidth, lRegulatedMotor, rRegulatedMotor, true
    );
    Navigator reverseNavigator = new Navigator(reversePilot);

    ColorPID lColorPID = new ColorPID(
      lColorSensor,
      lMotor,
      500
    );

    ColorPID rColorPID = new ColorPID(
      rColorSensor,
      rMotor,
      500
    );

    align = new Align(lColorPID, rColorPID);
    sonar = new Sonar(ultrasonicSensor, lMotor, rMotor);
    claw = new Claw(clawMotor);
    pathFinder = new PathFinder(navigator);

    Button.waitForAnyPress();

    while(true) {
      LCD.drawString("START", 0, 0);

      Sonar.Pipe pipe = sonar.run();
      LCD.drawString("PIPE: " + pipe.name(), 0, 1);

      LCD.drawString("WAITING", 0, 0);
      sleep(1000);

      pilot.quickStop();
      pilot.setRotateSpeed(40);
      pilot.setTravelSpeed(5);
      pilot.rotate(89, false);
      pilot.travel(5, false);

      claw.run(true);
      claw.run(false);

      pilot.travel(-10);
      pilot.rotate(89);

      LCD.drawString("ALIGN W/ RED", 0, 0);
      align.run(300, 300); // TODO: Improve red line callibration

      double y = trackWidth / 2;
      pilot.travel(-y);
      pilot.rotate(-89);

      LCD.drawString("ALIGN W/ BLUE", 0, 0);
      align.run(300, 300); // TODO: Callibrate for blue line

      pilot.rotate(89);
      pathFinder.run(0, y); // TODO: decide starting coordinates

      LCD.drawString("WAITING", 0, 0);
      sleep(1000);
    }
  }
}
