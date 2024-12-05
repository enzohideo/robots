import hardware.Hardware;
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
  static Align align;
  static Sonar sonar;
  static Claw claw;
  static Deliver Deliver;

  static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      LCD.drawString("Failed to sleep", 0, 0);
    }
  }

  public static void main(String[] args) {
    UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(Hardware.ultrasonicSensorPort);

    NXTMotor lMotor = new NXTMotor(Hardware.lMotorPort);
    NXTMotor rMotor = new NXTMotor(Hardware.rMotorPort);

    LightSensor lLightSensor = new LightSensor(Hardware.lLightSensorPort);
    LightSensor rLightSensor = new LightSensor(Hardware.rLightSensorPort);
    ColorSensor clawColorSensor = new ColorSensor(Hardware.clawColorSensorPort);

    NXTRegulatedMotor lRegulatedMotor = new NXTRegulatedMotor(Hardware.lMotorPort);
    NXTRegulatedMotor rRegulatedMotor = new NXTRegulatedMotor(Hardware.rMotorPort);
    NXTRegulatedMotor clawMotor = new NXTRegulatedMotor(Hardware.clawMotorPort);

    DifferentialPilot pilot = new DifferentialPilot(
      Hardware.wheelDiameter, Hardware.trackWidth, lRegulatedMotor, rRegulatedMotor, false
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

      Claw.Color color = claw.run(true);
      claw.run(false);
      LCD.drawString("CLAW COLOR " + color, 0, 0);

      pilot.travel(-10);
      pilot.rotate(89);

      LCD.drawString("ALIGN W/ RED", 0, 0);
      align.run(300, 300); // TODO: Improve red line callibration

      double y = Hardware.trackWidth / 2;
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
