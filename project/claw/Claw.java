package claw;

import lejos.nxt.NXTRegulatedMotor;

public class Claw {
  private NXTRegulatedMotor motor;
  static int closeAngle = -45;

  public Claw(NXTRegulatedMotor motor) {
    this.motor = motor;
    this.motor.setSpeed(80);
  }

  public void run(boolean close) {
    if (close) {
      this.motor.rotateTo(closeAngle);
    } else {
      this.motor.rotateTo(0);
    }
  }
}
