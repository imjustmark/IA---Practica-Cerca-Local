package src;
import IA.Desastres.*;

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

    static int capacidad_max = 15;
    static double tiempoCentro = 10.0;
    static double tiempoPersonaPriod1 = 2.0;
    static double tiempoPersonaPriod2 = 1.0;
    static double velocidadH = (100.0/60.0);

    int Ngrupos, Ncentros, Nhelicopteros;
    //Nhelicopteros por centro.

    public static Grupos grupos;
    public static Centros centros;

    public ArrayList<ArrayList<Integer> > solucion;
    Boolean[] gruposAsignados;

    public RescateEstado(int seed){
       Random random = new Random();
       Ngrupos = random.nextInt(maxgrupos);
       Ncentros = random.nextInt(maxcentros);
       Nhelicopteros = random.nextInt(maxhelicopteros);

       RescateEstado.grupos = new Grupos(Ngrupos, seed);
       RescateEstado.centros = new Centros(Ncentros, Nhelicopteros, seed);
    }

    public RescateEstado(int grupos, int centros, int helicopteros, int seed){
        Ngrupos = grupos;
        Ncentros = centros;
        Nhelicopteros = helicopteros;

        RescateEstado.grupos = new Grupos(grupos, seed);
        RescateEstado.centros = new Centros(centros, helicopteros, seed);
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
        solucion = new ArrayList<ArrayList<Integer>>(Nhelicopteros*Ncentros);
        Random random = new Random();
        Integer[] capacidadHelicopteros;
        capacidadHelicopteros = new Integer[Nhelicopteros*Ncentros];
        Arrays.fill(capacidadHelicopteros, capacidad_max);

        for (int g=0; g<Ngrupos; ++g) {
            int centro = random.nextInt(Ncentros);
            int helicoptero = random.nextInt(centros.get(centro).getNHelicopteros());
            int index = centro * Nhelicopteros + helicoptero;

            if (capacidadHelicopteros[index] - grupos.get(g).getNPersonas() > 0) {
                solucion.get(Ncentros * Nhelicopteros + helicoptero).add(g);
                capacidadHelicopteros[index] -= grupos.get(g).getNPersonas();
            } else if (capacidadHelicopteros[index] - grupos.get(g).getNPersonas() == 0) {
                solucion.get(Ncentros * Nhelicopteros + helicoptero).add(g);
                solucion.get(Ncentros * Nhelicopteros + helicoptero).add(-1);
                capacidadHelicopteros[index] = capacidad_max;
            } else if (capacidadHelicopteros[index] - grupos.get(g).getNPersonas() < 0) {
                solucion.get(Ncentros * Nhelicopteros + helicoptero).add(-1);
                solucion.get(Ncentros * Nhelicopteros + helicoptero).add(g);
                capacidadHelicopteros[index] = capacidad_max - grupos.get(g).getNPersonas();
            }
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

    public void ParadaEnCentro(int centro, int helicoptero, int grupo_previo) {
        int index = solucion.get(helicoptero).indexOf(grupo_previo);
        ++index;
        int aux = (-1) * centro;
        solucion.get(helicoptero).add(index, aux);
    }

    // COMPROBADORAS APLICACIÓN DE OPERADORES

    public boolean EsValidoCambiaGrupo(int grupo, int nuevo_helicoptero, int grupo_previo) {
        int carga_actual = 0;
        int index = solucion.get(nuevo_helicoptero).indexOf(grupo_previo);
        while (index >= 0 && solucion.get(nuevo_helicoptero).get(index) >= 0) {
            carga_actual = carga_actual + grupos.get(solucion.get(nuevo_helicoptero).get(index)).getNPersonas();
            --index;
        }
        return carga_actual + grupos.get(grupo).getNPersonas() <= capacidad_max;
    }

    public boolean EsValidoIntercambio(int grupo1, int helicoptero1, int grupo2, int helicoptero2) {
        int carga_h1 = 0;
        int carga_h2 = 0;
        int index1 = solucion.get(helicoptero1).indexOf(grupo1) - 1;
        int index2 = solucion.get(helicoptero2).indexOf(grupo2) - 1;
        while (index1 >= 0 && solucion.get(helicoptero1).get(index1) >= 0) {
            carga_h1 = carga_h1 + grupos.get(solucion.get(helicoptero1).get(index1)).getNPersonas();
            --index1;
        }
        while (index2 >= 0 && solucion.get(helicoptero2).get(index2) >= 0) {
            carga_h2 = carga_h2 + grupos.get(solucion.get(helicoptero2).get(index2)).getNPersonas();
            --index2;
        }
        return (carga_h1 + grupos.get(grupo2).getNPersonas() <= capacidad_max) &&
                (carga_h2 + grupos.get(grupo1).getNPersonas() <= capacidad_max);
    }
}