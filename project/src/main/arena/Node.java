package arena;

public class Node {
  public static enum Wall {
    RIGHT(0),
    UP(1),
    LEFT(2),
    DOWN(3);

    byte bitmask = 0;

    Wall(int index) {
      this.bitmask += (1 << (index * 2));
    }

    protected byte getMask() {
      return this.bitmask;
    }
  }
  static byte toggledMask = 0;

  static {
    for (int i = 1; i < 8; i += 2) {
      toggledMask += (1 << i);
    }
  }

  public void toggle() {
    if ((state & toggledMask) != 0)
      state >>= 1;
    else
      state <<= 1;
  }

  public boolean isOpen(Wall wall) {
    return (state & wall.getMask()) != 0;
  }

  byte state = 0;

  public Node(
    boolean openRight,
    boolean openUp,
    boolean openLeft,
    boolean openDown
  ) {
    if (openRight)  state += Wall.RIGHT.getMask();
    if (openUp)     state += Wall.UP.getMask();
    if (openLeft)   state += Wall.LEFT.getMask();
    if (openDown)   state += Wall.DOWN.getMask();
  }
}
