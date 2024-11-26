package dijkstra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dijkstra {
  // TODO: Implement matrix of Nodes
  public static class Node {
    public static enum Wall {
      RIGHT,
      UP,
      LEFT,
      DOWN
    }

    byte state;

    static int wallToState(Wall wall) {
      switch(wall) {
        case RIGHT:
          return 1;
        case UP:
          return 1 << 2;
        case LEFT:
          return 1 << 4;
        case DOWN:
          return 1 << 6;
      }
      return 0;
    }

    public boolean isOpen(Wall wall) {
      return (state & wallToState(wall)) != 0;
    }

    static byte createState(
      boolean right,
      boolean up,
      boolean left,
      boolean down
    ) {
      byte state = 0;

      if (right)  state += wallToState(Wall.RIGHT);
      if (up)     state += wallToState(Wall.UP);
      if (left)   state += wallToState(Wall.LEFT);
      if (down)   state += wallToState(Wall.DOWN);

      return state;
    }

    public Node(
      boolean openRight,
      boolean openUp,
      boolean openLeft,
      boolean openDown
    ) {
      state = createState(openRight, openUp, openLeft, openDown);
    }
  }

  public static List<Integer> findRoute(boolean[][] adjMatrix, int start, int end) {
    int n = adjMatrix.length;

    int[] distances = new int[n];
    Arrays.fill(distances, Integer.MAX_VALUE);
    distances[start] = 0;

    int[] previous = new int[n];
    Arrays.fill(previous, -1);

    boolean[] visited = new boolean[n];

    List<Integer> queue = new ArrayList<>();
    queue.add(start);

    while (!queue.isEmpty()) {
      int current = -1;
      int minDistance = Integer.MAX_VALUE;
      for (int vertex : queue) {
        if (distances[vertex] >= minDistance) continue;
        minDistance = distances[vertex];
        current = vertex;
      }

      queue.remove(current);

      if (visited[current]) continue;
      visited[current] = true;

      for (int neighbor = 0; neighbor < n; neighbor++) {
        if (adjMatrix[current][neighbor] && !visited[neighbor]) {
          int newDist = distances[current] + 1; // Peso uniforme (1)
          if (newDist < distances[neighbor]) {
            distances[neighbor] = newDist;
            previous[neighbor] = current;
            if (!queue.contains(neighbor)) {
              queue.add(neighbor);
            }
          }
        }
      }
    }

    List<Integer> path = new ArrayList<>();
    for (int at = end; at != -1; at = previous[at]) {
      path.add(at);
    }

    if (path.isEmpty() || path.get(0) != start) {
      return new ArrayList<>();
    }

    for (int i = 0; i < path.size() / 2; ++i) {
      int j = path.size() - i - 1;
      path.set(i, path.get(i) ^ path.get(j));
      path.set(j, path.get(i) ^ path.get(j));
      path.set(i, path.get(i) ^ path.get(j));
    }

    return path;
  }
}
