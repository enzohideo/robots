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
      LCD.drawString(color.toString(), 0, 1);
    }
  }
}
