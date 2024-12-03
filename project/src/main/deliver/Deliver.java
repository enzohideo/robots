package deliver;

import deliver.Arena.Location;
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
}
