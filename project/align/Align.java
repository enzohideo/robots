package align;

public class Align {
  ColorPID lColorPID;
  ColorPID rColorPID;

  public void run(int lMiddle, int rMiddle) {
    while(true) {
      lColorPID.run(lMiddle);
      rColorPID.run(rMiddle);
    }
  }

  public Align(
    ColorPID lColorPID,
    ColorPID rColorPID
  ) {
    this.lColorPID = lColorPID;
    this.rColorPID = rColorPID;
  }
}
