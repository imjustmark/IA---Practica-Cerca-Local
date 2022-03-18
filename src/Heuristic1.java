package src;

import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;
import java.lang.Math.*;

import static src.RescateEstado.tiempoCentro;
import static src.RescateEstado.velocidadH;

public class Heuristic1 implements HeuristicFunction{

    public double tiempoDest(int coordx1, int coordy1, int coordx2, int coordy2){
        return Math.sqrt((coordx1-coordx2)^2 + (coordy1-coordy2)^2)/velocidadH;
    }

    public double calculaTiempoH(ArrayList<Integer> H){
        int num_dest = H.size();
        double sum = 0;
        for(int i = 0; i < num_dest -1; ++i){

            sum += tiempoDest();

            if(H.get(i+1) < 0){
                if(i+1 < num_dest) sum += tiempoCentro;
            }
            else{
                sum += tiempoRecoger();
            }
        }
        return 0.0;
    }

    public double getHeuristicValue(Object n){
        RescateEstado state = (RescateEstado) n;
        ArrayList<ArrayList<Integer>> conf = state.getSolucion();
        double sum = 0;
        int num_h = conf.size();
        for(int i = 0; i < num_h; ++i){
            double tempsH = calculaTiempoH(conf.get(i));
            sum += tempsH;
        }
        return sum;
    }


}
