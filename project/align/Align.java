package align;

public class Align {
  LightPID lLightPID;
  LightPID rLightPID;

  public void run(int lMiddle, int rMiddle) {
    boolean leftHasEnded = false;
    boolean rightHasEnded = false;
    while(!leftHasEnded && !rightHasEnded) {
      if (!leftHasEnded && lLightPID.run(lMiddle))
        leftHasEnded = true;
      if (!rightHasEnded && rLightPID.run(rMiddle))
        rightHasEnded = true;
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
