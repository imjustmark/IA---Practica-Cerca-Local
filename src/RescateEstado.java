package src;
import IA.Desastres.*;

import java.lang.reflect.Array;
import java.util.*;

/***********************************************************************
 ***                                                                 ***
 ***   Práctica IA Q2 / 2022                                         ***
 ***                                                                 ***
 ***   Alumnos: Marc Delgado Sánchez                                 ***
 ***            Mario Font Blanc                                     ***
 ***            Manel Piera Garrigosa                                ***
 ***   Grupo:   14                                                   ***
 ***                                                                 ***
 ***   src.RescateEstado.java                                        ***
 ***                                                                 ***
 ***********************************************************************/
//hola
public class RescateEstado {

    static int maxgrupos = 20;
    static int maxcentros = 20;
    static int maxhelicopteros = 20;
    //maxhelicopteros por centro.

    static int Ngrupos = 100;
    static int Ncentros = 5;
    static int Nhelicopteros = 1;

    static int capacidad_max = 15;
    static double tiempoCentro = 10.0;
    static double tiempoPersonaPriod1 = 2.0;
    static double tiempoPersonaPriod2 = 1.0;
    static double velocidadH = (100.0/60.0);

    //Nhelicopteros por centro.

    public static Grupos grupos;
    public static Centros centros;

    public ArrayList<ArrayList<Integer> > solucion;
    Boolean[] gruposAsignados;

    public RescateEstado(int seed){
//       Random random = new Random();

       RescateEstado.grupos = new Grupos(Ngrupos, seed);
       RescateEstado.centros = new Centros(Ncentros, Nhelicopteros, seed);

       EstadoInicial2();
    }

    public RescateEstado(int grupos, int centros, int helicopteros, int seed){
        Ngrupos = grupos;
        Ncentros = centros;
        Nhelicopteros = helicopteros;

        RescateEstado.grupos = new Grupos(grupos, seed);
        RescateEstado.centros = new Centros(centros, helicopteros, seed);
    }

    public RescateEstado(RescateEstado estado_a_copiar) {
        Ngrupos = estado_a_copiar.Ngrupos;
        Ncentros = estado_a_copiar.Ncentros;
        Nhelicopteros = estado_a_copiar.Nhelicopteros;

        solucion = estado_a_copiar.getSolucion();
    }

    //-1 implica helicoptero en centro
    public void EstadoInicial(){
        solucion = new ArrayList<ArrayList<Integer>>(Nhelicopteros*Ncentros);
        gruposAsignados = new Boolean[Ngrupos];
        Arrays.fill(gruposAsignados, Boolean.FALSE);
        Random random = new Random();

        while (!TodosGruposAsignados()){
            for (int i=0; i < Ncentros; ++i)
                for (int j = 0; j < centros.get(i).getNHelicopteros(); ++j) {
                    int capacidadHelicoptero = capacidad_max;
                    while (capacidadHelicoptero > 0) {
                        int g = random.nextInt(Ngrupos);
                        while (gruposAsignados[g])
                            g = random.nextInt(Ngrupos);
                        int personasGrupo = grupos.get(g).getNPersonas();

                        if (capacidad_max - personasGrupo > 0) {
                            gruposAsignados[g] = Boolean.TRUE;
                            solucion.get(Ncentros * Nhelicopteros + j).add(g);
                            capacidadHelicoptero -= personasGrupo;
                        } else if (capacidad_max - personasGrupo == 0) {
                            gruposAsignados[g] = Boolean.TRUE;
                            solucion.get(Ncentros * Nhelicopteros + j).add(g);
                            solucion.get(Ncentros * Nhelicopteros + j).add(-1);
                            capacidadHelicoptero -= personasGrupo;
                        }
                    }
                }
        }
    }

