package dijkstra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dijkstra {
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

      queue.remove((Integer) current);

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

    for (int i = 0; i < path.size() / 2; ++i) {
      int j = path.size() - i - 1;
      path.set(i, path.get(i) ^ path.get(j));
      path.set(j, path.get(i) ^ path.get(j));
      path.set(i, path.get(i) ^ path.get(j));
    }

    if (path.size() == 0 || path.get(0) != start) {
      return new ArrayList<>();
    }

    return path;
  }
}
