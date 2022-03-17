package src;
import IA.Desastres.*;
import com.sun.security.auth.UnixNumericGroupPrincipal;

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

public class RescateEstado {

    int maxgrupos = 20;
    int maxcentros = 20;
    int maxhelicopteros = 20;

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
           int c = random.nextInt(Ncentros);
           while (centros.get(c).getNHelicopteros() == 0)
               c = random.nextInt(Ncentros);


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
}