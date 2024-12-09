package sonar;

import hardware.Hardware;
import java.util.Arrays;

import align.IdentifyLine;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Sonar {
  public enum Pipe {
    SHORT, TALL
  }

  private UltrasonicSensor sonar;

  DifferentialPilot pilot;

  // TODO: Distinguish short from tall pipes
  private float[] shortPipeThreshold = { 220f, 27f };
  private float[] tallPipeThreshold = { 42f, 16f };

  class Distances {
    int[] array;
    int sum = 0;
    int head = 0;
    int size = 0;

    public Distances(int size) {
      this.size = size;
      this.array = new int[this.size];
      reset();
    }

    public void reset() {
      this.sum = 255 * this.size;
      Arrays.fill(this.array, 255);
    }

    public void add(int value) {
      this.head = (this.head + 1) % this.size;
      this.sum = this.sum + value - this.array[this.head];
      this.array[this.head] = value;
    }

    public float avg() {
      return (float) this.sum / this.size;
    }

    public float var() {
      float mean = this.avg();
      float  sum = 0;
      for (int distance : this.array) {
        float delta = distance - mean;
        sum += delta * delta;
      }
      return sum / (this.size - 1);
    }

    public int min() {
      int minDistance = Integer.MAX_VALUE;
      for (int i : array) {
        if (i < minDistance)
          minDistance = i;
      }
      return minDistance;
    }
  }
  Distances distances;

  public Sonar(UltrasonicSensor sonar, DifferentialPilot pilot) {
    this.distances = new Distances(32);
    this.sonar = sonar;
    this.pilot = pilot;
    this.sonar.continuous();
  }

  public Pipe run() {
    this.distances.reset();
    this.pilot.setTravelSpeed(3); // 2.5
    this.pilot.forward();
    this.sonar.continuous();

    Pipe pipe = earlyDetection();

    // 2nd stage: Alignment with the pipe.
    // Walk forward until it's next to the pipe
    lateDetection(pipe);

    this.pilot.stop();
    this.sonar.off();

    return pipe;
  }

  Pipe earlyDetection() {
    while (true) {
      int distance = this.sonar.getDistance();

      LCD.clear(0);
      LCD.clear(1);
      LCD.clear(2);
      LCD.clear(3);
      LCD.drawString("Dist: " + Integer.toString(distance), 0, 2);

      distances.add(distance);
      LCD.drawString("Min: " + Integer.toString(distances.min()), 0, 1);
      LCD.drawString("Avg: " + Float.toString(distances.avg()), 0, 0);
      LCD.drawString("Var: " + Float.toString(distances.var()), 0, 3);

      // float avg = distances.avg();

      if (distances.var() > 4e3) continue;

      double avg = distances.avg();

      if (avg < this.tallPipeThreshold[0]) {
        LCD.drawString("TALL", 0, 4);
        return Pipe.TALL;
      } else if (avg < this.shortPipeThreshold[0]) {
        LCD.drawString("SHORT", 0, 4);
        return Pipe.SHORT;
      }
    }
  }

  void lateDetection(Pipe pipe) {
    float distance;
    switch(pipe) {
      case TALL:
        do {
          distance = this.sonar.getDistance();
          LCD.drawString("Sonar: " + Float.toString(distance), 0, 2);
        } while (distance > this.tallPipeThreshold[1]);
        break;
      default:
      case SHORT:
        do {
          distance = this.sonar.getDistance();
          LCD.drawString("Sonar: " + Float.toString(distance), 0, 2);
        } while (distance > this.shortPipeThreshold[1]);
        break;
    }
  }

  public static void main(String[] args) {
    UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(Hardware.ultrasonicSensorPort);
    NXTRegulatedMotor lMotor = new NXTRegulatedMotor(Hardware.lMotorPort);
    NXTRegulatedMotor rMotor = new NXTRegulatedMotor(Hardware.rMotorPort);
    DifferentialPilot pilot = new DifferentialPilot(
      Hardware.wheelDiameter, Hardware.trackWidth, lMotor, rMotor
    );

    LightSensor lLightSensor = new LightSensor(Hardware.lLightSensorPort);
    IdentifyLine idLine = new IdentifyLine(lLightSensor, pilot);
    Sonar sonar = new Sonar(ultrasonicSensor, pilot);

    while (true) {
      Button.waitForAnyPress();

      idLine.run(25);

      pilot.setRotateSpeed(40);
      pilot.rotate(-90);

      idLine.run(30);

      pilot.setRotateSpeed(40);
      pilot.rotate(-90);

      Pipe pipe = sonar.run();

      pilot.quickStop();
      pilot.setRotateSpeed(40);
      pilot.setTravelSpeed(2.5);

      if (pipe == Sonar.Pipe.SHORT) {
        pilot.travel(2, false);
      } else {
        pilot.travel(4, false);
      }

      pilot.rotate(90, false);
    }
  }
}
