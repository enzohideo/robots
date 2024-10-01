/*
 *
 * Aula 6 - Programa 1
 *
*/

import lejos.nxt.LCD;
import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.util.Delay;
import lejos.nxt.Motor;
import lejos.robotics.navigation.*;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public class Atividade1 {
  public static void main(String[] args) {
    Button.waitForAnyPress();

    LCD.clear();
    LCD.drawString("Sensor de Toque", 0, 0);

    TouchSensor toque = new TouchSensor(SensorPort.S3);
    UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
    DifferentialPilot pilot = new DifferentialPilot(5.6, 11.2, Motor.A, Motor.B);

    pilot.forward();
    while (sonic.getDistance() > 30) {}
    pilot.stop();

    pilot.arc(30, -90, true);

    while (!toque.isPressed() && pilot.isMoving()) {}
    if (toque.isPressed()) {
      LCD.drawString("bateu na parede", 0, 1);
      pilot.stop();
      Sound.playTone(500, 2000);
    }
    Delay.msDelay(1000);

    pilot.arc(-30, -90, false);
    pilot.travel(30, false);
  }
}
