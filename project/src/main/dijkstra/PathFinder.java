package dijkstra;

import dijkstra.Arena.Location;
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
    int final_index = this.arena.location2Index(location);
    int initial_index = this.arena.initialIndex(y);

    this.arena.toggle_door(location, true);

    Path path = this.arena.findRoute(initial_index, final_index);
    navigator.getPoseProvider().setPose(new Pose(x, y, 0));
    navigator.clearPath();
    navigator.followPath(path);

    this.arena.toggle_door(location, false);
  }
}
