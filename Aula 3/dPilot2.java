import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class dPilot2 {

    public static void main(String[] args) {
        LCD.drawString("D.P. - ROTATE", 0, 0);
        Button.waitForAnyPress();
        DifferentialPilot pilot = new DifferentialPilot(2.205f, 4.527f, 
                               Motor.C, Motor.B, false); //in inches
        LCD.clear();
        pilot.setRotateSpeed(360);
        pilot.rotate(90, false); // 90 degrees anti-clockwise
        Button.waitForAnyPress();
    }
}

