package align;

import hardware.Hardware;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class CompassAlign {
  static float maxDegreesError = 0.5f;
  static int rotateSpeed = 10;
  static int calibrationRotateSpeed = 360 / 20;

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
    compass.resetCartesianZero();
  }

  int sign(float degrees) {
    return (degrees > 0 ? 1 : 0) - (degrees < 0 ? 1 : 0);
  }

  public void run() {
    float degrees = compass.getDegreesCartesian();
    pilot.setRotateSpeed(rotateSpeed);
    while(Math.abs(degrees) > maxDegreesError) {
      pilot.rotate(- degrees * 0.5);
      degrees = compass.getDegreesCartesian();
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
    align.calibrate();
    align.run();
  }
}
