package deliver;

import deliver.Arena.Location;
import hardware.Hardware;
import lejos.nxt.Button;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.pathfinding.Path;

public class Deliver {
  private Navigator navigator;
  private Arena deliver;

  public Deliver(Navigator navigator) {
    this.navigator = navigator;
    this.deliver = new Arena();
  }

  public void run(float x, float y, Location location) {
    Path path = this.deliver.findRoute(x, y, location);
    navigator.getPoseProvider().setPose(new Pose(x, y, 0));
    navigator.clearPath();
    navigator.followPath(path);
    navigator.waitForStop();
  }

  public static void main(String[] args) {
    Button.waitForAnyPress();
    NXTRegulatedMotor lRegulatedMotor = new NXTRegulatedMotor(Hardware.lMotorPort);
    NXTRegulatedMotor rRegulatedMotor = new NXTRegulatedMotor(Hardware.rMotorPort);
    DifferentialPilot pilot = new DifferentialPilot(
      Hardware.wheelDiameter, Hardware.trackWidth, lRegulatedMotor, rRegulatedMotor, true
    );
    Navigator navigator = new Navigator(pilot);
    (new Deliver(navigator)).run(0, 0, Location.MUSEUM);
  }
}
