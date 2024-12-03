package arena;

import arena.Arena.Location;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.pathfinding.Path;

public class PathFinder {
  private Navigator navigator;
  private Arena arena;

  public PathFinder(Navigator navigator) {
    this.navigator = navigator;
    this.arena = new Arena();
  }

  public void run(float x, float y, Location location) {
    Path path = this.arena.findRoute(x, y, location);
    navigator.getPoseProvider().setPose(new Pose(x, y, 0));
    navigator.clearPath();
    navigator.followPath(path);
  }
}