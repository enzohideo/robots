class DriveForward implements Behavior {
  private boolean suppressed = false;
  public boolean takeControl() {
    return true; // this behavior always wants control, no matter the situation
  }
  public void suppress() {
    suppressed = true; // standard practice (some behaviors can do something
    // else before stopping)
  }
  public void action() {
    LCD.clear();
    LCD.drawString("DriveForward",0,1);
    suppressed = false;
    ForwardCar.leftMotor.forward();
    ForwardCar.rightMotor.forward();
    while (!suppressed) {
      Thread.yield(); //don't exit till suppressed
    }
    ForwardCar.leftMotor.stop();
    ForwardCar.rightMotor.stop();
  }
}
