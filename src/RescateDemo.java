package src;

import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class RescateDemo {

    public static void main(String[] args) {
        System.out.println("Introduzca la seed deseada: ");
        Scanner in = new Scanner(System.in);
        int seed = in.nextInt();
        Instant inicio = Instant.now();
        RescateEstado state = new RescateEstado(seed);
        state.EstadoInicial2();

        if (state.comprobacionCapacidad()) state.print_solution();
        else System.out.println("ERROR! Se sobrepasa la capacidad maxima de los helicopteros en la solucion inicial!");

        if (!state.comprobacionNumero()) System.out.println("ERROR! Se sobrepasa el numero maximo de grupos por helicoptero en la solucion inicial!");

        System.out.println("Introduzca que metodo quiere usar: ");
        System.out.println(" 0: Hill Climbing");
        System.out.println(" 1: Simulated Annealing");
        int choice = in.nextInt();
        if (choice == 0) helicoptersHillClimbing(state);
        else if (choice == 1) helicoptersSimulatedAnnealing(state);
        else System.out.println("Error, introduzca un valor valido (0 o 1)");
        Instant finish =Instant.now();
        long timeElapsed = Duration.between(inicio,finish).toMillis();
        System.out.println("Elapsed time: " + timeElapsed + " ms.");
    }

    private static void helicoptersSimulatedAnnealing(RescateEstado state) {
        System.out.println("\nHelicopters Simulated Annealing  Search -->");
        try {
            Problem problem = new Problem(state, new SuccessorFunctionSA(), new EstadoFinal(), new Heuristic2());
            Search search = new SimulatedAnnealingSearch(10000, 100, 125, 0.01);
            SearchAgent agent = new SearchAgent(problem, search);

            RescateEstado solucionfinal = (RescateEstado) search.getGoalState();
            solucionfinal.print_solution();
            if (!solucionfinal.comprobacionCapacidad()) System.out.println("ERROR! Se sobrepasa la capacidad maxima de los helicopteros!");

            if (!state.comprobacionNumero()) System.out.println("ERROR! Se sobrepasa el numero maximo de grupos por helicoptero!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void helicoptersHillClimbing(RescateEstado state) {
        System.out.println("\nHelicopters HillClimbing  -->");
        try {
            Problem problem =  new Problem(state,new SuccessorFunctionHC(), new EstadoFinal(),new Heuristic3());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);

            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            RescateEstado solucionfinal = (RescateEstado) search.getGoalState();
            if (solucionfinal.comprobacionCapacidad()) solucionfinal.print_solution();
            else System.out.println("ERROR! Se sobrepasa la capacidad maxima de los helicopteros!");

            if (!state.comprobacionNumero()) System.out.println("ERROR! Se sobrepasa el numero maximo de grupos por helicoptero!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
}
