/*
 *
 * 
 * Made by: Pedro Miguelez
 * Commented by: Gabriel Navarro
 *
 *  
*/

// Importa a classe que será utilizada para manipulação dos motores e botões do Lego NXT.
import lejos.nxt.*;  
// Importa a classe que será utilizada para criar pausas no programa.
import lejos.util.Delay;  
// Importa classes de navegação, como DifferentialPilot e Navigator.
import lejos.robotics.navigation.*;  

public class Waypoints {
  public static void main(String[] args) {

    // Exibe uma mensagem no console NXT para indicar que o programa está em execução.
    System.out.println("Running");
    
    // Aguarda o usuário pressionar qualquer botão para iniciar o programa.
    Button.waitForAnyPress();
    
    // Introduz uma pausa de 500 milissegundos antes de iniciar o movimento do robô.
    Delay.msDelay(500);

    // Cria um objeto DifferentialPilot para controlar o movimento do robô.
    // Parâmetros: (diâmetro da roda, espaçamento entre rodas, motor esquerdo, motor direito, inversão dos motores)
    DifferentialPilot pilot = new DifferentialPilot(2.205 * 2.56, 4.527 * 2.56, Motor.A, Motor.B, false);

    // Define a velocidade de deslocamento linear do robô para 15 cm/s.
    pilot.setTravelSpeed(15);
    
    // Define a velocidade de rotação do robô para 30 graus/s.
    pilot.setRotateSpeed(30);

    // Cria um objeto Navigator que usa o DifferentialPilot para navegar.
    Navigator navigator = new Navigator(pilot);

    // Adiciona um ponto de passagem (waypoint) nas coordenadas (50, 0).
    navigator.addWaypoint(50, 0);
    
    // Adiciona um ponto de passagem (waypoint) nas coordenadas (50, 50).
    navigator.addWaypoint(50, 50);
    
    // Adiciona um ponto de passagem (waypoint) nas coordenadas (0, 50).
    navigator.addWaypoint(0, 50);
    
    // Adiciona um ponto de passagem (waypoint) nas coordenadas (0, 0) (retorno ao ponto de origem).
    navigator.addWaypoint(0, 0);
    
    // Faz o robô seguir o caminho definido pelos pontos de passagem adicionados.
    navigator.followPath();
    
    // Aguarda o usuário pressionar qualquer botão antes de encerrar o programa.
    Button.waitForAnyPress();
  }
}
