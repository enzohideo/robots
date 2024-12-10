package claw;

import java.lang.Math;
import java.util.Arrays;

import hardware.Hardware;
import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.LCD;
import lejos.nxt.Button;

public class Claw {
  static int closeAngle = -55;

  ColorSensor colorSensor;
  NXTRegulatedMotor motor;

  public enum Color {
    BLUE,
    RED,
    GREEN,
    YELLOW,
    BROWN,
    UNDEFINED
  }

  public Claw(NXTRegulatedMotor motor, ColorSensor colorSensor) {
    this.motor = motor;
    this.colorSensor = colorSensor;
  }

  double colorDiff(ColorSensor.Color c1, ColorSensor.Color c2) {
    int r = c1.getRed() - c2.getRed();
    int g = c1.getGreen() - c2.getGreen();
    int b = c1.getBlue() - c2.getBlue();
    return r*r + g*g + b*b;
  }

  double colorNorm(ColorSensor.Color c) {
    double r = c.getRed() / 255f;
    double g = c.getGreen() / 255f;
    double b = c.getBlue() / 255f;
    return Math.sqrt(r*r + g*g + b*b);
  }

  public static int classifyColorByDistance(int r, int g, int b) {
    // Prototípicos para amarelo e vermelho
    int[] yellow = {255, 255, 0};
    int[] red = {255, 0, 0};

    // Distâncias euclidianas
    double distanceToYellow = Math.sqrt(Math.pow(r - yellow[0], 2) +
                                        Math.pow(g - yellow[1], 2) +
                                        Math.pow(b - yellow[2], 2));
    double distanceToRed = Math.sqrt(Math.pow(r - red[0], 2) +
                                     Math.pow(g - red[1], 2) +
                                     Math.pow(b - red[2], 2));

    if (distanceToYellow < distanceToRed) {
        return 1;
    } else {
        return 0;
    }
  }


  double normalize(int channel) {
    return (double) channel / 255f;
  }

  double[] rgb2hsv(int r, int g, int b) {
    double nr  = normalize(r);
    double ng  = normalize(g);
    double nb  = normalize(b);

    // Calcula o máximo e o mínimo dos valores normalizados
    double max = Math.max(nr, Math.max(ng, nb));
    double min = Math.min(nr, Math.min(ng, nb));

    // Diferença entre o máximo e o mínimo
    double delta = max - min;

    // Variável para armazenar o valor do Hue
    double hue = 0.0f;

    double saturation = (max == 0) ? 0 : (max - min) / max;

    if (delta == 0) {
        // Se delta é 0, não há matiz (cor neutra)
        hue = 0;
    } else if (max == nr) {
        hue = 60 * (((ng - nb) / delta) % 6);
    } else if (max == ng) {
        hue = 60 * (((nb - nr) / delta) + 2);
    } else if (max == nb) {
        hue = 60 * (((nr - ng) / delta) + 4);
    }

    // Garante que o hue esteja no intervalo [0, 360]
    if (hue < 0) {
        hue += 360;
    }

    double[] hsv = {hue, saturation, max};

    return hsv;

  }

  public static int classifyHue(double hue) {
    //LCD.drawString(Double.toString(hue), 0, 6);
    if (hue >= 180 && hue <= 270) {
      return 2;
    } else if (hue >= 60 && hue < 180) {
        return 1;
    } else {
        return 0;
    }
    /* 
    if (hue >= 0 && hue < 30 || hue >= 330 && hue <= 360) {
      return 0;
      //return Color.RED;
    } else if (hue >= 30 && hue < 90) {
        return 1;
        //return Color.YELLOW;
    } else if (hue >= 90 && hue < 210) {
        return 2;
      //return Color.GREEN;
    } else if (hue >= 210 && hue < 330) {
        return 3;
      //return Color.BLUE;
    } else {
        return 4;
      //return Color.INDEFINED;
    }*/
  }

