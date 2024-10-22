import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lejos.nxt.NXTMotor;

// black  40~41
// middle 47-52
// white  61~62

public class FollowLine{
  public static void main(String [] args){
    LightSensor light = new LightSensor(SensorPort.S4);
    Button.waitForAnyPress();

    NXTMotor mA = new NXTMotor(MotorPort.A);
    NXTMotor mC = new NXTMotor(MotorPort.C);

    while (true) {
      int light_value = light.getLightValue();

      LCD.clear();
      LCD.drawString("Value", 0, 0);
      LCD.drawInt(light_value, 0, 1);

      if (light_value < 45) {
        mA.setPower(0);
        mC.setPower(30);
      } else if (light_value > 55) {
        mA.setPower(30);
        mC.setPower(0);
      } else {
        mA.setPower(30);
        mC.setPower(30);
      }
    }
  }
}

