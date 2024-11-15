/*
 * 
 * Aula 6 - Programa 3
 * 
*/

import lejos.nxt.UltrasonicSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.Motor;
import lejos.nxt.TouchSensor;

public class Ultrasonic {
  public static void main(String[] args) {
    Button.waitForAnyPress();

    TouchSensor toque = new TouchSensor(SensorPort.S3);
    UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);

    while (!toque.isPressed()) {
      if (sonic.getDistance() > 30) {
        Motor.A.forward();
        Motor.B.forward();
      } else {
        Motor.A.stop();
        Motor.B.stop();
      }
    }  
  }
}


