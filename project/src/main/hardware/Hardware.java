package hardware;

import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;

public class Hardware {
  public static SensorPort ultrasonicSensorPort = SensorPort.S2;

  public static MotorPort lMotorPort = MotorPort.A;
  public static MotorPort rMotorPort = MotorPort.C;
  public static MotorPort clawMotorPort = MotorPort.B;

  public static SensorPort compassPort = SensorPort.S4;
  public static SensorPort lLightSensorPort = SensorPort.S3;
  public static SensorPort rLightSensorPort = SensorPort.S4;
  public static SensorPort clawColorSensorPort = SensorPort.S1;

  public static double wheelDiameter = 5.6;
  public static double trackWidth = 11.62;

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      LCD.drawString("Failed to sleep", 0, 0);
    }
  }

}
