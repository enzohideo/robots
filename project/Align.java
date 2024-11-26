import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.NXTMotor;

class ColorPID {
  public static float uLine = 0;
  public static float kP = 2.0f;
  public static float kI = 0f;
  public static float kD = 1f;

  ColorSensor sensor;
  NXTMotor motor;

  public int white = 60;
  public int prevError = 0;
  public int accError = 0;

  public ColorPID(ColorSensor sensor, NXTMotor motor, int white) {
    this.sensor = sensor;
    this.motor = motor;
    this.white = white;
  }

  public int getError(int value, int middle) {
    LCD.drawString("error: " + value + " " + middle, 0, 0);
    return (int) ((value - middle) / 1024f * 100f);
  }

  public float proportional(int error) {
    // Stop motor before crossing the line
    float tmp = kP * error;
    return tmp < 0 ? error : tmp;
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
    int value = sensor.getRawLightValue();
    int error = getError(value, middle);

    turn((value < this.white)
      ? proportional(error) + integral(error) + derivative(error)
      : 25
    );

    prevError = error;
  }
}

class Align {
  ColorPID lColorPID;
  ColorPID rColorPID;

  public void run(int lMiddle, int rMiddle) {
    while(true) {
      lColorPID.run(lMiddle);
      rColorPID.run(rMiddle);
    }
  }

  public Align(
    ColorPID lColorPID,
    ColorPID rColorPID
  ) {
    this.lColorPID = lColorPID;
    this.rColorPID = rColorPID;
  }
}
