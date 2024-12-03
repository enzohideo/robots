package arena;

// TODO: Implement matrix of Nodes
public class Node {
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
