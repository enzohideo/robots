import align.Align;
import align.LightPID;
import claw.Claw;
import deliver.Deliver;
import deliver.Arena.Location;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import sonar.Sonar;

public class Project {
  static SensorPort ultrasonicSensorPort = SensorPort.S2;

  static MotorPort lMotorPort = MotorPort.A;
  static MotorPort rMotorPort = MotorPort.C;
  static MotorPort clawMotorPort = MotorPort.B;

  static SensorPort lLightSensorPort = SensorPort.S3;
  static SensorPort rLightSensorPort = SensorPort.S4;
  static SensorPort clawColorSensorPort = SensorPort.S1;

  static Align align;
  static Sonar sonar;
  static Claw claw;
  static Deliver Deliver;

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

    LightSensor lLightSensor = new LightSensor(lLightSensorPort);
    LightSensor rLightSensor = new LightSensor(rLightSensorPort);
    ColorSensor clawColorSensor = new ColorSensor(clawColorSensorPort);

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

    LightPID lLightPID = new LightPID(
      lLightSensor,
      lMotor,
      60
    );

    LightPID rLightPID = new LightPID(
      rLightSensor,
      rMotor,
      60
    );

    align = new Align(lLightPID, rLightPID);
    sonar = new Sonar(ultrasonicSensor, lMotor, rMotor);
    claw = new Claw(clawMotor, clawColorSensor);
    Deliver = new Deliver(navigator);

    align.run(56, 45);
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

      int color = claw.run(true);
      claw.run(false);
      LCD.drawString("CLAW COLOR " + color, 0, 0);

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
      Deliver.run(0, (float) 0, Location.MUSEUM); // TODO: decide starting coordinates

      LCD.drawString("WAITING", 0, 0);
      sleep(1000);
    }
  }
}
