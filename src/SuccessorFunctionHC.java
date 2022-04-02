package src;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class SuccessorFunctionHC implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        RescateEstado estado = (RescateEstado) aState;

        int Nhelicopteros = estado.Nhelicopteros;

        ArrayList<ArrayList<Integer>> solucion = estado.getSolucion();

        // todo: FALTAN OPERADORES: MOVER LA PARADA EN BASE (quizas? no se).

        // Cambiar todos los grupos de helicoptero a todos los otros helicopteros detrás de todos los otros grupos.
        for (int h = 0; h < Nhelicopteros; ++h) {                       // Iterar sobre todos los helicopteros
            int NgruposH = solucion.get(h).size();
            ArrayList<Integer> gruposH = solucion.get(h);
            for (int g = 0; g < NgruposH; ++g) {                        // Iterar sobre todos los grupos de cada helicoptero
                int grupoH = gruposH.get(g);
                if (grupoH >= 0) {                                      // Evitar cambiar una parada en centro
                    for (int nh = 0; nh < Nhelicopteros; ++nh) {        // Iterar sobre los demas helicopteros
                        if (h != nh) {
                            int NgruposNH = solucion.get(nh).size();
                            ArrayList<Integer> gruposNH = solucion.get(nh);
                            for (int gp = 0; gp < NgruposNH; ++gp) {     // Iterar sobre los grupos del nuevo helicoptero
                                int grupoNH = gruposNH.get(gp);
                                if (estado.EsValidoCambiaGrupo(grupoH, nh, grupoNH)) {
                                    RescateEstado nuevo_estado = new RescateEstado(estado);
                                    nuevo_estado.CambiaGrupoDeHelicoptero(grupoH, h, nh, grupoNH);
                                    String mensaje = "Cambiar grupo " + grupoH + " del helicóptero " + h + " al helicóptero " + nh + " después de " + grupoNH + ".";
                                    retVal.add(new Successor(mensaje, nuevo_estado));
                                }
                            }
                        }
                    }
                }
            }
        }

        // Cambiar todos los grupos de orden en todos los helicopteros
        for (int h = 0; h < Nhelicopteros; ++h) {
            int NgruposH = solucion.get(h).size();
            ArrayList<Integer> gruposH = solucion.get(h);
            for (int g = 0; g < NgruposH; ++g) {
                for (int g2 = g + 1; g2 < NgruposH; ++g2) {
                    int grupo = gruposH.get(g);
                    int grupo2 = gruposH.get(g2);
                    if (grupo >= 0 && grupo2 >= 0 && estado.EsValidoCambioOrden(h,grupo,grupo2)) {
                        RescateEstado nuevo_estado = new RescateEstado(estado);
                        nuevo_estado.CambiaOrdenGrupos(h,grupo,grupo2);
                        String mensaje = "Cambiar de orden el grupo " + grupo + " y el " + grupo2 + " en el helicoptero " + h + ".";
                        retVal.add(new Successor(mensaje, nuevo_estado));
                    }
                }
            }
        }

        //Intercambiar todos los grupos de un helicóptero con todos los otros
        for (int h = 0; h < Nhelicopteros; ++h) {
            int NgruposH = solucion.get(h).size();
            ArrayList<Integer> gruposH = solucion.get(h);
            for (int g = 0; g < NgruposH; ++g) {
                int grupoH = gruposH.get(g);
                if (grupoH >= 0) {
                    for (int nh = h + 1; nh < Nhelicopteros; ++nh) {
                        int NgruposNH = solucion.get(nh).size();
                        ArrayList<Integer> gruposNH = solucion.get(nh);
                        for (int g2 = 0; g2 < NgruposNH; ++g2) {
                            int grupoNH = gruposNH.get(g2);
                            if (grupoNH >= 0 && estado.EsValidoIntercambio(grupoH,h,grupoNH,nh)) {
                                RescateEstado nuevo_estado = new RescateEstado(estado);
                                nuevo_estado.IntercambiaGruposDeHelicopteros(grupoH,h,grupoNH,nh);
                                String mensaje = "Intercambiar el grupo " + grupoH + " del helicoptero " + h + " con el grupo " + grupoNH + " del helicoptero " + nh + ".";
                                retVal.add(new Successor(mensaje, nuevo_estado));
                            }
                        }
                    }
                }
            }
        }

        // Añadir una parada en centro después de cada grupo
        for (int h = 0; h < Nhelicopteros; ++h) {
            int NgruposH = solucion.get(h).size();
            ArrayList<Integer> gruposH = solucion.get(h);
            for (int g = 0; g < NgruposH; ++g) {
                int grupo = gruposH.get(g);
                if (grupo >= 0) {
                    RescateEstado nuevo_estado = new RescateEstado(estado);
                    nuevo_estado.ParadaEnCentro(h,grupo);
                    String mensaje = "Añadir una parada en centro al helicoptero " + h + " después de recoger al grupo " + grupo + ".";
                    retVal.add(new Successor(mensaje, nuevo_estado));
                }
            }
        }

        return retVal;
    }
}
