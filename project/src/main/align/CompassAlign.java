package align;

import hardware.Hardware;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class CompassAlign {
  static float maxDegreesError = 0.05f;
  static int minRotateSpeed = 5;
  static int rotateTime = 2;
  static int calibrationRotateSpeed = 360 / 10;
  static int waitForCompassToSettle = 1250;
  static double calibrationDegrees = 540 + 2;

  CompassHTSensor compass;
  DifferentialPilot pilot;

  public CompassAlign(CompassHTSensor compass, DifferentialPilot pilot) {
    this.compass = compass;
    this.pilot = pilot;
  }

  public void calibrate() {
    compass.startCalibration();

    pilot.setRotateSpeed(calibrationRotateSpeed);
    pilot.rotate(calibrationDegrees);
    pilot.quickStop();

    compass.stopCalibration();
    Hardware.sleep(waitForCompassToSettle); // wait compass to settle
    reset();
  }

  public void reset() {
    compass.resetCartesianZero();
  }

  int sign(float degrees) {
    return (degrees > 0 ? 1 : 0) - (degrees < 0 ? 1 : 0);
  }

  float getDegrees() {
    float degrees = compass.getDegreesCartesian();
    if (degrees > 180) {
      degrees -= 360f;
    }
    return degrees;
  }

  public void run() {
    float degrees = getDegrees();
    LCD.drawString("Compass", 0, 0);
    while(Math.abs(degrees) > maxDegreesError) {
      LCD.clear(1);
      LCD.clear(2);
      LCD.drawString("before: " + degrees, 0, 1);
      pilot.setRotateSpeed(Math.max(Math.abs(degrees / rotateTime), minRotateSpeed));
      pilot.rotate(- degrees);

      Hardware.sleep(waitForCompassToSettle);

      degrees = getDegrees();
      LCD.drawString("after: " + degrees, 0, 2);
    }
  }

  public static void main(String[] args) {
    NXTRegulatedMotor lMotor = new NXTRegulatedMotor(Hardware.lMotorPort);
    NXTRegulatedMotor rMotor = new NXTRegulatedMotor(Hardware.rMotorPort);
    DifferentialPilot pilot = new DifferentialPilot(
      Hardware.wheelDiameter, Hardware.trackWidth, lMotor, rMotor
    );
    CompassHTSensor compass = new CompassHTSensor(Hardware.compassPort);
    CompassAlign align = new CompassAlign(compass, pilot);

    Button.waitForAnyPress();
    align.calibrate();

    while (true) {
      Button.waitForAnyPress();
      align.run();
    }
  }
}
