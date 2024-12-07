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
    pilot.setRotateSpeed(360 / 10); // 10 seconds per rotation
    pilot.rotate(360 * 1.5);        // 1.5 rotations
    compass.stopCalibration();

    try {
      Thread.sleep(2000);
    } catch(InterruptedException error) {
      LCD.drawString("Failed to sleep", 0, 0);
    }

    compass.resetCartesianZero();

    LCD.clear();
    LCD.drawString("Heading", 0, 0);
    LCD.drawString("Cartesian", 0, 2);

    while(true) {
      LCD.clear(1);
      LCD.clear(3);
      LCD.drawString(Float.toString(compass.getDegrees()), 0, 1);
      LCD.drawString(Float.toString(compass.getDegreesCartesian()), 0, 3);
    }
  }
}
