/*
 * 
 * Aula 6 - Programa 2
 * 
*/

import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.util.Delay;
import lejos.nxt.LCD; 
import lejos.nxt.SensorPort;
import lejos.nxt.Button;
import lejos.nxt.TouchSensor;

public class Light1 {
  public static void main(String[] args) {
    Button.waitForAnyPress();

    LCD.drawString("Sensor Optico", 0, 0); 

    TouchSensor toque = new TouchSensor(SensorPort.S3);
    LightSensor light = new LightSensor(SensorPort.S4, true);

    while (!toque.isPressed())  {
      int value = light.readValue();
      System.out.println(value);
      if (value > 10 && value < 40)
        Sound.playTone(200,500);
      else if (value > 50 && value < 80)
        Sound.playTone(500,500);
      Delay.msDelay(1000);
    }
  }
}


