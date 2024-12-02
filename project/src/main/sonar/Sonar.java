package sonar;

import java.util.ArrayList;
import java.util.Arrays;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.NXTMotor;
import lejos.nxt.UltrasonicSensor;

public class Sonar {
  public enum Pipe {
    SHORT, TALL
  }

  private UltrasonicSensor sonar;

  private NXTMotor lMotor;
  private NXTMotor rMotor;

  // TODO: Distinguish short from tall pipes
  private float[] shortPipeThreshold = { 80f, 24f };
  private float[] tallPipeThreshold = { 25f, 16f };

  private int power = 25;
  private int samplingPeriod = 25;

  class Distances {
    int[] array;
    int sum = 0;
    int head = 0;
    int size = 0;

    public Distances(int size) {
      this.size = size;
      this.array = new int[this.size];
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

  public Sonar(UltrasonicSensor sonar, NXTMotor lMotor, NXTMotor rMotor) {
    this.distances = new Distances(32);
    this.sonar = sonar;
    this.lMotor = lMotor;
    this.rMotor = rMotor;
    this.sonar.off();
  }

  public Pipe run() {
    this.rMotor.setPower(power);
    this.lMotor.setPower(power);
    this.sonar.continuous();

    Pipe pipe;
    // while (true) {
      pipe = earlyDetection();
    //   if (false) break;
    // }

    // LCD.drawString("FOUND " + pipe.name(), 0, 1);

    // 2nd stage: Alignment with the pipe.
    // Walk forward until it's next to the pipe
    lateDetection(pipe);

    this.rMotor.setPower(0);
    this.lMotor.setPower(0);
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

      distance = distances.min();
      float avg = distances.avg();

      if (distances.var() > 4e3) continue;

      if (distance < this.tallPipeThreshold[0]) {
        LCD.drawString("TALL", 0, 4);
        return Pipe.TALL;
      } else if (distance < this.shortPipeThreshold[0]) {
        LCD.drawString("SHORT", 0, 4);
        return Pipe.SHORT;
      }

      sleep();
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

  void sleep() {
    // try {
    //   Thread.sleep(this.samplingPeriod);
    // } catch (InterruptedException error) {
    //   LCD.drawString("Sonar ERROR: interrupted", 0, 0);
    // }
  }
}
