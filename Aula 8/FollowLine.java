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

/* Relatório
 * ---------
 * 1) Proporcional
 *
 * Em relação ao bang-bang, ele é mais suave nas linhas retas, mas falha nas
 * curvas mais estreitas.
 * 
 * Com k_p = 5 ele faz curvas muito bruscas mesmo na seção de linha reta
 * (balança de um lado para outro).
 *
 * Com k_p = 2 e u_line = 30 o robo foi bem na pista oval. Na outra pista, o
 * robô saiu da pista e ficou perdido na curva de ~45° pela borda externa,
 * porém ele é bem-sucedido na pista interna. Na curva pequena de ~90° ele
 * falha pela borda interna, mas passa pela borda externa.
 *
 */

public class FollowLine{
  static LightSensor light;
  static NXTMotor mRight, mLeft;

  public static int middle_value = 49;
  public static int u_line = 30;
  public static int k_p = 2;

  public static int error() {
    return light.getLightValue() - middle_value;
  }

  public static int proportional(int e) {
    return k_p * e;
  }

  public static int clamp(int value) {
    return value > 100 ? 100 : (value < -100 ? -100 : value);
  }

  public static void turn(int t) {
    int u = u_line + t;
    mRight.setPower(clamp(u));

    u = u_line - t;
    mLeft.setPower(clamp(u));
  }

  public static void main(String [] args){
    Button.waitForAnyPress();

    light = new LightSensor(SensorPort.S4);
    mRight = new NXTMotor(MotorPort.A);
    mLeft = new NXTMotor(MotorPort.C);

    while (true) {
      int e = error();
      int t = proportional(e);
      turn(t);
    }
  }
}

