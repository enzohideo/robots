import lejos.nxt.Button;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lejos.nxt.NXTMotor;
import java.lang.Math;

enum Side {
  LEFT,
  RIGHT
}

public class Align {
  static NXTMotor motor_right, motor_left;
  static LightSensor light_right;
  static LightSensor light_left;

  public static int light_value_left = 49;
  public static int light_value_right = 55;
  public static int middle_value_left = 49;
  public static int middle_value_right = 55;

  public static float u_line = 0;
  public static float k_p = 2.5f;
  public static float k_i = 0f;
  public static float k_d = 1f;

  public static int prev_error_left = 0;
  public static int prev_error_right = 0;
  public static int acc_error_left = 0;
  public static int acc_error_right = 0;

  public static int error(Side side) {
    if (side == Side.LEFT) {
      return light_value_left - middle_value_left;
    } else {
      return light_value_right- middle_value_right;
    }
  }

  public static float proportional(int e) {
    // Stop motor before crossing the line
    float tmp = k_p * e;
    return tmp < 0 ? e : tmp;
  }

  public static float integral(int e, Side side) {
    if (side == Side.LEFT) {
      if (acc_error_left * e < 0) acc_error_left = 0;
      acc_error_left = acc_error_left + e;
      return k_i * acc_error_left;
    } else {
      if (acc_error_right * e < 0) acc_error_right = 0;
      acc_error_right = acc_error_right + e;
      return k_i * acc_error_right;
    }
  }

  public static float derivative(int e, Side side) {
    if (side == Side.LEFT) {
      return k_d * (e - prev_error_left);
    } else {
      return k_d * (e - prev_error_right);
    }
  }

  public static int clamp(float value, float lower, float upper) {
    return (int) (value > upper ? upper : (value < lower ? lower : value));
  }

  public static void turn(NXTMotor motor, float t) {
    float u = u_line + t;
    motor.setPower(clamp(u, -100, 100));
  }

  public static void main(String [] args){
    Button.waitForAnyPress();

    light_right = new LightSensor(SensorPort.S2, true);
    light_left = new LightSensor(SensorPort.S3, true);
    motor_right = new NXTMotor(MotorPort.A);
    motor_left = new NXTMotor(MotorPort.C);

    while (true) {
      light_value_left = light_left.getLightValue();
      light_value_right = light_right.getLightValue();

      LCD.drawString("Left", 0, 0);
      LCD.drawInt(light_value_left,  0, 1);
      LCD.drawString("Right", 0, 2);
      LCD.drawInt(light_value_right,  0, 3);

      int error_right = error(Side.RIGHT);
      int error_left = error(Side.LEFT);

      float t_right = (light_value_right < 60)
        ? proportional(error_right) +
          integral(error_right, Side.RIGHT) +
          derivative(error_right, Side.RIGHT)
        : 25;

      float t_left = (light_value_left < 60)
        ? proportional(error_left) +
          integral(error_left, Side.LEFT) +
          derivative(error_left, Side.LEFT)
        : 25;

      turn(motor_right, t_right);
      turn(motor_left, t_left);

      prev_error_left = error_left;
      prev_error_right = error_right;
    }
  }
}

