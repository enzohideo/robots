import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.Waypoint;

interface IState {
  public void run();
}

// class Sonar implements IState {
//
//   private UltrasonicSensor sonar;
//   private NXTMotor motor_left;
//   private NXTMotor motor_right;
//   private float distance_no_object = 30.0;
//   private float distance_small_object = 20.0;
//   private float distance_tall_object = 15.0;
//   private float distance_movement = 10.0;
//
//   public Sonar(UltrasonicSensor sonar, NXTMotor motor_left, NXTMotor motor_right) {
//     this.sonar = sonar;
//     this.motor_left = motor_left;
//     this.motor_right = motor_right;
//   }
//
//   public void run() {
//     float distance = this.sonar.getDistance();
//     if (distance > this.distance_no_object) {
//       this.motor_right.setPower(distance_movement);
//       this.motor_left.setPower(distance_movement);
//     }
//     else if (distance > this.distance_small_object) {
//
//     }
//     else {
//       
//     }
//   }
//
// }

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

// class GeneratePath implements IState {
//   
//   private boolean[][] adjacency_matrix;
//
//   public GeneratePath() {
//     this.adjacency_matrix = 
//   }
//
//   public boolean[][] WaipointsGraph() {
//     Waipoint point0 = new Waipoint(15.0, 15.0);
//     Waipoint point1 = new Waipoint(15.0, 30.0);
//     Waipoint point2 = new Waipoint(15.0, 45.0);
//     Waipoint point3 = new Waipoint(15.0, 60.0);
//     Waipoint point4 = new Waipoint(15.0, 75.0);
//     Waipoint point5 = new Waipoint(30.0, 15.0);
//     Waipoint point6 = new Waipoint(30.0, 30.0);
//     Waipoint point7 = new Waipoint(30.0, 45.0);
//     Waipoint point8 = new Waipoint(30.0, 60.0);
//     Waipoint point9 = new Waipoint(30.0, 75.0);
//     Waipoint point10 = new Waipoint(45.0, 15.0);
//     Waipoint point11 = new Waipoint(45.0, 30.0);
//     Waipoint point12 = new Waipoint(45.0, 45.0);
//     Waipoint point13 = new Waipoint(45.0, 60.0);
//     Waipoint point14 = new Waipoint(45.0, 75.0);
//     Waipoint point15 = new Waipoint(60.0, 15.0);
//     Waipoint point16 = new Waipoint(60.0, 30.0);
//     Waipoint point17 = new Waipoint(60.0, 45.0);
//     Waipoint point18 = new Waipoint(60.0, 60.0);
//     Waipoint point19 = new Waipoint(60.0, 75.0);
//     Waipoint point20 = new Waipoint(75.0, 15.0);
//     Waipoint point21 = new Waipoint(75.0, 30.0);
//     Waipoint point22 = new Waipoint(75.0, 45.0);
//     Waipoint point23 = new Waipoint(75.0, 60.0);
//     Waipoint point24 = new Waipoint(75.0, 75.0);
//
//     boolean[][] matrix = new boolean[25][25];
//     for(int i = 0; i < 25; i++) {
//       for(int j = 0; j < 25; j++) {
//         matrix[i][j] = false;
//       }
//     }
//     matrix[0][1] = true;
//     matrix[1][0] = true;
//     matrix[1][2] = true;
//     matrix[1][6] = true;
//     matrix[2][1] = true;
//     matrix[2][3] = true;
//     matrix[3][2] = true;
//     matrix[3][4] = true;
//     matrix[3][8] = true;
//     matrix[4][3] = true;
//     //matrix[4][9] = true;
//     //matrix[5][6] = true; 5 = School
//     //matrix[5][10] = true;
//     matrix[6][1] = true;
//     //matrix[6][5] = true;
//     //matrix[6][7] = true;
//     matrix[6][11] = true;
//     //matrix[7][6] = true; 7 = City Hall
//     //matrix[7][8] = true;
//     matrix[8][3] = true;
//     matrix[8][7] = true;
//     matrix[8][9] = true;
//     matrix[8][13] = true;
//     //matrix[9][4] = true; 9 = Library
//     //matrix[9][8] = true;
//     //matrix[10][5] = true;
//     matrix[10][11] = true;
//     matrix[11][6] = true;
//     matrix[11][10] = true;
//     matrix[11][12] = true;
//     matrix[11][16] = true;
//     matrix[12][11] = true;
//     matrix[12][13] = true;
//     matrix[13][8] = true;
//     matrix[13][12] = true;
//     matrix[13][14] = true;
//     matrix[13][18] = true;
//     matrix[14][13] = true;
//     // 15 = Bakery
//     matrix[16][11] = true;
//     matrix[16][21] = true;
//     // 17 = Drugstore 
//     matrix[18][13] = true;
//     matrix[18][23] = true;
//     // 19 = Museum
//     matrix[20][21] = true;
//     matrix[21][16] = true;
//     matrix[21][20] = true;
//     matrix[21][22] = true;
//     matrix[22][21] = true;
//     matrix[22][23] = true;
//     matrix[23][18] = true;
//     matrix[23][22] = true;
//     matrix[23][24] = true;
//     matrix[24][23U = true;
//     return matrix;
//   }
//
//   public void run() {
//
//   }
// }
//
//

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
    while (true) {
      int value = sensor.getLightValue();
      int error = getError(value);

      turn((value < this.white)
        ? proportional(error) + integral(error) + derivative(error)
        : 25
      );

      prevError = error;
    }
  }
}

class Align implements IState {

  NXTMotor lMotor;
  NXTMotor rMotor;
  ColorPID lColorPID;
  ColorPID rColorPID;

  public void run() {
    rColorPID.run();
    lColorPID.run();
  }

  public Align(NXTMotor lMotor, NXTMotor rMotor) {
    this.lMotor = lMotor;
    this.rMotor = rMotor;

    lColorPID = new ColorPID(
      new ColorSensor(SensorPort.S1),
      lMotor,
      49,
      60
    );

    rColorPID = new ColorPID(
      new ColorSensor(SensorPort.S4),
      rMotor,
      55,
      60
    );
  }
}

public class Project {
  static SensorPort ultrasonicSensorPort = SensorPort.S1;

  static MotorPort lMotorPort = MotorPort.A;
  static MotorPort rMotorPort = MotorPort.C;
  static MotorPort clawMotorPort = MotorPort.B;

  static Align align;
  static Sonar sonar;
  static Claw claw;

  public static void main(String[] args) {
    UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(ultrasonicSensorPort);

    NXTMotor lMotor = new NXTMotor(lMotorPort);
    NXTMotor rMotor = new NXTMotor(rMotorPort);

    NXTRegulatedMotor lRegulatedMotor = new NXTRegulatedMotor(lMotorPort);
    NXTRegulatedMotor rRegulatedMotor = new NXTRegulatedMotor(rMotorPort);
    NXTRegulatedMotor clawMotor = new NXTRegulatedMotor(clawMotorPort);

    align = new Align(lMotor, rMotor);
    // sonar = new Sonar(ultrasonicSensor, lMotor, rMotor);
    claw = new Claw(clawMotor);

    Button.waitForAnyPress();

    while(true) {
      align.run();
      // sonar.run();
      // align.run();
      // claw.run(true);
    }
  }
}
