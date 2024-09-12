import lejos.nxt.*;
import lejos.util.Delay;
import lejos.robotics.navigation.*;
import lejos.robotics.mapping.*;
import lejos.robotics.pathfinding.*;
import lejos.robotics.localization.*;
import lejos.nxt.addon.CompassSensor;

import lejos.geom.*;

public class PathfindingWithCompassPilot {
    public static void main(String[] args) {
        try {
            // Exibe uma mensagem para indicar que o programa está em execução.
            System.out.println("Running");
            // Aguarda o usuário pressionar qualquer botão para iniciar.
            Button.waitForAnyPress();
            // Introduz uma pausa de 500 milissegundos antes de iniciar o movimento.
            Delay.msDelay(500);

            // Inicializa o Compass Sensor conectado à porta S1.
            CompassSensor compass = new CompassSensor(SensorPort.S1);

            // Inicializa o CompassPilot para controlar os motores do robô.
            // Parâmetros: bússola, diâmetro da roda, distância entre as rodas, motores esquerdo e direito.
            CompassPilot pilot = new CompassPilot(compass, 2.205 * 2.56, 4.527 * 2.56, Motor.A, Motor.B);

            /*
                * Como o CompassPilot toma as medidas ?
                * No CompassPilot, todas as medidas de orientação são tomadas de forma absoluta em relação ao norte magnético. 
                * O CompassPilot utiliza um sensor de bússola digital, que mede o campo magnético da Terra para determinar a direção em que o 
                * robô está apontando. Essa direção é fornecida como um ângulo em relação ao norte magnético, que é considerado o ângulo 
                * de referência de 0 graus.
            */

            // Define a velocidade de deslocamento linear para 15 cm/s.
            pilot.setTravelSpeed(15);
            // Define a velocidade de rotação para 30 graus/s.
            pilot.setRotateSpeed(30);

            // Usa o CompassPilot para navegação. Não há um Navigator específico, mas criaremos uma lógica semelhante.
            // Usaremos o ponto atual como ponto de partida.

            // Define um conjunto de linhas que representarão obstáculos no mapa.
            Line[] lines = {
                new Line(60, -50, 60, 50),
                new Line(60, 50, 70, 50),
                new Line(60, -50, 70, -50),
                new Line(70, -50, 70, 50)
            };

            // Define os limites do mapa como um retângulo de 1000x1000 unidades.
            Rectangle bounds = new Rectangle(0, 0, 1000, 1000);

            // Cria um mapa de linhas (LineMap) que inclui os obstáculos e os limites do mapa.
            LineMap map = new LineMap(lines, bounds);

            // Inicializa o algoritmo de busca de caminho (pathfinding) usando o algoritmo de Dijkstra.
            DijkstraPathFinder pathFinder = new DijkstraPathFinder(map);

            // Obtém a pose inicial usando a bússola como referência.
            Pose initialPose = new Pose(0, 0, compass.getDegreesCartesian()); // Observe que a pose foi obtida com a bússola e não com odometria.

            // Define um ponto de destino (waypoint) em (200, 0).
            Waypoint wp = new Waypoint(200, 0);

            // Calcula a rota do ponto de partida (pose atual) até o waypoint usando o algoritmo de Dijkstra.
            Path path = pathFinder.findRoute(initialPose, wp);
            
            /*
             * Sobre o objeto do tipo Path criado acima:
             * O Path contém a sequência de coordenadas (x, y) que o robô deve seguir. Esta rota é calculada considerando os obstáculos
             * no mapa e buscando a menor distância possível do ponto inicial (initialPose) até o ponto de destino (wp).
            */

            // Faz o robô seguir o caminho calculado.
            followPathWithCompassPilot(pilot, compass, path);

            // Aguarda o usuário pressionar qualquer botão antes de encerrar o programa.
            Button.waitForAnyPress();
        } catch (Exception e) {
            // Imprime qualquer exceção que ocorra durante a execução.
            System.out.println(e);
        }
    }

    // Função para seguir o caminho usando o CompassPilot
    public static void followPathWithCompassPilot(CompassPilot pilot, CompassSensor compass, Path path) {
        /*
         * A ideia por trás do funcionamento dessa função é a seguinte:
         * Como o CompassPilot toma medidas absolutas em relação ao norte magnético, ele consegue alinhar sua orientação de acordo com
         * os ângulos absolutos necessários para chegar aos waypoints definidos pela variável path.
         * Dessa forma, é possível criar uma espécie de Navigator baseado no CompassPilot, que toma decisões de navegação diretamente.
        */

        // Para cada waypoint no caminho planejado
        for (Waypoint wp : path) {
            // Calcula o ângulo de rotação necessário para alinhar o robô com o próximo waypoint
            float headingToNextWaypoint = (float) Math.toDegrees(Math.atan2(
                wp.y - pilot.getPoseProvider().getPose().getY(),  // Diferença de coordenadas Y
                wp.x - pilot.getPoseProvider().getPose().getX()   // Diferença de coordenadas X
            ));

            /*
             * Explicação Matemática:
             * - A função Math.atan2(dy, dx) calcula o ângulo (em radianos) entre o ponto atual do robô e o próximo waypoint
             *   em relação ao eixo X positivo. Convertido para graus com Math.toDegrees.
             * - O ângulo calculado é um valor absoluto da direção que o robô deve virar para alinhar com o waypoint.
            */

            // Gira o robô para o ângulo calculado usando o CompassPilot
            pilot.rotateTo(headingToNextWaypoint);

            /*
             * Função rotateTo:
             * - Faz o robô girar até o ângulo absoluto especificado.
             * - Este método utiliza a bússola para assegurar que o robô atinja o ângulo exato necessário.
            */

            // Calcula a distância para o próximo waypoint
            float distanceToWaypoint = (float) Math.hypot(
                wp.y - pilot.getPoseProvider().getPose().getY(),  // Diferença de coordenadas Y
                wp.x - pilot.getPoseProvider().getPose().getX()   // Diferença de coordenadas X
            );

            /*
             * Função Math.hypot(dy, dx):
             * - Calcula a distância euclidiana entre dois pontos (x, y).
             * - Neste caso, entre a posição atual do robô e o próximo waypoint.
            */

            // Move o robô para o próximo waypoint
            pilot.travel(distanceToWaypoint);

            /*
             * Função travel:
             * - Faz o robô se mover para frente pela distância especificada.
             * - Usa a direção atual como a direção de deslocamento.
             * - O CompassPilot, portanto, permite que o robô viaje de um ponto ao outro de forma precisa.
            */
        }
    }
}