    public void EstadoInicial2(){
        solucion = new ArrayList<>();
        for (int i = 0; i < Nhelicopteros*Ncentros; ++i) {
            ArrayList<Integer> aux = new ArrayList<>();
            aux.add(-1);
            solucion.add(i, aux);
        }
        Random random = new Random();
        ArrayList<Integer> capacidadHelicopteros = new ArrayList<Integer>(Arrays.asList(new Integer[Nhelicopteros*Ncentros]));
        Collections.fill(capacidadHelicopteros, capacidad_max);


        for (int g=0; g<Ngrupos; ++g) {
            int centro = random.nextInt(Ncentros);
            int helicoptero = random.nextInt(centros.get(centro).getNHelicopteros());
            int index = centro * Nhelicopteros + helicoptero;

            if (capacidadHelicopteros.get(index) - grupos.get(g).getNPersonas() > 0) {
                solucion.get(helicoptero).add(g);
                capacidadHelicopteros.set(index, capacidadHelicopteros.get(index) - grupos.get(g).getNPersonas());
            } else if (capacidadHelicopteros.get(index) - grupos.get(g).getNPersonas() == 0) {
                solucion.get(index).add(g);
                solucion.get(index).add(-1);
                capacidadHelicopteros.set(index, capacidad_max);
            }
            else {
                solucion.get(index).add(-1);
                solucion.get(index).add(g);
                capacidadHelicopteros.set(index, capacidad_max - grupos.get(g).getNPersonas());
            }
        }
        for (int i = 0; i < Nhelicopteros*Ncentros; ++i) {
            if (solucion.get(i).get(solucion.get(i).size()-1) != -1)
                solucion.get(i).add(-1);
        }
    }

