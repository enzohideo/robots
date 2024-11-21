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
import lejos.robotics.navigation.Waypoint;

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
    this.motor.setSpeed(120);
  }

  public void run(boolean close) {
    if (close) {
      this.motor.rotateTo(closeAngle);
    } else {
      this.motor.rotateTo(0);
    }
  }

}



class GeneratePath implements IState {

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

  private boolean[][] adjacency_matrix;
  private Waypoint[] nodes;
  private List<Waypoint> path;

  public GeneratePath() {
    this.nodes = init_list_of_nodes();
    this.adjacency_matrix = init_adjacency_matrix();
  }

  public static Waypoint[] init_list_of_nodes() {
    Waypoint[] points = new Waypoint[25];

    for (int i = 0; i < 5; ++i) {
      for (int j = 0; j < 5; ++j) {
        points[i*5 + j] = new Waypoint((i + 1) * 15.0, (j + 1) * 15.0);
      }
    }
    return points;
  }

  public static boolean[][] init_adjacency_matrix() {

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
    this.adjacency_matrix[14][19] = false;
    this.adjacency_matrix[18][19] = false;
  }

  public void open_museum() {
    this.adjacency_matrix[14][19] = true;
    this.adjacency_matrix[18][19] = true;
  }

  public void close_drugstore() {
    this.adjacency_matrix[12][17] = false;
    this.adjacency_matrix[22][17] = false;
  }

  public void open_drugstore() {
    this.adjacency_matrix[12][17] = true;
    this.adjacency_matrix[22][17] = true;
  }

  public void close_bakery() {
    this.adjacency_matrix[16][15] = false;
    this.adjacency_matrix[20][15] = false;
  }

  public void open_bakery() {
    this.adjacency_matrix[16][15] = true;
    this.adjacency_matrix[20][15] = true;
  }

  public void close_school() {
    this.adjacency_matrix[6][5] = false;
    this.adjacency_matrix[10][5] = false;
  }

  public void open_school() {
    this.adjacency_matrix[6][5] = true;
    this.adjacency_matrix[10][5] = true;
  }

  public void close_cityhall() {
    this.adjacency_matrix[8][7] = false;
    this.adjacency_matrix[6][7] = false;
  }

  public void open_cityhall() {
    this.adjacency_matrix[8][7] = true;
    this.adjacency_matrix[6][7] = true;
  }

  public void close_library() {
    this.adjacency_matrix[4][9] = false;
    this.adjacency_matrix[8][9] = false;
  }

  public void open_library() {
    this.adjacency_matrix[4][9] = true;
    this.adjacency_matrix[8][9] = true;
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
            if (distances[vertex] < minDistance) {
                minDistance = distances[vertex];
                current = vertex;
            }
        }

        // Remover o vértice atual da lista
        queue.remove((Integer) current);

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

    // Reconstruir o caminho mais curto
    List<Integer> path = new ArrayList<>();
    for (int at = end; at != -1; at = previous[at]) {
        path.add(at);
    }

    // Reverter a lista manualmente
    List<Integer> reversedPath = new ArrayList<>();
    for (int i = path.size() - 1; i >= 0; i--) {
        reversedPath.add(path.get(i));
    }

    // Se o caminho não começa com o vértice inicial, significa que não há caminho
    if (reversedPath.isEmpty() || reversedPath.get(0) != start) {
        return new ArrayList<>(); // Retorna uma lista vazia
    }

    return reversedPath;
  }

  public void WaypointsPath(List<Integer> nodes) {
    List<Waypoint> path = new ArrayList<Waypoint>();
    for (int i = 0; i < path.size(); i++) {
      path.add(this.nodes[nodes.get(i)]);
    }
    this.path = path;
  }

  public void run() {

  }
}


class ColorPID {
  public static float uLine = 0;
  public static float kP = 2.5f;
  public static float kI = 0f;
  public static float kD = 1f;

  ColorSensor sensor;
  NXTMotor motor;

  public int white = 60;
  public int middle = 49;
  public int prevError = 0;
  public int accError = 0;

  public ColorPID(ColorSensor sensor, NXTMotor motor, int middle, int white) {
    this.sensor = sensor;
    this.motor = motor;
    this.middle = middle;
    this.white = white;
  }

  public int getError(int value) {
    return value - middle;
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

  public void run() {
    int value = sensor.getLightValue();
    int error = getError(value);

    turn((value < this.white)
      ? proportional(error) + integral(error) + derivative(error)
      : 25
    );

    prevError = error;
  }
}

class Align implements IState {

  NXTMotor lMotor;
  NXTMotor rMotor;
  ColorPID lColorPID;
  ColorPID rColorPID;

  public void run() {
    while(true) {
      rColorPID.run();
      lColorPID.run();
    }
  }

  public Align(
    ColorSensor lColorSensor, NXTMotor lMotor,
    ColorSensor rColorSensor, NXTMotor rMotor
  ) {
    this.lMotor = lMotor;
    this.rMotor = rMotor;

    lColorPID = new ColorPID(
      lColorSensor,
      lMotor,
      49,
      60
    );

    rColorPID = new ColorPID(
      rColorSensor,
      rMotor,
      55,
      60
    );
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

  static double wheelDiameter = 5.6;
  static double trackWidth = 11.2;

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
      wheelDiameter, trackWidth, lRegulatedMotor, rRegulatedMotor, true
    );

    align = new Align(lColorSensor, lMotor, rColorSensor, rMotor);
    sonar = new Sonar(ultrasonicSensor, lMotor, rMotor);
    claw = new Claw(clawMotor);

    Button.waitForAnyPress();

    while(true) {
      LCD.drawString("START", 0, 0);
      // align.run();

      Sonar.Pipe pipe = sonar.run();
      LCD.drawString("PIPE FOUND: " + pipe.name(), 0, 1);
      LCD.drawString("WAITING", 0, 0);

      sleep(1000);

      pilot.rotate(89);
      pilot.travel(5);

      claw.run(true);

      pilot.travel(-10);
      pilot.rotate(89);

      align.run();

      pilot.rotate(-89);
    }
  }
}
