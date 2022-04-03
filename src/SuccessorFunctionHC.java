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
        int NCentros = estado.Ncentros;

        ArrayList<ArrayList<Integer>> solucion = estado.getSolucion();

        // todo: FALTAN OPERADORES: MOVER LA PARADA EN BASE + ELIMINAR PARADA + QUE ELS INTERCANVIS POSIN PARADES SI HO NECESSITEN
        // todo: Considerar que al final sempre hi ha un -1.

        // Cambiar todos los grupos de helicoptero a todos los otros helicopteros detrás de todos los otros grupos.
        for (int h = 0; h < Nhelicopteros * NCentros; ++h) {                       // Iterar sobre todos los helicopteros
            int NgruposH = solucion.get(h).size();
            ArrayList<Integer> gruposH = solucion.get(h);
            for (int g = 1; g < NgruposH - 1; ++g) {                        // Iterar sobre todos los grupos de cada helicoptero
                int grupoH = gruposH.get(g);
                if (grupoH >= 0) {                                      // Evitar cambiar una parada en centro
                    for (int nh = 0; nh < Nhelicopteros * NCentros; ++nh) {        // Iterar sobre los demas helicopteros
                        if (h != nh) {
                            int NgruposNH = solucion.get(nh).size();
                            for (int gp = 0; gp < NgruposNH-1; ++gp) {     // Iterar sobre los grupos del nuevo helicoptero
                                if (estado.EsValidoCambiaGrupo(g, nh, gp)) {
                                    StringBuffer S = new StringBuffer();
                                    RescateEstado nuevo_estado = new RescateEstado(estado);
                                    nuevo_estado.CambiaGrupoDeHelicoptero(g, h, nh, gp);
                                    int grupoNH = solucion.get(nh).get(gp);
                                    S.append("Cambiar grupo " + grupoH + " del helicóptero " + h + " al helicóptero " + nh + " después de " + grupoNH + ".\n");
                                    retVal.add(new Successor(S.toString(), nuevo_estado));

                                }
                            }
                        }
                    }
                }
            }
        }

        // Cambiar todos los grupos de orden en todos los helicopteros
        for (int h = 0; h < Nhelicopteros * NCentros; ++h) {
            int NgruposH = solucion.get(h).size();
            ArrayList<Integer> gruposH = solucion.get(h);
            for (int g = 1; g < NgruposH - 1; ++g) {
                for (int g2 = g + 1; g2 < NgruposH - 1; ++g2) {
                    int grupo = gruposH.get(g);
                    int grupo2 = gruposH.get(g2);
                    if (grupo >= 0 && grupo2 >= 0 && estado.EsValidoCambioOrden(h,grupo,grupo2)) {
                        StringBuffer S = new StringBuffer();
                        RescateEstado nuevo_estado = new RescateEstado(estado);
                        nuevo_estado.CambiaOrdenGrupos(h,grupo,grupo2);
                        S.append("Cambiar de orden el grupo " + grupo + " y el " + grupo2 + " en el helicoptero " + h + ".\n");
                        retVal.add(new Successor(S.toString(), nuevo_estado));
                    }
                }
            }
        }

        //Intercambiar todos los grupos de un helicóptero con todos los otros
        for (int h = 0; h < Nhelicopteros*NCentros; ++h) {

            int NgruposH = solucion.get(h).size();
            ArrayList<Integer> gruposH = solucion.get(h);

            for (int g = 1; g < NgruposH - 1; ++g) {

                int grupoH = gruposH.get(g);

                for (int nh = h + 1; nh < Nhelicopteros * NCentros; ++nh) {

                    int NgruposNH = solucion.get(nh).size();
                    ArrayList<Integer> gruposNH = solucion.get(nh);

                    for (int g2 = 1; g2 < NgruposNH-1; ++g2) {

                        int grupoNH = gruposNH.get(g2);

                        if (estado.EsValidoIntercambio(grupoH,h,grupoNH,nh)) {

                            StringBuffer S = new StringBuffer();
                            RescateEstado nuevo_estado = new RescateEstado(estado);
                            nuevo_estado.IntercambiaGruposDeHelicopteros(grupoH,h,grupoNH,nh);
                            S.append("Intercambiar el grupo " + grupoH + " del helicoptero " + h + " con el grupo " + grupoNH + " del helicoptero " + nh + ".\n");
                            retVal.add(new Successor(S.toString(), nuevo_estado));

                        }
                    }
                }

            }
        }
        return retVal;
    }
}
