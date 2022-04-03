package src;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuccessorFunctionSA implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        RescateEstado estado = (RescateEstado) aState;

        ArrayList<ArrayList<Integer>> solucion = estado.getSolucion();

        int Ngrupos = estado.Ngrupos;
        int Nhelicopteros = estado.Nhelicopteros;

        int sucesores_operador_1 = (int) (Ngrupos * Ngrupos * (float)(Nhelicopteros -1) / Nhelicopteros);
        int sucesores_operador_2 = (int) (Ngrupos * ((float)Ngrupos/Nhelicopteros - 1)/2);
        int sucesores_operador_3 = (int) (Ngrupos * (float)(Nhelicopteros - 1) / 2);
        int sucesores_operador_4 = Ngrupos;

        int sucesores_totales = sucesores_operador_1 + sucesores_operador_2 + sucesores_operador_3 + sucesores_operador_4;
        float fraction1 = (float)sucesores_operador_1 / sucesores_totales;
        float fraction2 = fraction1 + (float)sucesores_operador_2 / sucesores_totales;
        float fraction3 = fraction2 + (float)sucesores_operador_3 / sucesores_totales;

        Random random = new Random();
        float rand = random.nextFloat();

        if (rand <= fraction1) {
            // Aplica operador1
            int h_rand_1, h_rand_2;
            int grupo1, grupo2;
            do {
                h_rand_1 = random.nextInt(Nhelicopteros);
                h_rand_2 = random.nextInt(Nhelicopteros);
                while (h_rand_1 == h_rand_2) h_rand_2 = random.nextInt(Nhelicopteros);

                ArrayList<Integer> gruposH1 = solucion.get(h_rand_1);
                ArrayList<Integer> gruposH2 = solucion.get(h_rand_2);
                int NgruposH1 = gruposH1.size();
                int NgruposH2 = gruposH2.size();

                do {
                    int index_rand_1 = random.nextInt(NgruposH1);
                    grupo1 = gruposH1.get(index_rand_1);
                } while(grupo1 < 0);

                int index_rand_2 = random.nextInt(NgruposH2);
                grupo2 = gruposH2.get(index_rand_2);

            } while (!estado.EsValidoCambiaGrupo(grupo1));

            RescateEstado nuevo_estado = new RescateEstado(estado);
            nuevo_estado.CambiaGrupoDeHelicoptero(grupo1, h_rand_1, h_rand_2, grupo2);
            String mensaje = "Cambiar grupo " + grupo1 + " del helicóptero " + h_rand_1 + " al helicóptero " + h_rand_2 + " después de " + grupo2 + ".";
            retVal.add(new Successor(mensaje, nuevo_estado));
        }
        else if (rand <= fraction2) {
            // Aplica operador2
            int h_rand;
            int grupo1, grupo2;
            do {
                h_rand = random.nextInt(Nhelicopteros);
                ArrayList<Integer> gruposH = solucion.get(h_rand);
                int NgruposH = gruposH.size();

                do {
                    int index_rand_1 = random.nextInt(NgruposH);
                    int index_rand_2 = random.nextInt(NgruposH);
                    grupo1 = gruposH.get(index_rand_1);
                    grupo2 = gruposH.get(index_rand_2);
                } while (grupo1 < 0 || grupo2 < 0 || grupo1 == grupo2);

            } while(!estado.EsValidoCambioOrden(h_rand,grupo1,grupo2));

            RescateEstado nuevo_estado = new RescateEstado(estado);
            nuevo_estado.CambiaOrdenGrupos(h_rand,grupo1,grupo2);
            String mensaje = "Cambiar de orden el grupo " + grupo1 + " y el " + grupo2 + " en el helicoptero " + h_rand + ".";
            retVal.add(new Successor(mensaje, nuevo_estado));
        }
        else if (rand <= fraction3) {
            // Aplica operador3
            int h_rand_1, h_rand_2;
            int grupo1, grupo2;
            do {
                h_rand_1 = random.nextInt(Nhelicopteros);
                h_rand_2 = random.nextInt(Nhelicopteros);
                while (h_rand_1 == h_rand_2) h_rand_2 = random.nextInt(Nhelicopteros);

                ArrayList<Integer> gruposH1 = solucion.get(h_rand_1);
                ArrayList<Integer> gruposH2 = solucion.get(h_rand_2);
                int NgruposH1 = gruposH1.size();
                int NgruposH2 = gruposH2.size();

                do {
                    int index_rand_1 = random.nextInt(NgruposH1);
                    int index_rand_2 = random.nextInt(NgruposH2);

                    grupo1 = gruposH1.get(index_rand_1);
                    grupo2 = gruposH2.get(index_rand_2);
                } while(grupo1 < 0 || grupo2 < 0);
            } while(!estado.EsValidoIntercambio(grupo1,grupo2));

            RescateEstado nuevo_estado = new RescateEstado(estado);
            nuevo_estado.IntercambiaGruposDeHelicopteros(grupo1,h_rand_1,grupo2,h_rand_2);
            String mensaje = "Intercambiar el grupo " + grupo1 + " del helicoptero " + h_rand_1 + " con el grupo " + grupo2 + " del helicoptero " + h_rand_2 + ".";
            retVal.add(new Successor(mensaje, nuevo_estado));
        }

        return retVal;
    }
}
