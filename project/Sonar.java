import lejos.nxt.LCD;
import lejos.nxt.NXTMotor;
import lejos.nxt.UltrasonicSensor;

class Sonar {
  public enum Pipe {
    SHORT, TALL
  }

  private UltrasonicSensor sonar;
  private NXTMotor lMotor;
  private NXTMotor rMotor;
  private float shortPipeThreshold = 30.0f;
  private float tallPipeThreshold = 15.0f;
  private int power = 25;
  private int samplingPeriod = 100;

  public Sonar(UltrasonicSensor sonar, NXTMotor lMotor, NXTMotor rMotor) {
    this.sonar = sonar;
    this.lMotor = lMotor;
    this.rMotor = rMotor;
  }

  public Pipe run() {
    this.rMotor.setPower(power);
    this.lMotor.setPower(power);

    Pipe result;

    while (true) {
      float distance = this.sonar.getDistance();

      LCD.drawString("Sonar: " + Float.toString(distance), 0, 0);

      if (distance < this.tallPipeThreshold) {
        result = Pipe.TALL;
        break;
      } else if (distance < this.shortPipeThreshold) {
        result = Pipe.SHORT;
        break;
      }

      try {
        Thread.sleep(samplingPeriod);
      } catch (InterruptedException error) {
        LCD.drawString("Sonar ERROR: interrupted", 0, 0);
      }
    }

    this.rMotor.setPower(0);
    this.lMotor.setPower(0);

    return result;
  }
}
