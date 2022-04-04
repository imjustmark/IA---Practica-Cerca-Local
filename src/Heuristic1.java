package src;

import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;

import static src.RescateEstado.*;

public class Heuristic1 implements HeuristicFunction{
    public double getHeuristicValue(Object n){
        RescateEstado state = (RescateEstado) n;
        ArrayList<ArrayList<Integer>> conf = state.getSolucion();
        double sum = 0;
        int num_h = conf.size();
        for(int i = 0; i < num_h; ++i){
            double tempsH = state.calculaTiempoH(i);
            sum += tempsH;
        }
        return sum;
    }
}
