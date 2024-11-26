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

    // DifferentialPilot reversePilot = new DifferentialPilot(
    //   wheelDiameter, trackWidth, lRegulatedMotor, rRegulatedMotor, true
    // );
    // Navigator reverseNavigator = new Navigator(reversePilot);

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
      pathFinder.run(0, (float) y); // TODO: decide starting coordinates

      LCD.drawString("WAITING", 0, 0);
      sleep(1000);
    }
  }
}
