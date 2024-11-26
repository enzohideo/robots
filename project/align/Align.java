package align;

public class Align {
  LightPID lLightPID;
  LightPID rLightPID;

  public void run(int lMiddle, int rMiddle) {
    while(true) {
      lLightPID.run(lMiddle);
      rLightPID.run(rMiddle);
    }
  }

  public Align(
    LightPID lLightPID,
    LightPID rLightPID
  ) {
    this.lLightPID = lLightPID;
    this.rLightPID = rLightPID;
  }
}
