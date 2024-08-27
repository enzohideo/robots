import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class dPilot1 {

	public static void main(String[] args) {
		LCD.drawString("D.P. - TRAVEL", 0, 0);
		Button.waitForAnyPress();
		DifferentialPilot pilot = new DifferentialPilot(2.205f, 4.527f, 
                                 Motor.C, Motor.B, false); //in inches
		LCD.clear();
		pilot.setTravelSpeed(4); //4  
		pilot.travel(15.75, false); //approx. 40cm
		pilot.stop();
		Button.waitForAnyPress();
	}
}

