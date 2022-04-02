package src;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class SuccessorFunctionHC implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        RescateEstado estado = (RescateEstado) aState;

        int Ngrupos = estado.Ngrupos;
        int Ncentros = estado.Ncentros;
        int Nhelicopteros = estado.Nhelicopteros;

        ArrayList<ArrayList<Integer>> solucion = estado.getSolucion();

        // todo: FALTA TENER EN CUENTA LAS PARADAS EN BASE.
        // todo: FALTAN OPERADORES: MOVER LA PARADA EN BASE (quizas? no se).

        // Cambiar todos los grupos de helicoptero a todos los otros helicopteros detrás de todos los otros grupos.
        for (int h = 0; h < Nhelicopteros; ++h) {                   // Iterar sobre todos los helicopteros
            int gruposH = solucion.get(h).size();
            for (int g = 0; g < gruposH; ++g) {                     // Iterar sobre todos los grupos de cada helicoptero
                for (int nh = 0; nh < Nhelicopteros; ++nh) {        // Iterar sobre los demas helicopteros
                    if (h != nh) {
                        int gruposNH = solucion.get(nh).size();
                        for (int gp = 0; gp < gruposNH; ++gp) {     // Iterar sobre los grupos del nuevo helicoptero
                            if (estado.EsValidoCambiaGrupo(g, nh, gp)) {
                                RescateEstado nuevo_estado = new RescateEstado(estado);
                                nuevo_estado.CambiaGrupoDeHelicoptero(g, h, nh, gp);
                                String mensaje = "Cambiar grupo " + g + " del helicóptero " + h + " al helicóptero " + nh + " después de " + gp + ".";
                                retVal.add(new Successor(mensaje, nuevo_estado));
                            }
                        }
                    }
                }
            }
        }

        // Cambiar todos los grupos de orden en todos los helicopteros
        for (int h = 0; h < Nhelicopteros; ++h) {
            int gruposH = solucion.get(h).size();
            for (int g = 0; g < gruposH; ++g) {
                for (int g2 = g + 1; g2 < gruposH; ++g2) {
                    if (estado.EsValidoCambioOrden(h,g,g2)) {
                        RescateEstado nuevo_estado = new RescateEstado(estado);
                        nuevo_estado.CambiaOrdenGrupos(h,g,g2);
                        String mensaje = "Cambiar de grupo " + g + " y " + g2 + " en el helicoptero " + h + ".";
                        retVal.add(new Successor(mensaje, nuevo_estado));
                    }
                }
            }
        }

        //Intercambiar todos los grupos de un helicóptero con todos los otros
        for (int h = 0; h < Nhelicopteros; ++h) {
            int gruposH = solucion.get(h).size();
            for (int g = 0; g < gruposH; ++g) {
                for (int nh = h + 1; nh < Nhelicopteros; ++nh) {
                    int gruposNH = solucion.get(nh).size();
                    for (int g2 = 0; g2 < gruposNH; ++g2) {
                        if (estado.EsValidoIntercambio(g,h,g2,nh)) {
                            RescateEstado nuevo_estado = new RescateEstado(estado);
                            nuevo_estado.IntercambiaGruposDeHelicopteros(g,h,g2,nh);
                            String mensaje = "Intercambiar grupo " + g + " del helicoptero " + h + " con el grupo " + g2 + " del helicoptero " + nh + ".";
                            retVal.add(new Successor(mensaje, nuevo_estado));
                        }
                    }
                }
            }
        }

        // Añadir una parada en centro después de cada grupo
        for (int h = 0; h < Nhelicopteros; ++h) {
            int gruposH = solucion.get(h).size();
            for (int g = 0; g < gruposH; ++g) {
                RescateEstado nuevo_estado = new RescateEstado(estado);
                nuevo_estado.ParadaEnCentro(h,g);
                String mensaje = "Añadir una parada en centro al helicoptero " + h + " después de recoger al grupo " + g + ".";
                retVal.add(new Successor(mensaje, nuevo_estado));
            }
        }

        return retVal;
    }
}
