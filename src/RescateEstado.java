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

    static int capacidad_max = 15;

    int Ngrupos, Ncentros, Nhelicopteros;

    public static Grupos grupos;
    public static Centros centros;

    public ArrayList<ArrayList<Integer> > solucion;

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

    public void EstadoInicialRandom(){
        solucion = new ArrayList<ArrayList<Integer>>(Nhelicopteros*Ncentros);
        Random random = new Random();

        for (int i=0; i < Ngrupos; ++i){
            //seleccionamos un centro random con helicopteros > 0
            int c = random.nextInt(Ncentros);
            while (centros.get(c).getNHelicopteros() == 0)
                c = random.nextInt(Ncentros);

            for (int j=0; j < Nhelicopteros; j++){
                if (solucion.get(j).get(solucion.get(j).size() - 1) == -c)
                    solucion.get(j).add(i);
            }
        }
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