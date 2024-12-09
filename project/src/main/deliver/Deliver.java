package deliver;

import deliver.Arena.Location;
import hardware.Hardware;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class Deliver {
  private Navigator navigator;
  private Arena deliver;

  public Deliver(Navigator navigator) {
    this.navigator = navigator;
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

    pilot.setRotateSpeed(40);
    pilot.setTravelSpeed(10);

    Navigator navigator = new Navigator(pilot);
    Deliver deliver = new Deliver(navigator);

    for (Location location : Arena.Location.values()) {
      LCD.drawString("Location " + location.name(), 0, 0);
      Button.waitForAnyPress();

      Path path = deliver.run(0, 0, location);
      Path reversePath = new Path();
      for (int i = 0; i < path.size() - 1; ++i) {
        Waypoint wp = path.get(i);
        reversePath.add(0, wp);
      }

      navigator.getPoseProvider().setPose(new Pose(0, 0, 0));
      navigator.clearPath();
      navigator.followPath(path);
      navigator.waitForStop();

      pilot.rotate(180);

      navigator.clearPath();
      navigator.followPath(reversePath);
    }
  }
}
