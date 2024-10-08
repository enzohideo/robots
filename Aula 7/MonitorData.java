import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.Sound;
import java.lang.Thread;  
  
public class MonitorData {
  public static class RobotMonitor extends Thread {  
    private  int  delay;
    UltrasonicSensor  sonar;
    
    public RobotMonitor(int  delay, UltrasonicSensor  sonar) {
      this.setDaemon(true);
      this.delay  =  delay;
      this.sonar  =  sonar;
    }  

    public  void  run() {   
      while(true) {  
        LCD.clear();
        LCD.drawString("MotorA ="+Motor.A.getTachoCount(), 0 , 0);
        LCD.drawString("MotorC ="+Motor.C.getTachoCount(), 0 , 1);
        LCD.drawString("Sonar ="+sonar.getDistance(), 0 , 2);
        try { this.sleep(delay); }
        catch (Exception e) { }
      }   
    }   
  }   

  public static class RobotSinger extends Thread {  
    private int delay;
    private int duration;

    public RobotSinger(int  delay) {
      this.setDaemon(true);
      this.delay  =  delay;
      this.duration = 100;
    }  

    public  void  run() {   
      while(true) {  
        Sound.playTone(440, duration);       
        try { this.sleep(delay); }
        catch (Exception e) { }
      }   
    }   
  }   

  public static void main(String [] args) throws Exception {
    UltrasonicSensor  sonic  =  new  UltrasonicSensor(SensorPort.S1);
    DifferentialPilot  pilot  =  new  DifferentialPilot(5.6f, 11.2f, Motor.A, Motor.B);
    RobotMonitor  rm  =  new  RobotMonitor(400, sonic);  
    RobotSinger sound_thread = new RobotSinger(300);
    pilot.forward();  
    rm.start(); 
    sound_thread.start(); 
  
    while(sonic.getDistance()  >=  30) {  
      Thread.sleep(200);  
    }
  }
}  
