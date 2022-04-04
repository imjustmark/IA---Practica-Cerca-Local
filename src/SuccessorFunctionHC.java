package src;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuccessorFunctionHC implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        RescateEstado estado = (RescateEstado) aState;

        int Nhelicopteros = estado.Nhelicopteros;
        int NCentros = estado.Ncentros;

        ArrayList<ArrayList<Integer>> solucion = estado.getSolucion();

        // Cambiar todos los grupos de helicoptero a todos los otros helicopteros detrás de todos los otros grupos.

        for (int h = 0; h < Nhelicopteros * NCentros; ++h) {
            int NgruposH = solucion.get(h).size();
            for (int i = 1; i < NgruposH - 1; ++i) {
                int grupo1 = solucion.get(h).get(i);
                for (int h2 = 0; h2 < Nhelicopteros * NCentros; ++h2) {
                    if (h != h2) {
                        int NgruposH2 = solucion.get(h2).size();
                        for (int j = 1; j < NgruposH2; ++j) {
                            int grupo2 = solucion.get(h2).get(j);
                            if (estado.EsValidoCambiaGrupo(grupo1)) {
                                StringBuffer S = new StringBuffer();
                                RescateEstado nuevo_estado = new RescateEstado(estado);
                                nuevo_estado.CambiaGrupoDeHelicoptero(i, h, h2, j);
                                S.append("Cambiar el grupo " + grupo1 + " del helicoptero " + h + " al helicoptero " + h2 + " despues del grupo " + grupo2 + ".\n");
                                retVal.add(new Successor(S.toString(), nuevo_estado));
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
                    if (estado.EsValidoCambioOrden(grupo,grupo2)) {
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
                    for (int g2 = 1; g2 < NgruposNH - 1; ++g2) {
                        int grupoNH = gruposNH.get(g2);
                        if (estado.EsValidoIntercambio(grupoH, grupoNH)) {
                            StringBuffer S = new StringBuffer();
                            RescateEstado nuevo_estado = new RescateEstado(estado);
                            nuevo_estado.IntercambiaGruposDeHelicopteros(grupoH, h, grupoNH, nh);
                            S.append("Intercambiar el grupo " + grupoH + " del helicoptero " + h + " con el grupo " + grupoNH + " del helicoptero " + nh + ".\n");
                            retVal.add(new Successor(S.toString(), nuevo_estado));

                        }
                    }
                }
            }
        }

        for (int h = 0; h < Nhelicopteros * NCentros; ++h) {
            ArrayList<Integer> gruposH = solucion.get(h);
            int NgruposH = gruposH.size();
            for (int g = 1; g < NgruposH - 1; ++g) {
                int id = gruposH.get(g);
                if (id == -1 && estado.EsValidoBorrarParada(h,g)) {
                    StringBuffer S = new StringBuffer();
                    RescateEstado nuevo_estado = new RescateEstado(estado);
                    int grupo_previo = solucion.get(h).get(g-1);
                    int grupo_posterior = solucion.get(h).get(g+1);
                    nuevo_estado.BorrarParada(h,g);
                    S.append("Eliminamos la parada en centro entre el grupo " + grupo_previo + " y el grupo " + grupo_posterior + " del helicoptero " + h + ".\n");
                    retVal.add(new Successor(S.toString(), nuevo_estado));
                }
            }
        }

        for (int h = 0; h < Nhelicopteros * NCentros; ++h) {
            ArrayList<Integer> gruposH = solucion.get(h);
            int NgruposH = gruposH.size();
            for (int g = 1; g < NgruposH - 1; ++g) {
                int id = gruposH.get(g);
                if (id == -1) {
                    ArrayList<Integer> movimientos = new ArrayList<>(Arrays.asList(-2,-1,1,2));
                    for (Integer m : movimientos) {
                        if (estado.EsValidoMoverParada(h,g,m)) {
                            StringBuffer S = new StringBuffer();
                            RescateEstado nuevo_estado = new RescateEstado(estado);
                            int grupo_previo = solucion.get(h).get(g-1);
                            int grupo_posterior = solucion.get(h).get(g+1);
                            nuevo_estado.MoverParada(h,g,m);
                            S.append("Movemos la parada en centro entre el grupo " + grupo_previo + " y el grupo " + grupo_posterior + " del helicoptero " + h + " " + m + " grupos.\n");
                            retVal.add(new Successor(S.toString(), nuevo_estado));
                        }
                    }
                }
            }
        }

        return retVal;
    }
}