    private Boolean TodosGruposAsignados(){
        for (Boolean gruposAsignado : gruposAsignados) {
            if (!gruposAsignado) return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public ArrayList<ArrayList<Integer>> getSolucion() {
        return solucion;
    }

    // OPERADORES
    // Los helicópteros van numerados de 0 a H.
    // Los grupos van numerados de 0 a G.
    // La posicion i del vector solución representa los grupos recogidos por el helicóptero i.

    public void CambiaGrupoDeHelicoptero(int grupo, int helicoptero, int nuevo_helicoptero, int grupo_previo) {
        solucion.get(helicoptero).remove(grupo);
        int indice_anterior = solucion.get(nuevo_helicoptero).indexOf(grupo_previo);
        ++indice_anterior;
        solucion.get(nuevo_helicoptero).add(indice_anterior,grupo);
    }

    public void CambiaGrupoDeHelicoptero(int grupo, int helicoptero, int nuevo_helicoptero) {
        solucion.get(helicoptero).remove(grupo);
        solucion.get(nuevo_helicoptero).add(0,grupo);
    }

    public void CambiaOrdenGrupos(int helicoptero, int grupo1, int grupo2) {
        int index1 = solucion.get(helicoptero).indexOf(grupo1);
        int index2 = solucion.get(helicoptero).indexOf(grupo2);
        solucion.get(helicoptero).set(index1, grupo2);
        solucion.get(helicoptero).set(index2, grupo1);
    }

    public void IntercambiaGruposDeHelicopteros(int grupo1, int helicoptero1, int grupo2, int helicoptero2) {
        int index1 = solucion.get(helicoptero1).indexOf(grupo1);
        int index2 = solucion.get(helicoptero2).indexOf(grupo2);
        solucion.get(helicoptero1).set(index1, grupo2);
        solucion.get(helicoptero2).set(index2, grupo1);
    }

    public void ParadaEnCentro(int helicoptero, int grupo_previo) {
        int index = solucion.get(helicoptero).indexOf(grupo_previo);
        ++index;
        solucion.get(helicoptero).add(index, -1);
    }

    // COMPROBADORAS APLICACIÓN DE OPERADORES

    public boolean EsValidoCambiaGrupo(int grupo, int nuevo_helicoptero, int grupo_previo) {
        int carga_actual = 0;
        int index = solucion.get(nuevo_helicoptero).indexOf(grupo_previo);
        int index_min = index - 1;
        while (index_min >= 0 && solucion.get(nuevo_helicoptero).get(index_min) >= 0) {
            carga_actual = carga_actual + grupos.get(solucion.get(nuevo_helicoptero).get(index_min)).getNPersonas();
            --index_min;
        }
        int index_max = index + 1;
        while (index_max < solucion.get(nuevo_helicoptero).size() && solucion.get(nuevo_helicoptero).get(index_min) >= 0) {
            carga_actual = carga_actual + grupos.get(solucion.get(nuevo_helicoptero).get(index_max)).getNPersonas();
            ++index_max;
        }
        return carga_actual + grupos.get(grupo).getNPersonas() <= capacidad_max;
    }

    // todo: Es posible que se deba comprobar que en los intervalos entre visitas a la base no se supere la carga maxima.
    // Aqui está hecho, faltaría en las demás.
    public boolean EsValidoCambioOrden(int helicoptero, int grupo1, int grupo2) {
        int carga_actual_1 = 0;
        int carga_actual_2 = 0;
        int index_original_1 = solucion.get(helicoptero).indexOf(grupo1);
        int index_original_2 = solucion.get(helicoptero).indexOf(grupo2);

        int index_max_1 = index_original_1 + 1;
        while (index_max_1 < solucion.get(helicoptero).size() && solucion.get(helicoptero).get(index_max_1) >= 0) {
            carga_actual_1 = carga_actual_1 + grupos.get(solucion.get(helicoptero).get(index_max_1)).getNPersonas();
            ++index_max_1;
        }
        int index_min_1 = index_original_1 - 1;
        while (index_min_1 >= 0 && solucion.get(helicoptero).get(index_min_1) >= 0) {
            carga_actual_1 = carga_actual_1 + grupos.get(solucion.get(helicoptero).get(index_min_1)).getNPersonas();
            --index_min_1;
        }
        int index_max_2 = index_original_2 + 1;
        while (index_max_2 < solucion.get(helicoptero).size() && solucion.get(helicoptero).get(index_max_2) >= 0) {
            carga_actual_2 = carga_actual_2 + grupos.get(solucion.get(helicoptero).get(index_max_2)).getNPersonas();
            ++index_max_2;
        }
        int index_min_2 = index_original_2 - 1;
        while (index_min_2 >= 0 && solucion.get(helicoptero).get(index_min_2) >= 0) {
            carga_actual_2 = carga_actual_2 + grupos.get(solucion.get(helicoptero).get(index_min_2)).getNPersonas();
            --index_min_2;
        }

        return (carga_actual_1 + grupos.get(grupo2).getNPersonas() <= capacidad_max) &&
                (carga_actual_2 + grupos.get(grupo1).getNPersonas() <= capacidad_max);
    }

    public boolean EsValidoIntercambio(int grupo1, int helicoptero1, int grupo2, int helicoptero2) {
        int carga_h1 = 0;
        int carga_h2 = 0;
        int index1 = solucion.get(helicoptero1).indexOf(grupo1);
        int index2 = solucion.get(helicoptero2).indexOf(grupo2);

        int index_min_1 = index1 - 1;
        while (index_min_1 >= 0 && solucion.get(helicoptero1).get(index_min_1) >= 0) {
            carga_h1 = carga_h1 + grupos.get(solucion.get(helicoptero1).get(index_min_1)).getNPersonas();
            --index_min_1;
        }
        int index_max_1 = index1 + 1;
        while (index_max_1 < solucion.get(helicoptero1).size()  && solucion.get(helicoptero1).get(index_max_1) >= 0) {
            carga_h1 = carga_h1 + grupos.get(solucion.get(helicoptero1).get(index_max_1)).getNPersonas();
            ++index_max_1;
        }
        int index_min_2 = index2 - 1;
        while (index_min_2 >= 0 && solucion.get(helicoptero2).get(index_min_2) >= 0) {
            carga_h2 = carga_h2 + grupos.get(solucion.get(helicoptero2).get(index_min_2)).getNPersonas();
            --index_min_2;
        }
        int index_max_2 = index2 + 1;
        while (index_max_2 < solucion.get(helicoptero2).size()  && solucion.get(helicoptero2).get(index_max_2) >= 0) {
            carga_h2 = carga_h2 + grupos.get(solucion.get(helicoptero2).get(index_max_2)).getNPersonas();
            ++index_max_2;
        }
        return (carga_h1 + grupos.get(grupo2).getNPersonas() <= capacidad_max) &&
                (carga_h2 + grupos.get(grupo1).getNPersonas() <= capacidad_max);
    }

    void print_solution(){
        for(int i = 0; i < solucion.size(); ++i){
            System.out.print("Helicoptero ");
            System.out.print(i);
            System.out.println(":");
            for(int j = 0; j < solucion.get(i).size(); ++j){
                System.out.print(solucion.get(i).get(j));
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}