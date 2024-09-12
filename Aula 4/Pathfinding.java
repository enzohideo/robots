/*
 *
 * 
 * Made by: Pedro Miguelez
 * Commented by: Gabriel Navarro
 *
 *  
*/

import lejos.nxt.*;
import lejos.util.Delay;
import lejos.robotics.navigation.*;
import lejos.robotics.mapping.*;
import lejos.robotics.pathfinding.*;
import lejos.robotics.localization.*;
import lejos.geom.*;

public class Pathfinding {
  public static void main(String[] args) {
    try {

      // Exibe uma mensagem para indicar que o programa está em execução
      System.out.println("Running");  
      // Aguarda o usuário pressionar qualquer botão para iniciar
      Button.waitForAnyPress();  
      // Introduz uma pausa de 500 milissegundos antes de iniciar o movimento
      Delay.msDelay(500);  

      // Inicializa o DifferentialPilot para controlar os motores do robô
      DifferentialPilot pilot = new DifferentialPilot(2.205 * 2.56, 4.527 * 2.56, Motor.A, Motor.B, false);
      // Define a velocidade de deslocamento linear para 15 cm/s
      pilot.setTravelSpeed(15);  
      // Define a velocidade de rotação para 30 graus/s
      pilot.setRotateSpeed(30);  

      // Provedor de Pose baseado em Odometria que calcula a posição e orientação do robô
      OdometryPoseProvider poseProvider = new OdometryPoseProvider(pilot);

      // Cria um objeto Navigator para controlar a navegação do robô
      Navigator navigator = new Navigator(pilot);

      // Fator multiplicador usado para dimensionar os elementos do mapa (não é utilizado no código a seguir)
      int mult = 3;  

      // Define um conjunto de linhas que representarão obstáculos no mapa
      Line[] lines = {
          new Line(60, -50, 60, 50),
          new Line(60, 50, 70, 50),
          new Line(60, -50, 70, -50),
          new Line(70, -50, 70, 50)
      };

      // Define os limites do mapa como um retângulo de 1000x1000 unidades
      Rectangle bounds = new Rectangle(0, 0, 1000, 1000);

      // Cria um mapa de linhas (LineMap) que inclui os obstáculos e os limites do mapa
      LineMap map = new LineMap(lines, bounds);

      // Inicializa o algoritmo de busca de caminho (pathfinding) usando o algoritmo de Dijkstra
      DijkstraPathFinder pathFinder = new DijkstraPathFinder(map);

      // Define um ponto de destino (waypoint) em (200, 0)
      Waypoint wp = new Waypoint(200, 0);

      // Calcula a rota do ponto de partida (pose atual) até o waypoint usando o algoritmo de Dijkstra
      navigator.setPath(pathFinder.findRoute(poseProvider.getPose(), wp));

      // Faz o robô seguir o caminho calculado
      navigator.followPath();
      
      // Aguarda o usuário pressionar qualquer botão antes de encerrar o programa
      Button.waitForAnyPress();
    } catch (Exception e) {
      // Imprime qualquer exceção que ocorra durante a execução
      System.out.println(e);  
    }
  }
}
