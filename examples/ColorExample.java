import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;

public class ColorExample {
  static ColorSensor colorSensor;

  public static void main(String[] args) {
    colorSensor = new ColorSensor(SensorPort.S3);

    LCD.drawString("Color Sensor", 0, 0);

    while(true) {
      Color color = colorSensor.getColor();

      LCD.drawString("red: " + Integer.toString(color.getRed()), 0, 1);
      LCD.drawString("green: " + Integer.toString(color.getGreen()), 0, 2);
      LCD.drawString("blue: " + Integer.toString(color.getBlue()), 0, 3);
      LCD.drawString("light: " + Integer.toString(colorSensor.getLightValue()), 0, 4);
      LCD.drawString("light: " + Float.toString(colorSensor.getNormalizedLightValue()), 0, 5);
      LCD.drawString("light: " + Integer.toString(colorSensor.getRawLightValue()), 0, 6);

      try {
        Thread.sleep(1000);
      } catch(InterruptedException error) {
        LCD.drawString("failed to sleep", 0, 1);
      }
    }
  }
}
