package src;

import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;

public class Heuristic3 implements HeuristicFunction {
    public double getHeuristicValue(Object n){
        RescateEstado state = (RescateEstado) n;
        ArrayList<ArrayList<Integer>> conf = state.getSolucion();
        double sum = 0;
        double factor = 1.0;
        int num_h = conf.size();
        for(int i = 0; i < num_h; ++i){
            double tempsH = state.calculaTiempoH(i) + factor*state.calculaTiempoHPriod1(i);
            sum += tempsH;
        }
        return sum;
    }
}

