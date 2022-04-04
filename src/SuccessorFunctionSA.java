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
        int Ncentros = estado.Ncentros;

        int sucesores_operador_1 = (int) (Ngrupos * Ngrupos * (float)(Nhelicopteros*Ncentros -1) / (Nhelicopteros*Ncentros));
        int sucesores_operador_2 = (int) (Ngrupos * ((float)Ngrupos/(Nhelicopteros * Ncentros) - 1)/2);
        int sucesores_operador_3 = (int) (Ngrupos * (float)(Nhelicopteros * Ncentros - 1) / 2);
        int sucesores_operador_4 = Ngrupos / 3;
        int sucesores_operador_5 = Ngrupos / 3 * 4;

        int sucesores_totales = sucesores_operador_1 + sucesores_operador_2 + sucesores_operador_3 + sucesores_operador_4 + sucesores_operador_5;
        float fraction1 = (float)sucesores_operador_1 / sucesores_totales;
        float fraction2 = fraction1 + (float)sucesores_operador_2 / sucesores_totales;
        float fraction3 = fraction2 + (float)sucesores_operador_3 / sucesores_totales;
        float fraction4 = fraction3 + (float)sucesores_operador_4 / sucesores_totales;
        float fraction5 = fraction4 + (float)sucesores_operador_5 / sucesores_totales;

        Random random = new Random();
        float rand = random.nextFloat();

        if (rand > fraction3 && rand <= fraction4) {
            boolean valid;
            int h_rand_op_4;
            int index_rand_op_4;
            int it = 0;
            do {
                valid = false;
                h_rand_op_4 = random.nextInt(Nhelicopteros*Ncentros);
                ArrayList<Integer> gruposH = solucion.get(h_rand_op_4);
                int NgruposH = gruposH.size();
                int grupo;
                do {
                    index_rand_op_4 = random.nextInt(NgruposH-2);
                    grupo = gruposH.get(index_rand_op_4);
                    ++it;
                    if ((index_rand_op_4 > 0 && grupo == -1 && estado.EsValidoBorrarParada(h_rand_op_4,index_rand_op_4))) valid = true;
                } while(!valid && it < 20);
            } while(!valid && it < 200);

            if (valid) {
                StringBuffer S = new StringBuffer();
                RescateEstado nuevo_estado = new RescateEstado(estado);
                int grupo_previo = solucion.get(h_rand_op_4).get(index_rand_op_4-1);
                int grupo_posterior = solucion.get(h_rand_op_4).get(index_rand_op_4+1);
                nuevo_estado.BorrarParada(h_rand_op_4,index_rand_op_4);
                S.append("Eliminamos la parada en centro entre el grupo " + grupo_previo + " y el grupo " + grupo_posterior + " del helicoptero " + h_rand_op_4 + ".\n");
                retVal.add(new Successor(S.toString(), nuevo_estado));
            }
            else {
                sucesores_totales = sucesores_operador_1 + sucesores_operador_2 + sucesores_operador_3;
                fraction1 = (float)sucesores_operador_1 / sucesores_totales;
                fraction2 = fraction1 + (float)sucesores_operador_2 / sucesores_totales;
                fraction3 = fraction2 + (float)sucesores_operador_3 / sucesores_totales;
            }
        }
        else if (rand > fraction4 && rand <= fraction5) {
            boolean valid;
            int h_rand_op_5;
            int index_rand_op_5;
            int mov_rand_op_5;
            int it = 0;
            do {
                valid = false;
                mov_rand_op_5 = random.nextInt(2);
                float aux = random.nextFloat();
                if (aux <= 0.5) mov_rand_op_5 = mov_rand_op_5 * (-1);
                h_rand_op_5 = random.nextInt(Nhelicopteros*Ncentros);
                ArrayList<Integer> gruposH = solucion.get(h_rand_op_5);
                int NgruposH = gruposH.size();
                int grupo;
                do {
                    index_rand_op_5 = random.nextInt(NgruposH-2);
                    grupo = gruposH.get(index_rand_op_5);
                    ++it;
                    if (index_rand_op_5 > 0 && grupo == -1 && estado.EsValidoMoverParada(h_rand_op_5,index_rand_op_5,mov_rand_op_5)) valid = true;
                } while(!valid && it < 20);
            } while(!valid && it < 200);

            if (valid) {
                StringBuffer S = new StringBuffer();
                RescateEstado nuevo_estado = new RescateEstado(estado);
                int grupo_previo = solucion.get(h_rand_op_5).get(index_rand_op_5-1);
                int grupo_posterior = solucion.get(h_rand_op_5).get(index_rand_op_5+1);
                nuevo_estado.MoverParada(h_rand_op_5,index_rand_op_5,mov_rand_op_5);
                S.append("Movemos la parada en centro entre el grupo " + grupo_previo + " y el grupo " + grupo_posterior + " del helicoptero " + h_rand_op_5 + " " + mov_rand_op_5 + " grupos.\n");
                retVal.add(new Successor(S.toString(), nuevo_estado));
            }
            else {
                sucesores_totales = sucesores_operador_1 + sucesores_operador_2 + sucesores_operador_3;
                fraction1 = (float)sucesores_operador_1 / sucesores_totales;
                fraction2 = fraction1 + (float)sucesores_operador_2 / sucesores_totales;
                fraction3 = fraction2 + (float)sucesores_operador_3 / sucesores_totales;
            }
        }

        if (rand <= fraction1) {
            // Aplica operador1
            int h_rand_1, h_rand_2;
            int grupo1, grupo2;
            int index_rand_1, index_rand_2;
            do {
                h_rand_1 = random.nextInt(Nhelicopteros*Ncentros);
                h_rand_2 = random.nextInt(Nhelicopteros*Ncentros);
                while (h_rand_1 == h_rand_2) h_rand_2 = random.nextInt(Nhelicopteros*Ncentros);

                ArrayList<Integer> gruposH1 = solucion.get(h_rand_1);
                ArrayList<Integer> gruposH2 = solucion.get(h_rand_2);
                int NgruposH1 = gruposH1.size();
                int NgruposH2 = gruposH2.size();

                do {
                    index_rand_1 = random.nextInt(NgruposH1);
                    grupo1 = gruposH1.get(index_rand_1);
                } while(grupo1 < 0);

                do {
                    index_rand_2 = random.nextInt(NgruposH2-1);
                    grupo2 = gruposH2.get(index_rand_2);
                } while(index_rand_2 < 1);


            } while (!estado.EsValidoCambiaGrupo(grupo1));

            StringBuffer S = new StringBuffer();
            RescateEstado nuevo_estado = new RescateEstado(estado);
            nuevo_estado.CambiaGrupoDeHelicoptero(index_rand_1, h_rand_1, h_rand_2, index_rand_2);
            S.append("Cambiar el grupo " + grupo1 + " del helicoptero " + h_rand_1 + " al helicoptero " + h_rand_2 + " despues del grupo " + grupo2 + ".\n");
            retVal.add(new Successor(S.toString(), nuevo_estado));
        }
        else if (rand <= fraction2) {
            // Aplica operador2
            int h_rand;
            int grupo1, grupo2;
            do {
                h_rand = random.nextInt(Nhelicopteros*Ncentros);
                ArrayList<Integer> gruposH = solucion.get(h_rand);
                int NgruposH = gruposH.size();

                do {
                    int index_rand_1 = random.nextInt(NgruposH-1);
                    int index_rand_2 = random.nextInt(NgruposH-1);
                    grupo1 = gruposH.get(index_rand_1);
                    grupo2 = gruposH.get(index_rand_2);
                } while (grupo1 < 0 || grupo2 < 0 || grupo1 == grupo2);

            } while(!estado.EsValidoCambioOrden(grupo1,grupo2));

            StringBuffer S = new StringBuffer();
            RescateEstado nuevo_estado = new RescateEstado(estado);
            nuevo_estado.CambiaOrdenGrupos(h_rand,grupo1,grupo2);
            S.append("Cambiar de orden el grupo " + grupo1 + " y el " + grupo2 + " en el helicoptero " + h_rand + ".\n");
            retVal.add(new Successor(S.toString(), nuevo_estado));
        }
        else if (rand <= fraction3) {
            // Aplica operador3
            int h_rand_1, h_rand_2;
            int grupo1, grupo2;
            do {
                h_rand_1 = random.nextInt(Nhelicopteros*Ncentros);
                h_rand_2 = random.nextInt(Nhelicopteros*Ncentros);
                while (h_rand_1 == h_rand_2) h_rand_2 = random.nextInt(Nhelicopteros*Ncentros);

                ArrayList<Integer> gruposH1 = solucion.get(h_rand_1);
                ArrayList<Integer> gruposH2 = solucion.get(h_rand_2);
                int NgruposH1 = gruposH1.size();
                int NgruposH2 = gruposH2.size();

                do {
                    int index_rand_1 = random.nextInt(NgruposH1-1);
                    int index_rand_2 = random.nextInt(NgruposH2-1);

                    grupo1 = gruposH1.get(index_rand_1);
                    grupo2 = gruposH2.get(index_rand_2);
                } while(grupo1 < 0 || grupo2 < 0);
            } while(!estado.EsValidoIntercambio(grupo1,grupo2));

            StringBuffer S = new StringBuffer();
            RescateEstado nuevo_estado = new RescateEstado(estado);
            nuevo_estado.IntercambiaGruposDeHelicopteros(grupo1, h_rand_1, grupo2, h_rand_2);
            S.append("Intercambiar el grupo " + grupo1 + " del helicoptero " + h_rand_1 + " con el grupo " + grupo2 + " del helicoptero " + h_rand_2 + ".\n");
            retVal.add(new Successor(S.toString(), nuevo_estado));
        }

        return retVal;
    }
}
