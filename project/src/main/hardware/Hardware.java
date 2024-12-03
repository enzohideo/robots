package hardware;

import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;

public class Hardware {
  public static SensorPort ultrasonicSensorPort = SensorPort.S2;

  public static MotorPort lMotorPort = MotorPort.A;
  public static MotorPort rMotorPort = MotorPort.C;
  public static MotorPort clawMotorPort = MotorPort.B;

  public static SensorPort lLightSensorPort = SensorPort.S3;
  public static SensorPort rLightSensorPort = SensorPort.S4;
  public static SensorPort clawColorSensorPort = SensorPort.S1;

  public static double wheelDiameter = 6.0;
  public static double trackWidth = 12.0;
}
