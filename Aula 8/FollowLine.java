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
 * 2) Integral
 * 
 * Com valores de k_i > 0.01 o robô começa a girar ou andar para trás (em
 * alguns casos).
 *
 * Decidi modificar o cálculo da integral para zerar o erro acumulado quando o
 * erro cruza a reta 0, isto é, quando o robô passa de branco para preto ou de
 * preto para branco. A ideia é que a integral não vai mais "tentar corrigir"
 * se o robô tiver voltado para a linha:
 *
 * if (acc_error * error < 0)
 *   acc_error = 0;
 *
 * Com k_i = 0.01 e a modificação acima, o robô começou a balançar para
 * esquerda e direita, fez um giro de 180 e começa a cambalear novamente.
 * (levemente melhor que o comportamento anterior de girar infinitamente).
 *
 * Com k_i = 0.001 não foi observado nenhum comportamento anormal (girar,
 * balançar, andar para trás), mas já que a constante é muito pequena, é
 * difícil dizer se o valor integral está fazendo efeito ou não.
 *
 * 3) Derivada
 *
 * O robô se dá bem na maior parte das curvas, porém falha nas curvas mais
 * estreitas. (sem a modificação dos monitores).
 */

public class FollowLine{
  static LightSensor light;
  static NXTMotor mRight, mLeft;

  public static int middle_value = 49;

  public static float u_line = 20f;
  public static float k_p = 2f;
  public static float k_i = 0.01f;
  public static float k_d = 16f;

  public static int prev_e = 0;
  public static int acc_e = 0;

  public static int error() {
    return light.getLightValue() - middle_value;
  }

  public static float proportional(int e) {
    return k_p * e;
  }

  public static float integral(int e) {
    if (acc_e * e < 0) acc_e = 0;
    acc_e = acc_e + e;
    return k_i * acc_e;
  }

  public static float derivative(int e) {
    return k_d * (e - prev_e);
  }

  public static int clamp(float value) {
    return (int) (value > 100 ? 100 : (value < -100 ? -100 : value));
  }

  public static void turn(float t) {
    float u = u_line + t;
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
      float t = proportional(e) + derivative(e);
      turn(t);
      prev_e = e;
    }
  }
}

