package align;

import java.util.Arrays;
import lejos.nxt.LightSensor;
import hardware.Hardware;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

public class IdentifyLine {
  LightSensor sensor;
  DifferentialPilot pilot;

  void setFloodlight(boolean floodlight) {
    this.sensor.setFloodlight(floodlight);
  }

  public void run(int lineColor) {
    setFloodlight(true);
    int[] lightValues = new int[16];
    Arrays.fill(lightValues, 90);
    int index = 0;

    pilot.setTravelSpeed(5);
    pilot.forward();

    int lightSum = 90 * lightValues.length;
    double lightAvg;
    do {
        int lightValue = sensor.getLightValue();
        lightSum = lightSum + lightValue - lightValues[index];
        lightValues[index++] = lightValue;
        index = index % lightValues.length;
        lightAvg = lightSum / lightValues.length;
        LCD.drawString("light " + lightAvg, 0, 0);
    } while (lightAvg > lineColor);

    pilot.stop();

    setFloodlight(false);
  }

  public IdentifyLine(LightSensor sensor, DifferentialPilot pilot) { // NXTMotor motorLeft, NXTMotor motorRight
    this.sensor = sensor;
    this.pilot = pilot;
    setFloodlight(false);
  }

  public static void main(String[] args) {
    NXTRegulatedMotor lMotor = new NXTRegulatedMotor(Hardware.lMotorPort);
    NXTRegulatedMotor rMotor = new NXTRegulatedMotor(Hardware.rMotorPort);
    DifferentialPilot pilot = new DifferentialPilot(
      Hardware.wheelDiameter, Hardware.trackWidth, lMotor, rMotor
    );
    LightSensor sensor = new LightSensor(Hardware.lLightSensorPort);
    IdentifyLine idline = new IdentifyLine(sensor, pilot);
    
    pilot.setRotateSpeed(40);
    pilot.setTravelSpeed(5);

    Button.waitForAnyPress();
    idline.run(45);
    Button.waitForAnyPress();
    
  }
}
