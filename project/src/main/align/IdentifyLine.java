package align;

import lejos.nxt.LightSensor;
import lejos.nxt.LCD;
import lejos.nxt.NXTMotor;
import lejos.robotics.navigation.DifferentialPilot;

public class IdentifyLine {
  LightSensor sensor; 
  //NXTMotor motorLeft;
  //NXTMotor motorRight;
  DifferentialPilot pilot;
  int white = 60;

  void setFloodlight(boolean floodlight) {
    this.sensor.setFloodlight(floodlight);
  }

  public void run() {
    setFloodlight(true);

    pilot.forward();
    while (sensor.getLightValue() > white) {}
    pilot.stop();
    /*boolean leftHasEnded = false;

    while(!leftHasEnded) {
      if (!leftHasEnded && lLightPID.run(lMiddle))
        leftHasEnded = true;
    }*/

    setFloodlight(false);
  }

  public IdentifyLine(LightSensor sensor, DifferentialPilot pilot) { // NXTMotor motorLeft, NXTMotor motorRight
    this.sensor = sensor;
    this.pilot = pilot;
    //this.motorLeft = motorLeft;
    //this.motorRight = motorRight;
    setFloodlight(false);
  }
}
