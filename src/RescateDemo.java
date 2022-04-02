package src;

import aima.search.eightpuzzle.*;
import aima.search.framework.*;
import aima.search.informed.AStarSearch;
import aima.search.informed.GreedyBestFirstSearch;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import aima.search.nqueens.NQueensBoard;
import aima.search.nqueens.NQueensGoalTest;
import aima.search.nqueens.NQueensSuccessorFunction;
import aima.search.nqueens.QueensToBePlacedHeuristic;
import aima.search.uninformed.DepthLimitedSearch;
import aima.search.uninformed.IterativeDeepeningSearch;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class RescateDemo {
    static RescateEstado state;

    public static void main(String[] args) {
        System.out.println("Introduzca la seed deseada: ");
        Scanner in = new Scanner(System.in);
        int seed = in.nextInt();
        state = new RescateEstado(seed);
        helicoptersHillClimbing();
        helicoptersSimulatedAnnealing();
    }

    private static void helicoptersSimulatedAnnealing() {
        System.out.println("\nHelicopters Simulated Annealing  Search -->");
        try {
            Problem problem = new Problem(state,
                    new SuccessorFunctionHC(),
                    new EstadoFinal(), new Heuristic1());
            Search search = new SimulatedAnnealingSearch();
            SearchAgent agent = new SearchAgent(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void helicoptersHillClimbing() {
        System.out.println("\nHelicopters HillClimbing  -->");
        try {
            Problem problem =  new Problem(state,new SuccessorFunctionHC(), new EstadoFinal(),new Heuristic1());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
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
