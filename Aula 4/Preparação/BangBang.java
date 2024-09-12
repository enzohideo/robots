import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;

public class BangBang {
    public static void main(String[] args) {

        // Instancia os motores.
        NXTMotor motorLeft = new NXTMotor(MotorPort.A);
        NXTMotor motorRight = new NXTMotor(MotorPort.B);
        
        // Instancia o sensor de luz.
        LightSensor lightSensor = new LightSensor(SensorPort.S1);
        
        // Seta a velocidade dos motores
        int speed = 60;
        
        // Seta um limiar para o sensor de luz
        int threshold = 45;  
        
        while (!Button.ESCAPE.isDown()) {

            // Leitura do sensor de luz
            int lightValue = lightSensor.getNormalizedLightValue();
            
            
            if (lightValue < threshold) { // Se o valor de luz for menor que o limiar, está na linha
                motorLeft.setPower(speed);
                motorRight.setPower(0);  // Motor direito para, fazendo o robô girar para a esquerda
            } else { // Se o valor de luz for maior ou igual ao limiar, está fora da linha
                motorLeft.setPower(0);  // Motor esquerdo para, fazendo o robô girar para a direita
                motorRight.setPower(speed);
            }
        }
        
        // Parada dos motores ao pressionar ESCAPE
        motorLeft.stop();
        motorRight.stop();
    }
}
