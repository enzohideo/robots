import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.CompassPilot; // deprecated

class CompassExample2 {
  static SensorPort compassPort = SensorPort.S1;
  static NXTRegulatedMotor leftMotor = Motor.A;
  static NXTRegulatedMotor rightMotor = Motor.C;

  public static void main(String[] args) {
    CompassHTSensor compass = new CompassHTSensor(compassPort);
    CompassPilot pilot = new CompassPilot(compass, 6, 12, leftMotor, rightMotor);

    LCD.drawString("Calibrating", 0, 0);
    pilot.calibrate();
    LCD.clear();

    while(true) {
      LCD.drawString("Robot heading", 0, 0);
      LCD.drawString(Float.toString(pilot.getHeading()), 0, 1);

      LCD.drawString("Compass heading", 0, 2);
      LCD.drawString(Float.toString(pilot.getCompassHeading()), 0, 2);
    }
  }
}
