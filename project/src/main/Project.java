import hardware.Hardware;
import align.IdentifyLine;
import align.CompassAlign;
import claw.Claw;
import deliver.Arena;
import deliver.Deliver;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import sonar.Sonar;

public class Project {
  //static Align align;
  static IdentifyLine idLine;
  static CompassAlign compass;
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

  public static Arena.Location define_destiny(Claw.Color color, Sonar.Pipe size) {
    if (color == Claw.Color.BLUE && size == Sonar.Pipe.SHORT)
      return Arena.Location.SCHOOL;
    else if (color == Claw.Color.BLUE && size == Sonar.Pipe.TALL)
      return Arena.Location.MUSEUM;
    else if (color == Claw.Color.GREEN && size == Sonar.Pipe.SHORT)
      return Arena.Location.PARK;
    else if (color == Claw.Color.GREEN && size == Sonar.Pipe.TALL)
      return Arena.Location.CITYHALL;
    else if (color == Claw.Color.YELLOW && size == Sonar.Pipe.SHORT)
      return Arena.Location.LIBRARY;
    else if (color == Claw.Color.YELLOW && size == Sonar.Pipe.TALL)
      return Arena.Location.BAKERY;
    return Arena.Location.DRUGSTORE;
  }

  public static void main(String[] args) {
    UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(Hardware.ultrasonicSensorPort);

    LightSensor lLightSensor = new LightSensor(Hardware.lLightSensorPort);
    ColorSensor clawColorSensor = new ColorSensor(Hardware.clawColorSensorPort);

    NXTRegulatedMotor lRegulatedMotor = new NXTRegulatedMotor(Hardware.lMotorPort);
    NXTRegulatedMotor rRegulatedMotor = new NXTRegulatedMotor(Hardware.rMotorPort);
    NXTRegulatedMotor clawMotor = new NXTRegulatedMotor(Hardware.clawMotorPort);
    CompassHTSensor compassHT = new CompassHTSensor(Hardware.compassPort);

    DifferentialPilot pilot = new DifferentialPilot(
      Hardware.wheelDiameter, Hardware.trackWidth, lRegulatedMotor, rRegulatedMotor, false
    );
    Navigator navigator = new Navigator(pilot);

    DifferentialPilot reversePilot = new DifferentialPilot(
      Hardware.wheelDiameter, Hardware.trackWidth, lRegulatedMotor, rRegulatedMotor, true
    );
    Navigator reverseNavigator = new Navigator(reversePilot);

    idLine = new IdentifyLine(lLightSensor, pilot);
    compass = new CompassAlign(compassHT, pilot);
    sonar = new Sonar(ultrasonicSensor, pilot);
    claw = new Claw(clawMotor, clawColorSensor);
    Deliver = new Deliver(navigator);

    Button.waitForAnyPress();

    // compass.calibrate();

    while(true) {
      idLine.run(48);

      pilot.setRotateSpeed(40);
      pilot.rotate(-90);

      idLine.run(48);

      pilot.setRotateSpeed(40);
      pilot.rotate(-90);

      Sonar.Pipe pipe = sonar.run();
      LCD.drawString("PIPE: " + pipe.name(), 0, 1);

      pilot.quickStop();
      pilot.setRotateSpeed(40);
      pilot.setTravelSpeed(5);

      if (pipe == Sonar.Pipe.SHORT) {
        pilot.travel(10, false);
      } else {
        pilot.travel(5, false);
      }

      pilot.rotate(90, false);
      pilot.travel(15, false);

      Claw.Color color = claw.run(true);
      LCD.drawString("CLAW COLOR " + color, 0, 2);

      pilot.travel(-20);
      pilot.rotate(90);

      idLine.run(48);
      pilot.rotate(-90);
      idLine.run(48);
      pilot.rotate(90);

      Arena.Location destiny = define_destiny(color, pipe);

      float x = 6;
      float y = 6;

      Path path = Deliver.run(x, y, destiny);
      Path reversePath = new Path();
      for (int i = 0; i < path.size() - 1; ++i) {
        Waypoint wp = path.get(i);
        reversePath.add(0, wp);
      }

      reversePilot.setRotateSpeed(40);
      reversePilot.setTravelSpeed(5);

      reverseNavigator.getPoseProvider().setPose(new Pose(x, y, 0));
      reverseNavigator.clearPath();
      reverseNavigator.followPath(path);
      reverseNavigator.waitForStop();

      reversePilot.rotate(180);
      claw.run(false);

      reverseNavigator.clearPath();
      reverseNavigator.followPath(reversePath);
    }
  }
}
