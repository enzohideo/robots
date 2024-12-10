package deliver;

import align.IdentifyLine;
import deliver.Arena.Location;
import hardware.Hardware;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class Deliver {
  private Arena deliver;

  public Deliver(Navigator navigator) {
    this.deliver = new Arena();
  }

  // TODO: Try generator function
  public Path run(float x, float y, Location location) {
    return this.deliver.findRoute(x, y, location);
  }

  public static void main(String[] args) {
    Button.waitForAnyPress();
    NXTRegulatedMotor lRegulatedMotor = new NXTRegulatedMotor(Hardware.lMotorPort);
    NXTRegulatedMotor rRegulatedMotor = new NXTRegulatedMotor(Hardware.rMotorPort);
    DifferentialPilot pilot = new DifferentialPilot(
      Hardware.wheelDiameter, Hardware.trackWidth, lRegulatedMotor, rRegulatedMotor, true
    );
    DifferentialPilot reversePilot = new DifferentialPilot(
      Hardware.wheelDiameter, Hardware.trackWidth, lRegulatedMotor, rRegulatedMotor, false
    );
    LightSensor lLightSensor = new LightSensor(Hardware.lLightSensorPort);
    IdentifyLine idLine = new IdentifyLine(lLightSensor, reversePilot);

    Navigator navigator = new Navigator(pilot);
    Deliver deliver = new Deliver(navigator);

    for (Location location : Arena.Location.values()) {
      LCD.drawString("Location " + location.name(), 0, 0);
      Button.waitForAnyPress();

      idLine.run(35);

      pilot.setRotateSpeed(40);
      pilot.rotate(90);

      idLine.run(30);

      pilot.setRotateSpeed(40);
      pilot.rotate(-88);

      float x = 8f;
      float y = 5f;

      pilot.setRotateSpeed(40);
      pilot.setTravelSpeed(10);

      Button.waitForAnyPress();

      Path path = deliver.run(0, 0, location);
      Path reversePath = new Path();
      for (int i = 0; i < path.size() - 1; ++i) {
        Waypoint wp = path.get(i);
        reversePath.add(0, wp);
      }

      navigator.getPoseProvider().setPose(new Pose(x, y, 0));
      navigator.clearPath();
      navigator.followPath(path);
      navigator.waitForStop();

      pilot.rotate(180);
      Pose pose = navigator.getPoseProvider().getPose();
      pose.setHeading(Math.round(pose.getHeading() / 90.0) * 90);

      navigator.clearPath();
      navigator.followPath(reversePath);
      navigator.waitForStop();
    }
  }
}
