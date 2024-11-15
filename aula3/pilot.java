import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;

public class pilot {
  public static void main(String[] args) {
    LCD.drawString("D.P. - SQUARE", 0, 0);
    Button.waitForAnyPress();
        Delay.msDelay(2000);

    DifferentialPilot pilot = new DifferentialPilot(
      2.20472f, 4.409449f, Motor.C, Motor.B, false
    );

    LCD.clear();

    pilot.setTravelSpeed(4); //4
    pilot.setRotateSpeed(60);

    for (int i = 0; i < 4; ++i) {
      pilot.travel(19.685, false);
      pilot.rotate(86, false);
    }

    pilot.stop();
  }
}

