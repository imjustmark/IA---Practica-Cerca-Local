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

        solucion = new ArrayList<>();

        ArrayList<ArrayList<Integer>> solucion_a_copiar = estado_a_copiar.getSolucion();
        for (int i = 0; i < solucion_a_copiar.size(); ++i) {
            ArrayList<Integer> aux = new ArrayList<>();
            for (int j = 0; j < solucion_a_copiar.get(i).size(); ++j) {
                aux.add(solucion_a_copiar.get(i).get(j));
            }
            solucion.add(aux);
        }
    }

    public boolean isGoalState() {
        return false;
    }

    private Boolean TodosGruposAsignados(){
        for (Boolean gruposAsignado : gruposAsignados) {
            if (!gruposAsignado) return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private ArrayList<Integer> randomArray(){
        ArrayList<Integer> array;
        array = new ArrayList<>();
        for (int i=0; i < Ngrupos; ++i){
            array.add(i);
        }

        Random random = new Random();
        for (int i=array.size()-1; i > 0; --i){
           int index = random.nextInt(i+1);
           int aux = array.get(index);
           array.set(index, array.get(i));
           array.set(i, aux);
    }
   return array;
    }

    //-1 implica helicoptero en centro
    public void EstadoInicial(){
        solucion = new ArrayList<>();
        for (int i = 0; i < Nhelicopteros*Ncentros; ++i) {
            ArrayList<Integer> aux = new ArrayList<>();
            aux.add(-1);
            solucion.add(i, aux);
        }
        ArrayList<Integer> arrayGrupos = randomArray();
        gruposAsignados = new Boolean[Ngrupos];
        Arrays.fill(gruposAsignados, Boolean.FALSE);
        Random random = new Random();
        int g = 0;

        while (g < Ngrupos){
            for (int i=0; i < Ncentros; ++i)
                for (int j = 0; j < Nhelicopteros; ++j) {
                    int capacidadHelicoptero = capacidad_max;
                    int numGrupos = 3;
                    while (capacidadHelicoptero > 0 && numGrupos > 0 && g < Ngrupos) {
                        int personasGrupo = grupos.get(g).getNPersonas();

                        if (capacidadHelicoptero - personasGrupo >= 0) {
                            solucion.get(i * Nhelicopteros + j).add(g);
                            gruposAsignados[g] = Boolean.TRUE;
                            capacidadHelicoptero -= personasGrupo;
                            numGrupos--;
                            ++g;
                        }
                        else {
                            capacidadHelicoptero = 0;
                        }
                    }
                    solucion.get(i*Nhelicopteros + j).add(-1);
                }
        }
    }


    public void EstadoInicial2(){
        solucion = new ArrayList<>();
        ArrayList<Integer> gruposDesdeCentro = new ArrayList<Integer>(Arrays.asList(new Integer[Nhelicopteros*Ncentros]));
        for (int i = 0; i < Nhelicopteros*Ncentros; ++i) {
            ArrayList<Integer> aux = new ArrayList<>();
            aux.add(-1);
            solucion.add(i, aux);

            gruposDesdeCentro.add(i, 0);
        }
        Random random = new Random();
        ArrayList<Integer> capacidadHelicopteros = new ArrayList<Integer>(Arrays.asList(new Integer[Nhelicopteros*Ncentros]));
        Collections.fill(capacidadHelicopteros, capacidad_max);


        for (int g=0; g<Ngrupos; ++g) {
            int centro = random.nextInt(Ncentros);
            int helicoptero = random.nextInt(Nhelicopteros);
            int index = centro * Nhelicopteros + helicoptero;

            int carga_h = capacidadHelicopteros.get(index) - grupos.get(g).getNPersonas();
            int gruposDCentro = gruposDesdeCentro.get(index);
            if (carga_h > 0 && gruposDCentro < 3) {
                solucion.get(index).add(g);
                gruposDesdeCentro.set(index, gruposDCentro + 1);
                capacidadHelicopteros.set(index, carga_h);
            } else if (carga_h == 0 && gruposDCentro < 3) {
                solucion.get(index).add(g);
                solucion.get(index).add(-1);
                gruposDesdeCentro.set(index, 0);
                capacidadHelicopteros.set(index, capacidad_max);
            }
            else {
                solucion.get(index).add(-1);
                solucion.get(index).add(g);
                gruposDesdeCentro.set(index, 1);
                capacidadHelicopteros.set(index, capacidad_max - grupos.get(g).getNPersonas());
            }
        }
        for (int i = 0; i < Nhelicopteros*Ncentros; ++i) {
            if (solucion.get(i).get(solucion.get(i).size()-1) != -1)
                solucion.get(i).add(-1);
        }
    }


    public ArrayList<ArrayList<Integer>> getSolucion() {
        return solucion;
    }

    // OPERADORES
    // Los helicópteros van numerados de 0 a H.
    // Los grupos van numerados de 0 a G.
    // La posicion i del vector solución representa los grupos recogidos por el helicóptero i.

    public void CambiaGrupoDeHelicoptero(int index_g, int helicoptero, int nuevo_helicoptero, int index_ga) {
        int grupo = solucion.get(helicoptero).remove(index_g);
        if (solucion.get(helicoptero).get(index_g) == -1 && solucion.get(helicoptero).get(index_g -1) == -1) {
            solucion.get(helicoptero).remove(index_g);
        }
        int num_grupos_antes = 0;
        int num_grupos_despues = 0;
        int carga_antes = 0;
        int carga_despues = 0;
        int index_min = index_ga - 1;
        while (index_min >= 0 && solucion.get(nuevo_helicoptero).get(index_min) >= 0) {
            carga_antes = carga_antes + grupos.get(solucion.get(nuevo_helicoptero).get(index_min)).getNPersonas();
            ++ num_grupos_antes;
            --index_min;
        }
        int index_max = index_ga;
        while (index_max < solucion.get(nuevo_helicoptero).size() && solucion.get(nuevo_helicoptero).get(index_max) >= 0) {
            carga_despues = carga_despues + grupos.get(solucion.get(nuevo_helicoptero).get(index_max)).getNPersonas();
            ++num_grupos_despues;
            ++index_max;
        }
        boolean addedB = false;
        boolean addedA = false;
        if(carga_despues + grupos.get(grupo).getNPersonas() > capacidad_max || num_grupos_despues > 2) {
            solucion.get(nuevo_helicoptero).add(index_ga, -1);
            addedA = true;
        }
        solucion.get(nuevo_helicoptero).add(index_ga , grupo);
        if(carga_antes + grupos.get(grupo).getNPersonas() > capacidad_max || num_grupos_antes > 2) {
            solucion.get(nuevo_helicoptero).add(index_ga, -1);
            addedB = true;
        }
        if(!addedA && !addedB && ((carga_antes + carga_despues + grupos.get(grupo).getNPersonas() > capacidad_max) || (num_grupos_despues +  num_grupos_antes > 2))){
            solucion.get(nuevo_helicoptero).add(index_ga, -1);
        }
    }

    public void CambiaOrdenGrupos(int helicoptero, int grupo1, int grupo2) {
        int carga_actual_1_antes = 0;
        int carga_actual_1_despues = 0;
        int carga_actual_2_antes = 0;
        int carga_actual_2_despues = 0;
        int index1 = solucion.get(helicoptero).indexOf(grupo1);
        int index2 = solucion.get(helicoptero).indexOf(grupo2);

        int index_min_1 = index1 - 1;
        while (index_min_1 >= 0 && solucion.get(helicoptero).get(index_min_1) >= 0) {
            carga_actual_1_antes = carga_actual_1_antes + grupos.get(solucion.get(helicoptero).get(index_min_1)).getNPersonas();
            --index_min_1;
        }
        carga_actual_1_antes = carga_actual_1_antes + grupos.get(grupo2).getNPersonas();

        int index_max_1 = index1 + 1;
        while (index_max_1 < solucion.get(helicoptero).size() && solucion.get(helicoptero).get(index_max_1) >= 0) {
            carga_actual_1_despues = carga_actual_1_despues + grupos.get(solucion.get(helicoptero).get(index_max_1)).getNPersonas();
            ++index_max_1;
        }

        carga_actual_1_despues = carga_actual_1_despues + grupos.get(grupo2).getNPersonas();

        int index_min_2 = index2 - 1;
        while (index_min_2 >= 0 && solucion.get(helicoptero).get(index_min_2) >= 0) {
            carga_actual_2_antes = carga_actual_2_antes + grupos.get(solucion.get(helicoptero).get(index_min_2)).getNPersonas();
            --index_min_2;
        }

        carga_actual_2_antes = carga_actual_2_antes + grupos.get(grupo1).getNPersonas();

        int index_max_2 = index2 + 1;
        while (index_max_2 < solucion.get(helicoptero).size() && solucion.get(helicoptero).get(index_max_2) >= 0) {
            carga_actual_2_despues = carga_actual_2_despues + grupos.get(solucion.get(helicoptero).get(index_max_2)).getNPersonas();
            ++index_max_2;
        }

        carga_actual_2_despues = carga_actual_2_despues + grupos.get(grupo1).getNPersonas();

        solucion.get(helicoptero).set(index1, grupo2);
        solucion.get(helicoptero).set(index2, grupo1);
        boolean added1A = false;
        boolean added1B = false;
        boolean added2A = false;
        boolean added2B = false;
        if(carga_actual_2_despues > capacidad_max) {
            solucion.get(helicoptero).add(index2 + 1, -1);
            added1A = true;
        }


        if(carga_actual_2_antes > capacidad_max){
            solucion.get(helicoptero).add(index2, -1);
            added1B = true;
        }

        if(!added1A && !added1B && (carga_actual_1_antes + carga_actual_1_despues - grupos.get(grupo2).getNPersonas() > capacidad_max)){
            solucion.get(helicoptero).add(index2, -1);
        }
        if(carga_actual_1_despues > capacidad_max) {
            solucion.get(helicoptero).add(index1 + 1, -1);
            added2A = true;
        }
        if(carga_actual_1_antes > capacidad_max) {
            solucion.get(helicoptero).add(index1, -1);
            added2B = true;
        }
        if(!added2A && !added2B && (carga_actual_2_antes + carga_actual_2_despues - grupos.get(grupo1).getNPersonas() > capacidad_max)){
            solucion.get(helicoptero).add(index1, -1);
        }
    }

    public void IntercambiaGruposDeHelicopteros(int grupo1, int helicoptero1, int grupo2, int helicoptero2) {
        int carga_h1_antes = 0;
        int carga_h1_despues = 0;
        int carga_h2_antes = 0;
        int carga_h2_despues = 0;
        int index1 = solucion.get(helicoptero1).indexOf(grupo1);
        int index2 = solucion.get(helicoptero2).indexOf(grupo2);

        int index_min_1 = index1 - 1;
        while (index_min_1 >= 0 && solucion.get(helicoptero1).get(index_min_1) >= 0) {
            carga_h1_antes = carga_h1_antes + grupos.get(solucion.get(helicoptero1).get(index_min_1)).getNPersonas();
            --index_min_1;
        }
        carga_h1_antes = carga_h1_antes + grupos.get(grupo2).getNPersonas();

        int index_max_1 = index1 + 1;
        while (index_max_1 < solucion.get(helicoptero1).size()  && solucion.get(helicoptero1).get(index_max_1) >= 0) {
            carga_h1_despues = carga_h1_despues + grupos.get(solucion.get(helicoptero1).get(index_max_1)).getNPersonas();
            ++index_max_1;
        }
        carga_h1_despues = carga_h1_despues + grupos.get(grupo2).getNPersonas();

        int index_min_2 = index2 - 1;
        while (index_min_2 >= 0 && solucion.get(helicoptero2).get(index_min_2) >= 0) {
            carga_h2_antes = carga_h2_antes + grupos.get(solucion.get(helicoptero2).get(index_min_2)).getNPersonas();
            --index_min_2;
        }
        carga_h2_antes = carga_h2_antes + grupos.get(grupo1).getNPersonas();

        int index_max_2 = index2 + 1;
        while (index_max_2 < solucion.get(helicoptero2).size()  && solucion.get(helicoptero2).get(index_max_2) >= 0) {
            carga_h2_despues = carga_h2_despues + grupos.get(solucion.get(helicoptero2).get(index_max_2)).getNPersonas();
            ++index_max_2;
        }
        carga_h2_despues = carga_h2_despues + grupos.get(grupo1).getNPersonas();

        solucion.get(helicoptero1).set(index1, grupo2);
        solucion.get(helicoptero2).set(index2, grupo1);
        boolean added1A = false;
        boolean added1B = false;
        boolean added2A = false;
        boolean added2B = false;
        if(carga_h1_despues > capacidad_max){
            solucion.get(helicoptero1).add(index1 + 1, -1);
            added1A = true;
        }
        if(carga_h1_antes > capacidad_max) {
            solucion.get(helicoptero1).add(index1, -1);
            added1B = true;
        }
        if(!added1A && !added1B && carga_h1_antes + carga_h1_despues - grupos.get(grupo2).getNPersonas() > capacidad_max) {
            solucion.get(helicoptero1).add(index1, -1);
        }

        if(carga_h2_despues > capacidad_max){
            solucion.get(helicoptero2).add(index2 + 1, -1);
            added2B = true;
        }
        if(carga_h2_antes > capacidad_max) {
            solucion.get(helicoptero2).add(index2, -1);
            added2A = true;
        }
        if(!added2A && !added2B && carga_h2_antes + carga_h2_despues - grupos.get(grupo1).getNPersonas() > capacidad_max) {
            solucion.get(helicoptero2).add(index2, -1);
        }
    }

    public void BorrarParada(int helicoptero, int index) {
        solucion.get(helicoptero).remove(index);
    }

    public void MoverParada(int helicoptero, int index, int movimiento) {
        if (movimiento > 0) {
            solucion.get(helicoptero).add(index+movimiento, -1);
            solucion.get(helicoptero).remove(index);
        }
        else {
            solucion.get(helicoptero).remove(index);
            solucion.get(helicoptero).add(index + movimiento, -1);
        }
    }

    // COMPROBADORAS APLICACIÓN DE OPERADORES

    public boolean EsValidoCambiaGrupo(int grupo) {
        return grupo >= 0;
    }

    // Aqui está hecho, faltaría en las demás.
    public boolean EsValidoCambioOrden(int helicoptero, int grupo1, int grupo2) {
        return (grupo1 >= 0) && (grupo2 >= 0);
    }

    public boolean EsValidoIntercambio(int grupo1, int grupo2) {
        return (grupo1 >= 0) && (grupo2 >= 0);
    }

    public boolean EsValidoBorrarParada(int helicoptero, int indice) {
        ArrayList<Integer> grupos_helicoptero = solucion.get(helicoptero);

        int carga_anterior = 0;
        int cuantos_antes = 0;
        int index_min = indice - 1;
        while (index_min >= 0 && grupos_helicoptero.get(index_min) >= 0) {
            carga_anterior = carga_anterior + grupos.get(solucion.get(helicoptero).get(index_min)).getNPersonas();
            ++cuantos_antes;
            --index_min;
        }

        int carga_posterior = 0;
        int cuantos_despues = 0;
        int index_max = indice + 1;
        while (index_max < grupos_helicoptero.size() && grupos_helicoptero.get(index_max) >= 0) {
            carga_posterior = carga_posterior + grupos.get(solucion.get(helicoptero).get(index_max)).getNPersonas();
            ++cuantos_despues;
            ++index_max;
        }

        return (carga_anterior + carga_posterior <= capacidad_max) && (cuantos_antes + cuantos_despues <= 3);
    }

    public boolean EsValidoMoverParada(int helicoptero, int indice, int movimiento) {
        boolean valid = (indice + movimiento < solucion.get(helicoptero).size() - 1 && indice + movimiento > 0);
        if (!valid) return false;
        if (movimiento > 0) {
            for (int i = indice + 1; i <= movimiento + indice + 1; ++i) {
                int id = solucion.get(helicoptero).get(i);
                if (id == -1) return false;
            }
            int cuantos = 0;
            int carga = 0;
            int index_min = indice - 1;
            while (index_min >= 0 && solucion.get(helicoptero).get(index_min) >= 0) {
                carga = carga + grupos.get(solucion.get(helicoptero).get(index_min)).getNPersonas();
                ++cuantos;
                --index_min;
            }
            for (int i = indice + 1; i < indice + 1 + movimiento; ++i) {
                carga = carga + grupos.get(solucion.get(helicoptero).get(i)).getNPersonas();
                ++cuantos;
            }
            if (carga > capacidad_max || cuantos > 3) return false;
            else return true;
        }
        else {
            for (int i = indice - 1; i >= movimiento + indice - 1; --i) {
                int id = solucion.get(helicoptero).get(i);
                if (id == -1) return false;
            }
            int cuantos = 0;
            int carga = 0;
            int index_max = indice + 1;
            while (index_max < solucion.get(helicoptero).size() && solucion.get(helicoptero).get(index_max) >= 0) {
                carga = carga + grupos.get(solucion.get(helicoptero).get(index_max)).getNPersonas();
                ++cuantos;
                ++index_max;
            }
            for (int i = indice - 1; i > indice - 1 + movimiento; --i) {
                carga = carga + grupos.get(solucion.get(helicoptero).get(i)).getNPersonas();
                ++cuantos;
            }
            if (carga > capacidad_max || cuantos > 3) return false;
            else return true;
        }
    }

    void print_solution(){
        /*
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
         */
        System.out.print("Valor maxim: ");
        System.out.println(valor_maxim());
        System.out.println();

        System.out.print("Valor maxim Priod 1: ");
        System.out.println(valor_maxim_priod1());
        System.out.println();

        System.out.print("Suma: ");
        System.out.println(suma());
        System.out.println();
    }

    double valor_maxim(){
        int num_h = Nhelicopteros*Ncentros;
        double max = 0.0f;
        for(int i = 0; i < num_h; ++i){
            double tempsH = calculaTiempoH(i);
            if(tempsH > max) max = tempsH;
        }
        return max;
    }
    double valor_maxim_priod1(){
        int num_h = Nhelicopteros*Ncentros;
        double max = 0.0f;
        for(int i = 0; i < num_h; ++i){
            double tempsH = calculaTiempoHPriod1(i);
            if(tempsH > max) max = tempsH;
        }
        return max;
    }
    double suma(){
        int num_h = Nhelicopteros*Ncentros;
        double sum = 0.0f;
        for(int i = 0; i < num_h; ++i){
            double tempsH = calculaTiempoH(i);
            sum += tempsH;
        }
        return sum;
    }

    public double tiempoDest(int coordx1, int coordy1, int coordx2, int coordy2){
        return Math.sqrt(Math.pow(coordx1-coordx2, 2) + Math.pow(coordy1-coordy2, 2))/velocidadH;
    }

    public double calculaTiempoH(int H){
        ArrayList<ArrayList<Integer>> conf = getSolucion();

        int num_dest = conf.get(H).size();
        double sum = 0.0f;
        for(int i = 0; i < num_dest -1; ++i){

            int coordx1, coordy1, coordx2, coordy2;
            if(conf.get(H).get(i) < 0){
                int centro = H/(Nhelicopteros);
                coordx1 = centros.get(centro).getCoordX();
                coordy1 = centros.get(centro).getCoordY();
            }
            else{
                coordx1 = grupos.get(conf.get(H).get(i)).getCoordX();
                coordy1 = grupos.get(conf.get(H).get(i)).getCoordY();
            }

            if(conf.get(H).get(i+1) < 0){
                int centro = H/(Nhelicopteros);
                coordx2 = centros.get(centro).getCoordX();
                coordy2 = centros.get(centro).getCoordY();
            }
            else{
                coordx2 = grupos.get(conf.get(H).get(i+1)).getCoordX();
                coordy2 = grupos.get(conf.get(H).get(i+1)).getCoordY();
            }
            double tempsViatge = tiempoDest(coordx1, coordy1, coordx2, coordy2);
            sum += tempsViatge;

            if(conf.get(H).get(i+1) < 0){
                if(i+1 < num_dest) sum += tiempoCentro;
            }
            else{
                if(grupos.get(conf.get(H).get(i+1)).getPrioridad() == 1){
                    double tempsRecollir = grupos.get(conf.get(H).get(i+1)).getNPersonas()*tiempoPersonaPriod1;
                    sum += tempsRecollir;
                }
                else if(grupos.get(conf.get(H).get(i+1)).getPrioridad() == 2){
                    double tempsRecollir = grupos.get(conf.get(H).get(i+1)).getNPersonas()*tiempoPersonaPriod2;
                    sum += tempsRecollir;
                }
            }
        }
        return sum;
    }

    public double calculaTiempoHPriod1(int H){
        ArrayList<ArrayList<Integer>> conf = getSolucion();

        int num_dest = conf.get(H).size();
        double sum = 0.0f;
        double tempsPriod1 = 0.0f;
        for(int i = 0; i < num_dest -1; ++i){

            int coordx1, coordy1, coordx2, coordy2;
            if(conf.get(H).get(i) < 0){
                int centro = H/(Nhelicopteros);
                coordx1 = centros.get(centro).getCoordX();
                coordy1 = centros.get(centro).getCoordY();
            }
            else{
                coordx1 = grupos.get(conf.get(H).get(i)).getCoordX();
                coordy1 = grupos.get(conf.get(H).get(i)).getCoordY();
            }

            if(conf.get(H).get(i+1) < 0){
                int centro = H/(Nhelicopteros);
                coordx2 = centros.get(centro).getCoordX();
                coordy2 = centros.get(centro).getCoordY();
            }
            else{
                coordx2 = grupos.get(conf.get(H).get(i+1)).getCoordX();
                coordy2 = grupos.get(conf.get(H).get(i+1)).getCoordY();
            }
            double tempsViatge = tiempoDest(coordx1, coordy1, coordx2, coordy2);
            sum += tempsViatge;

            if(conf.get(H).get(i+1) < 0){
                if(i+1 < num_dest) sum += tiempoCentro;
            }
            else{
                if(grupos.get(conf.get(H).get(i+1)).getPrioridad() == 1){
                    double tempsRecollir = grupos.get(conf.get(H).get(i+1)).getNPersonas()*tiempoPersonaPriod1;
                    sum += tempsRecollir;
                    tempsPriod1 = sum;
                }
                else if(grupos.get(conf.get(H).get(i+1)).getPrioridad() == 2){
                    double tempsRecollir = grupos.get(conf.get(H).get(i+1)).getNPersonas()*tiempoPersonaPriod2;
                    sum += tempsRecollir;
                }
            }
        }
        return tempsPriod1;
    }

    boolean comprobacionCapacidad() {
        int carga = 0;
        for (int i = 0; i < solucion.size(); ++i) {
            ArrayList<Integer> grupos_helicoptero = solucion.get(i);
            for (int j = 0; j < grupos_helicoptero.size(); ++j) {
                int ID = grupos_helicoptero.get(j);
                if (ID == -1) carga = 0;
                else carga = carga + grupos.get(ID).getNPersonas();
                if (carga > capacidad_max) return false;
            }
        }
        return true;
    }

    boolean comprobacionNumero(){
        int num = 0;
        for (int i = 0; i < solucion.size(); ++i) {
            ArrayList<Integer> grupos_helicoptero = solucion.get(i);
            for (int j = 0; j < grupos_helicoptero.size(); ++j) {
                int ID = grupos_helicoptero.get(j);
                if (ID == -1) num = 0;
                else ++num;
                if (num > 3) return false;
            }
        }
        return true;
    }
}