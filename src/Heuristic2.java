package src;

import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;

import static src.RescateEstado.*;

public class Heuristic2 implements HeuristicFunction{

    public double tiempoDest(int coordx1, int coordy1, int coordx2, int coordy2){
        return Math.sqrt((double)((coordx1-coordx2)^2 + (coordy1-coordy2)^2))/velocidadH;
    }

    public double calculaTiempoH(int H, Object n){
        RescateEstado state = (RescateEstado) n;
        ArrayList<ArrayList<Integer>> conf = state.getSolucion();

        int num_dest = conf.get(H).size();
        double sum = 0.0;
        for(int i = 0; i < num_dest -1; ++i){

            int coordx1, coordy1, coordx2, coordy2;
            if(conf.get(H).get(i) < 0){
                int centro = H/(state.Nhelicopteros);
                coordx1 = state.centros.get(centro).getCoordX();
                coordy1 = state.centros.get(centro).getCoordY();
            }
            else{
                coordx1 = state.grupos.get(conf.get(H).get(i)).getCoordX();
                coordy1 = state.grupos.get(conf.get(H).get(i)).getCoordY();
            }

            if(conf.get(H).get(i+1) < 0){
                int centro = H/(state.Nhelicopteros);
                coordx2 = state.centros.get(centro).getCoordX();
                coordy2 = state.centros.get(centro).getCoordY();
            }
            else{
                coordx2 = state.grupos.get(conf.get(H).get(i+1)).getCoordX();
                coordy2 = state.grupos.get(conf.get(H).get(i+1)).getCoordY();
            }

            sum += tiempoDest(coordx1, coordy1, coordx2, coordy2);

            if(conf.get(H).get(i+1) < 0){
                if(i+1 < num_dest) sum += tiempoCentro;
            }
            else{
                if(state.grupos.get(conf.get(H).get(i+1)).getPrioridad() == 1){
                    sum += state.grupos.get(conf.get(H).get(i+1)).getNPersonas()*tiempoPersonaPriod1;
                }
                else {
                    sum += state.grupos.get(conf.get(H).get(i+1)).getNPersonas()*tiempoPersonaPriod2;
                }
            }
        }
        return sum;
    }

    public double getHeuristicValue(Object n){
        RescateEstado state = (RescateEstado) n;
        ArrayList<ArrayList<Integer>> conf = state.getSolucion();
        double sum = 0;
        int num_h = conf.size();
        for(int i = 0; i < num_h; ++i){
            double tempsH = calculaTiempoH(i, state);
            sum += tempsH*tempsH;
        }
        sum = (-1)*sum;
        return sum;
    }
}