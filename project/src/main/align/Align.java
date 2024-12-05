package align;

public class Align {
  LightPID lLightPID;
  //LightPID rLightPID;

  void setFloodlight(boolean floodlight) {
    this.lLightPID.sensor.setFloodlight(floodlight);
    //this.rLightPID.sensor.setFloodlight(floodlight);
  }

  public void run(int lMiddle, int rMiddle) {
    setFloodlight(true);

    boolean leftHasEnded = false;
    //boolean rightHasEnded = false;

    while(!leftHasEnded) {
      if (!leftHasEnded && lLightPID.run(lMiddle))
        leftHasEnded = true;
      //if (!rightHasEnded && rLightPID.run(rMiddle))
        //rightHasEnded = true;
    }

    setFloodlight(false);
  }

  public Align(
    LightPID lLightPID
    //LightPID rLightPID
  ) {
    this.lLightPID = lLightPID;
    //this.rLightPID = rLightPID;
    setFloodlight(false);
  }
}