  public Color getColor() {
    //ColorSensor.Color[] colors = ColorSensor.Color[20];
    int colors[] = {0, 0, 0, 0, 0};
     
    int max = 15;
    int i = 0;
    while(i < max) {
      i += 1;
      int id = colorSensor.getColorID();
      if (id > 3) 
        id = 4;
      else if (id == 2) {
        ColorSensor.Color color = colorSensor.getColor();
        int r = color.getRed();
        int b = color.getBlue();
        int g = color.getGreen();
        double[] hsv = rgb2hsv(r, g, b);
        double hue = hsv[0];
        int index = classifyHue(hue);
        if (index == 1 || index == 2)
          id = index;
      }
      colors[id] += 1;
    }
      /* 
      ColorSensor.Color color = colorSensor.getColor();
      int r = color.getRed();
      int b = color.getBlue();
      int g = color.getGreen();
      //double norm = colorNorm(color);
      LCD.clear();
      LCD.drawString(Integer.toString(r), 0, 5);
      LCD.drawString(Integer.toString(g), 0, 6);
      LCD.drawString(Integer.toString(b), 0, 7);
      try {
        Thread.sleep(500);
      }
      catch (InterruptedException e){
      }
      if (b >= r && b >= g) {
        colors[3] += 1;
      }
      else {
        double[] hsv = rgb2hsv(r, g, b);
        double hue = hsv[0];
        double saturation = hsv[1];
        double brightness = hsv[2];
        int index = classifyHue(hue, saturation);
        if (index == 0){
          index = classifyColorByDistance(r, g, b);
        }
        colors[index] += 1;
      }
    }*/
    int max_index = -1;
    int max_value = -1;
    for (int j = 0; j < 5; j++) {
      if (colors[j] > max_value) {
        max_value = colors[j];
        max_index = j;
      }
    }

    //int id = colorSensor.getColorID();
    
    //LCD.drawString(Integer.toString(id), 0, 6);

    switch (max_index) {
      case 0:
        return Color.RED;
      case 1:
        return Color.GREEN;
      case 2:
        return Color.BLUE;
      case 3:
        return Color.YELLOW;
      case 4:
      default:
        return Color.UNDEFINED;
    }
  }

  public Color run(boolean close) {
    this.motor.setSpeed(80);

    if (close)
      this.motor.rotateTo(closeAngle);
    else
      this.motor.rotateTo(0);

    return getColor();
  }

  public static void main(String[] args) {
    LCD.drawString("Color Sensor", 0, 0);
    Button.waitForAnyPress();

    NXTRegulatedMotor clawMotor = new NXTRegulatedMotor(Hardware.clawMotorPort);
    ColorSensor colorSensor = new ColorSensor(Hardware.clawColorSensorPort);
    Claw claw = new Claw(clawMotor, colorSensor);

    claw.run(true);

    while (true) {
      //LCD.clear();
      Color c = claw.getColor();
      if (c == Color.RED) {
        LCD.drawString("RED", 0, 1);
        LCD.clear(2);
        LCD.clear(3);
        LCD.clear(4);
        //LCD.clear(5);
      }
      else if (c == Color.GREEN) {
        LCD.drawString("GREEN", 0, 2);
        LCD.clear(1);
        LCD.clear(3);
        LCD.clear(4);
        //LCD.clear(5);
      }
      else if (c == Color.BLUE) {
        LCD.drawString("BLUE", 0, 3);
        LCD.clear(2);
        LCD.clear(1);
        LCD.clear(4);
        //LCD.clear(5);
      }
      else if (c == Color.YELLOW) {
        LCD.drawString("YELLOW", 0, 4);
        LCD.clear(2);
        LCD.clear(3);
        LCD.clear(1);
        //LCD.clear(5);
      }
      else if (c == Color.UNDEFINED) {
        LCD.drawString("UNDEFINED", 0, 5);
        LCD.clear(2);
        LCD.clear(3);
        LCD.clear(4);
        LCD.clear(1);
      }
    }
  }
}
