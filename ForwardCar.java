import lejos.nxt.*;
import lejos.robotics.subsumption.behavior; // importa a interface behavior
import lejos.robotics.subsumption.arbitrator; // importa a classe arbitrator
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
