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

  public static int middle_value = 49;

  public static float u_line = 25f;
  public static float k_p = 0.5f;
  public static float k_i = 0f;
  public static float k_d = 0.001f;

  public static int prev_error_left = 0;
  public static int prev_error_right = 0;
  public static int acc_error_left = 0;
  public static int acc_error_right = 0;

  public static int error(LightSensor light_) {
    return light_.getLightValue() - middle_value;
  }

  public static float proportional(int e) {
    return k_p * e;
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
      int error_right = error(light_right);
      int error_left = error(light_left);

      float t_right = proportional(error_right) +
        integral(error_right, Side.RIGHT) +
        derivative(error_right, Side.RIGHT);

      float t_left = proportional(error_left) +
        integral(error_left, Side.LEFT) +
        derivative(error_left, Side.LEFT);

      turn(motor_right, t_right);
      turn(motor_left, t_left);

      prev_error_left = error_left;
      prev_error_right = error_right;
    }
  }
}

