import lejos.nxt.*;
import lejos.util.Delay;
import lejos.robotics.navigation.CompassPilot;
import lejos.nxt.addon.CompassSensor;

public class SimpleCompassPilotExample {
    public static void main(String[] args) {
        // Inicializa o sensor de bússola conectado à porta S1
        CompassSensor compass = new CompassSensor(SensorPort.S1);

        /*
         * Inicializa o CompassPilot para controlar os motores do robô.
         * Parâmetros do CompassPilot:
         * 1. CompassSensor compass: O sensor de bússola que mede a direção absoluta.
         * 2. double wheelDiameter: O diâmetro da roda (em centímetros).
         * 3. double trackWidth: A distância entre as rodas (em centímetros).
         * 4. NXTRegulatedMotor leftMotor: O motor que controla a roda esquerda.
         * 5. NXTRegulatedMotor rightMotor: O motor que controla a roda direita.
         */
        CompassPilot pilot = new CompassPilot(compass, 4.32, 12.5, Motor.A, Motor.B);

        // Define a velocidade de deslocamento linear (em centímetros por segundo).
        pilot.setTravelSpeed(10); // O robô se moverá a 10 cm/s

        // Define a velocidade de rotação (em graus por segundo).
        pilot.setRotateSpeed(30); // O robô girará a 30 graus/s

        // Aguarda o usuário pressionar qualquer botão para iniciar o programa.
        System.out.println("Pressione um botão para iniciar...");
        Button.waitForAnyPress();

        // Move o robô para frente por 50 cm.
        System.out.println("Movendo para frente 50 cm...");
        pilot.travel(50); // Move para frente 50 cm

        // Aguarda um momento antes de girar.
        Delay.msDelay(1000);

        // Gira o robô em 90 graus no sentido horário.
        System.out.println("Girando 90 graus no sentido horário...");
        pilot.rotate(90); // Gira 90 graus no sentido horário

        // Aguarda um momento antes de mover de volta.
        Delay.msDelay(1000);

        // Move o robô para trás 50 cm.
        System.out.println("Movendo para trás 50 cm...");
        pilot.travel(-50); // Move para trás 50 cm

        // Aguarda o usuário pressionar qualquer botão antes de encerrar o programa.
        System.out.println("Pressione um botão para encerrar...");
        Button.waitForAnyPress();
    }
}
