import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.DifferentialPilot;

// https://lejos.sourceforge.io/nxt/nxj/api/lejos/nxt/addon/CompassHTSensor.html#startCalibration()
// "Must rotate *very* slowly, taking at least 20 seconds per rotation. Should
// make 1.5 to 2 full rotations. Must call stopCalibration() when done."

class CompassExample1 {
  static SensorPort compassPort = SensorPort.S4;
  static NXTRegulatedMotor leftMotor = Motor.A;
  static NXTRegulatedMotor rightMotor = Motor.C;

  public static void main(String[] args) {
    CompassHTSensor compass = new CompassHTSensor(compassPort);
    DifferentialPilot pilot = new DifferentialPilot(5.6, 11.5, leftMotor, rightMotor);

    LCD.drawString("Calibrating", 0, 0);
    compass.startCalibration();
    pilot.setRotateSpeed(360 / 20 * 2);
    pilot.rotate(360 * 2, false);
    compass.stopCalibration();
    try {
      Thread.sleep(2000);
    } catch(InterruptedException error) {
      LCD.drawString("failed to sleep", 0, 1);
    }
    compass.resetCartesianZero();
    LCD.clear();

    while(true) {
      LCD.drawString("Heading", 0, 0);
      LCD.clear(1);
      LCD.drawString(Float.toString(compass.getDegrees()), 0, 1);

      LCD.drawString("Cartesian", 0, 3);
      LCD.clear(4);
      LCD.drawString(Float.toString(compass.getDegreesCartesian()), 0, 4);
    }
  }
}
