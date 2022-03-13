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
}