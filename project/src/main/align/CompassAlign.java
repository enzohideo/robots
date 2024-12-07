package align;

import hardware.Hardware;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class CompassAlign {
  static float maxDegreesError = 0.05f;
  static int rotateSpeed = 5;
  static int calibrationRotateSpeed = 360 / 10;
  static int waitForCompassToSettle = 1250;

  CompassHTSensor compass;
  DifferentialPilot pilot;

  public CompassAlign(CompassHTSensor compass, DifferentialPilot pilot) {
    this.compass = compass;
    this.pilot = pilot;
  }

  public void calibrate() {
    compass.startCalibration();

    pilot.setRotateSpeed(calibrationRotateSpeed);
    pilot.rotate(540);
    pilot.quickStop();

    compass.stopCalibration();
    Hardware.sleep(waitForCompassToSettle); // wait compass to settle
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
    pilot.setRotateSpeed(rotateSpeed);
    while(Math.abs(degrees) > maxDegreesError) {
      LCD.clear(0);
      LCD.drawString("degrees: " + degrees, 0, 0);
      pilot.rotate(- degrees);
      Hardware.sleep(waitForCompassToSettle);
      degrees = getDegrees();
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
