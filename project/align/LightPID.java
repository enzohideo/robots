package align;

import lejos.nxt.LightSensor;
import lejos.nxt.LCD;
import lejos.nxt.NXTMotor;

public class LightPID {
  public static float uLine = 0;
  public static float kP = 2.0f;
  public static float kI = 0f;
  public static float kD = 1f;

  static int count = 0;
  int innerCount = 0;

  LightSensor sensor;
  NXTMotor motor;

  public int white = 60;
  public int prevError = 0;
  public int accError = 0;

  public LightPID(LightSensor sensor, NXTMotor motor, int white) {
    this.sensor = sensor;
    this.motor = motor;
    this.white = white;
    this.innerCount = count++;
  }

  public int getError(int value, int middle) {
    LCD.clear(innerCount);
    LCD.drawString(value + " " + middle + " " + (value - middle), 0, innerCount);
    return value - middle;
  }

  public float proportional(int error) {
    // Stop motor before crossing the line
    float tmp = kP * error;
    return tmp < -2 && tmp > -8 ?  4 * tmp : tmp;
  }

  public float integral(int error) {
    if (accError * error < 0) accError = 0;
    accError = accError + error;
    return kI * accError;
  }

  public float derivative(int error) {
    return kD * (error - prevError);
  }

  public void turn(float t) {
    float u = uLine + t;
    this.motor.setPower(clamp(u, -100, 100));
  }

  public int clamp(float value, float lower, float upper) {
    return (int) (value > upper ? upper : (value < lower ? lower : value));
  }

  public void run(int middle) {
    int value = sensor.getLightValue();
    int error = getError(value, middle);

    turn((value < this.white)
      ? proportional(error) + integral(error) + derivative(error)
      : 25
    );

    prevError = error;
  }
}
