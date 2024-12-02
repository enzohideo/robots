package claw;

import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;

public class Claw {
  static int closeAngle = -45;

  ColorSensor colorSensor;
  NXTRegulatedMotor motor;

  // TODO: Fill it up with pipe colors
  enum Color {
    RED,
    YELLOW,
    BROWN
  }

  public Claw(NXTRegulatedMotor motor, ColorSensor colorSensor) {
    this.motor = motor;
    this.colorSensor = colorSensor;
  }

  double colorDiff(ColorSensor.Color c1, ColorSensor.Color c2) {
    int r = c1.getRed() - c2.getRed();
    int g = c1.getGreen() - c2.getGreen();
    int b = c1.getBlue() - c2.getBlue();
    return r*r + g*g + b*b;
  }

  int getColor() {
    // ColorSensor.Color color = colorSensor.getColor();
    // ColorSensor.Color[] colors = {
    //   
    // };
    // Color[] colorIDs = {
    //
    // };

    return this.colorSensor.getColorID();
  }

  public int run(boolean close) {
    this.motor.setSpeed(80);

    if (close)
      this.motor.rotateTo(closeAngle);
    else
      this.motor.rotateTo(0);

    return getColor();
  }
}
