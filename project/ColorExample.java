import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;

public class ColorExample {
  static ColorSensor colorSensor;

  public static void main(String[] args) {
    colorSensor = new ColorSensor(SensorPort.S1);

    LCD.drawString("Color Sensor", 0, 0);
    Button.waitForAnyPress();

    while(true) {
      Color color = colorSensor.getColor();
      LCD.drawString("red: " + Integer.toString(color.getRed()), 0, 1);
      LCD.drawString("green: " + Integer.toString(color.getGreen()), 0, 2);
      LCD.drawString("blue: " + Integer.toString(color.getBlue()), 0, 3);
      try {
        Thread.sleep(1000);
      } catch(InterruptedException error) {
        LCD.drawString("failed to sleep", 0, 1);
      }
    }
  }
}
