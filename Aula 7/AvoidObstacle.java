class AvoidObstacle implements Behavior {
  private UltrasonicSensor sonar;

  public AvoidObstacle() {        // define o construtor da classe
    sonar = new UltrasonicSensor(SensorPort.S3);
  }

  public boolean takeControl() {
    return sonar.getDistance() < 25 ;  //returns True if an obstacle is detected
  }
  public void suppress() {
    //Since this is the highest priority behavior,
    // suppress will never be called.
  }
  public void action() {
    LCD.clear();
    LCD.drawString("DetectWall",0,1);
    // start Motor.A rotating backward
    ForwardCar.rightMotor.rotate(-360);
  }
}
