import hardware.Hardware;
import align.Align;
import align.IdentifyLine;
import align.CompassAlign;
//import align.LightPID;
import claw.Claw;
import deliver.Arena;
import deliver.Deliver;
import deliver.Arena.Location;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.robotics.navigation.Pose;
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

    NXTMotor lMotor = new NXTMotor(Hardware.lMotorPort);
    NXTMotor rMotor = new NXTMotor(Hardware.rMotorPort);

    LightSensor lLightSensor = new LightSensor(Hardware.lLightSensorPort);
    //LightSensor rLightSensor = new LightSensor(Hardware.rLightSensorPort);
    ColorSensor clawColorSensor = new ColorSensor(Hardware.clawColorSensorPort);

    NXTRegulatedMotor lRegulatedMotor = new NXTRegulatedMotor(Hardware.lMotorPort);
    NXTRegulatedMotor rRegulatedMotor = new NXTRegulatedMotor(Hardware.rMotorPort);
    NXTRegulatedMotor clawMotor = new NXTRegulatedMotor(Hardware.clawMotorPort);
    CompassHTSensor compassHT = new CompassHTSensor(Hardware.compassPort);

    DifferentialPilot pilot = new DifferentialPilot(
      Hardware.wheelDiameter, Hardware.trackWidth, lRegulatedMotor, rRegulatedMotor, false
    );
    Navigator navigator = new Navigator(pilot);

    // DifferentialPilot reversePilot = new DifferentialPilot(
    //   wheelDiameter, trackWidth, lRegulatedMotor, rRegulatedMotor, true
    // );
    // Navigator reverseNavigator = new Navigator(reversePilot);

    idLine = new IdentifyLine(lLightSensor, pilot);
    compass = new CompassAlign(compassHT, pilot);
    sonar = new Sonar(ultrasonicSensor, lMotor, rMotor);
    claw = new Claw(clawMotor, clawColorSensor);
    Deliver = new Deliver(navigator);

    Button.waitForAnyPress();

    compass.calibrate();

    while(true) {
      LCD.drawString("START", 0, 0);

      LCD.drawString("WAITING", 0, 0);
      sleep(1000);

      Sonar.Pipe pipe = sonar.run();
      LCD.drawString("PIPE: " + pipe.name(), 0, 1);

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

      Pose pose = navigator.getPoseProvider().getPose();
      LCD.drawString("X" + pose.getX(), 0, 2);
      LCD.drawString("Y" + pose.getY(), 0, 3);

      //LCD.drawString("ALIGN W/ Black", 0, 0);
      //idLine.run();

      double y = Hardware.trackWidth / 2;
      pilot.travel(-y);
      pilot.rotate(-89);

      //LCD.drawString("ALIGN W/ BLUE", 0, 0);
      //idLine.run();

      pilot.rotate(89);

      Arena.Location destiny = define_destiny(color, pipe);
      Deliver.run(0, (float) 0, destiny); // TODO: decide starting coordinates

      LCD.drawString("WAITING", 0, 0);
      sleep(1000);
    }
  }
}
