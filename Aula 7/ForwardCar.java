import lejos.nxt.*;
import lejos.robotics.subsumption.*;

class AvoidObstacle implements Behavior {
  private UltrasonicSensor sonar;

  public AvoidObstacle() {
    sonar = new UltrasonicSensor(SensorPort.S1);
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

public class ForwardCar {

  // renaming  instances of Motor.A and Motor.B de ...
  static NXTRegulatedMotor leftMotor = Motor.A;
  static NXTRegulatedMotor rightMotor = Motor.B;
  public static void main(String[] args) {
    leftMotor.setSpeed(300);
    rightMotor.setSpeed(300);
    // create objects  b1 and b2
    Behavior b1 = new DriveForward();
    Behavior b2 = new AvoidObstacle();
    // create a list of behaviors: the order indicates the priority
    Behavior[] behaviorList = { b1, b2 };
    // create a new arbitrator passing the behavior list
    Arbitrator arby = new Arbitrator(behaviorList);
    arby.start();
  }

}
