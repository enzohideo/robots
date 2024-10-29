import lejos.nxt.Button;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lejos.nxt.NXTMotor;
import java.lang.Math;

public class FollowWall{
  static NXTMotor mRight, mLeft;
  static UltrasonicSensor sensor;

  public static int middle_value = 30;

  public static float u_line = 40f;
  public static float k_p = 0.5f;
  public static float k_i = 0f;
  public static float k_d = 0.001f;

  public static int prev_e = 0;
  public static int acc_e = 0;

  public static int error() {
    return sensor.getDistance() - middle_value;
  }

  public static float proportional(int e) {
    return k_p * e;
  }

  public static float integral(int e) {
    if (acc_e * e < 0) acc_e = 0;
    acc_e = acc_e + e;
    return k_i * acc_e;
  }

  public static float derivative(int e) {
    return k_d * (e - prev_e);
  }

  public static int clamp(float value, float lower, float upper) {
    return (int) (value > upper ? upper : (value < lower ? lower : value));
  }

  public static void turn(float t) {
    float u = u_line + t;
    mRight.setPower(clamp(u, -100, 100));

    u = u_line - t;
    mLeft.setPower(clamp(u, -100, 100));
  }

  public static void main(String [] args){
    Button.waitForAnyPress();

    sensor = new UltrasonicSensor(SensorPort.S1);
    mRight = new NXTMotor(MotorPort.A);
    mLeft = new NXTMotor(MotorPort.C);

    while (true) {
      int e = error();
      float t = proportional(e) + integral(e) + derivative(e);
      turn(t);
      prev_e = e;
    }
  }
}

