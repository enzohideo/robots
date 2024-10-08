import lejos.nxt.*;
import lejos.robotics.subsumption.*;
import lejos.robotics.navigation.*;
import lejos.robotics.mapping.*;
import lejos.robotics.pathfinding.*;
import lejos.robotics.localization.*;

class AvoidObstacle implements Behavior {
  private UltrasonicSensor sonar;
  private boolean right = true;
  private boolean suppressed = true;

  public AvoidObstacle() {
    sonar = new UltrasonicSensor(SensorPort.S1);
  }

  public boolean takeControl() {
    return sonar.getDistance() < 25 ;  //returns True if an obstacle is detected
  }

  public void suppress() {
    suppressed = true;
    //Since this is the highest priority behavior,
    // suppress will never be called.
  }

  public void action() {
    LCD.clear();
    LCD.drawString("DetectWall",0,1);

    while (sonar.getDistance() < 30) {
      ForwardCar.pilot.rotate(right ? -87 : 87);
    }

    ForwardCar.pilot.travel(30);

    right = !right;
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
    ForwardCar.navigator.clearPath();
  }
  public void action() {
    LCD.clear();
    LCD.drawString("DriveForward",0,1);
    suppressed = false;

    // try {
      // Pose current_pose = poseProvider.getPose();
      // ForwardCar.navigator.addWaypoint(current_pose.getX(), current_pose.getY());
    ForwardCar.navigator.goTo(200, 238);
      // ForwardCar.navigator.setPath(pathFinder.findRoute(poseProvider.getPose(), target_waypoint));
    // } catch (Exception e) {
      //TODO: handle exception
    // }
    // ForwardCar.navigator.followPath();

    while (!suppressed) {
      Thread.yield(); //don't exit till suppressed
    }
  }
}

public class ForwardCar {

  // renaming  instances of Motor.A and Motor.B de ...
  static NXTRegulatedMotor leftMotor = Motor.A;
  static NXTRegulatedMotor rightMotor = Motor.B;
  static DifferentialPilot pilot;
  // static OdometryPoseProvider poseProvider;
  static Navigator navigator;
  static Waypoint target_waypoint;

  public static void main(String[] args) {
    pilot = new DifferentialPilot(5.6, 11.2, Motor.B, Motor.A, false);
    // poseProvider = new OdometryPoseProvider(pilot);
    navigator = new Navigator(pilot);
    target_waypoint = new Waypoint(100, 100);

    pilot.setRotateSpeed(35.);
    pilot.setTravelSpeed(10.);

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
