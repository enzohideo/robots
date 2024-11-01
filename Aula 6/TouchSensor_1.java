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

public class TouchSensor_1 {
  public static void main(String[] args) {
    Button.waitForAnyPress();

    LCD.clear();
    LCD.drawString("Sensor de Toque", 0, 0);

    TouchSensor toque = new TouchSensor(SensorPort.S3); //construtor do sensor de toque
    DifferentialPilot pilot = new DifferentialPilot(2.205 * 2.56, 4.527 * 2.56,        
                               Motor.A, Motor.B);
    pilot.travel(30, false);    
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
