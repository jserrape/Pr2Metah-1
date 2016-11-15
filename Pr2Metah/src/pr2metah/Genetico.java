/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr2metah;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Xenahort
 *
 *
 * EN EL SELECCIONA PADRE HABRIA QUE PLANTEARSE COGERLO ALEATORIO REVISAR LO DE
 * LA VARIABLE muta
 *
 */
public class Genetico {

    int tamPoblacion = 50;
    int tabu;
    ArrayList<int[]> poblacion, descendencia;
    int costes[], costesAux[];
    double probGen;
    int nGeneracion;
    int anteriorMejor;

    public void prueba(int x, int y, int matriz[][], int cubre[], Pair cubreOrdenado[]) {
        poblacion = new ArrayList<>();
        costes = new int[tamPoblacion];
        Random rand = new Random();
        generarPoblacion(x, y, tamPoblacion, cubreOrdenado, matriz);
        for (int i = 0; i < tamPoblacion; i++) {
            System.out.println(i + ": " + costes[i]);
        }
        descendencia = new ArrayList<>();
        costesAux = new int[tamPoblacion];
        int muta = (Math.abs(rand.nextInt() % tamPoblacion));
        
        for (int i = 0; i < 1; i++) {
            tabu = -1;
            int padre1 = torneoBinario(y, poblacion, matriz);
            System.out.println("Gana el padre1 " + padre1);
            int padre2 = torneoBinario(y, poblacion, matriz);
            System.out.println("Gana el padre2 " + padre2);

            if (rand.nextDouble() < 0.70) {
                System.out.println("Cruzo a los padres");
                cruceF(i, padre1, padre2, x, y, matriz, cubreOrdenado);
                //mutacionAGG(matriz, x, y, cubreOrdenado);
                //AQUI ARREGLO LA SOLUCION
                if (i == muta) {
                    mutacionAGG(i, y); //COMPROBAR SI REALMENTE SE MODIFICA
                }
                arreglaSol(x, y, matriz, cubreOrdenado, descendencia.get(0));
                //AQUI ELIMINO LAS REDUNDANCIAS
                eliminaRedundancias(y, x, descendencia.get(0), cubreOrdenado, matriz);
                //AQUI CALCULO SU COSTE Y LO METO EN COSTESAUX
                System.out.println("El puto hijo tiene coste "+calculaSolucion(y,descendencia.get(0),matriz));
                esSolucionPrint(x, y, matriz, descendencia.get(0));
            } else {
                if (i == muta) {
                    mutacionAGG(i, y); //COMPROBAR SI REALMENTE SE MODIFICA
                    arreglaSol(x, y, matriz, cubreOrdenado, descendencia.get(i));
                    eliminaRedundancias(y, x, descendencia.get(i), cubreOrdenado, matriz);
                    calculaSolucion(y,descendencia.get(i),matriz);
                } else {
                    System.out.println("Selecciono uno de los padres");
                    seleccionaPadre(i, padre1, padre2);
                    //AQUI METO EL COSTE EN COSTEAUX
                }
            }

            //AQUI BUSCO EL MEJOR DE LA POBLACION
            //AQUI BUSCO EL PEOR DE LOS DESCENDIENTES
            //AQUI LOS INTERCAMBIO
            System.out.println("\n");
        }
        //
    }

    public void arreglaSol(int x, int y, int matriz[][], Pair cubreOrdenado[], int sol[]) {
        while (true) {
            int cubiertos[] = new int[x];
            for (int i = 1; i < x; i++) {
                cubiertos[i] = 0;
            }
            for (int c = 1; c < y; c++) {
                if (sol[c] == 1) {
                    for (int f = 1; f < x; f++) {
                        if (matriz[f][c] == 1) {
                            cubiertos[f] = 1;
                        }
                    }
                }
            }
            System.out.println(Arrays.toString(cubiertos));
            int cubre[] = new int[y];
            for (int i = 0; i < y; i++) {
                cubre[i] = 0;
            }
            for (int f = 1; f < x; f++) {
                if (cubiertos[f] == 0) {
                    for (int c = 1; c < y; c++) {
                        if (matriz[f][c] == 1) {
                            ++cubre[c];
                        }
                    }
                }
            }
            System.out.println(Arrays.toString(cubre));
            float ratio[] = new float[y];
            float mayor = (float) cubre[1] / matriz[0][1];
            int pos = 1;
            for (int i = 2; i < y; i++) {
                if (((float) cubre[i] / matriz[0][i]) > mayor) {
                    mayor = (float) cubre[i] / matriz[0][i];
                    pos = i;
                }
            }
            if (mayor == 0) {
                return;
            }
            System.out.println("El mejor tiene ratio " + mayor + " en la pos " + pos);
            sol[pos] = 1;
            System.out.println(Arrays.toString(sol));
        }
    }

    public void mainGenetico(int x, int y, int matriz[][], int cubre[], Pair cubreOrdenado[]) {
        poblacion = new ArrayList<>();
        costes = new int[tamPoblacion];
        Random rand = new Random();
        generarPoblacion(x, y, tamPoblacion, cubreOrdenado, matriz);
        for (int i = 0; i < tamPoblacion; i++) {
            System.out.println(i + ": " + costes[i]);
        }
        /////////////////////////////////////////////////////////////
        descendencia = new ArrayList<>();
        costesAux = new int[tamPoblacion];
        for (int i = 0; i < tamPoblacion; i++) {
            tabu = -1;
            int padre1 = torneoBinario(y, poblacion, matriz);
            System.out.println("Gana el padre1 " + padre1);
            int padre2 = torneoBinario(y, poblacion, matriz);
            System.out.println("Gana el padre2 " + padre2);

            if (rand.nextDouble() < 0.70) {
                System.out.println("Cruzo a los padres");
                cruceF(i, padre1, padre2, x, y, matriz, cubreOrdenado);
                mutacionAGG(matriz, x, y, cubreOrdenado);
                //AQUI ARREGLO LA SOLUCION
                //AQUI ELIMINO LAS REDUNDANCIAS
                //AQUI CALCULO SU COSTE Y LO METO EN COSTESAUX
            } else {
                System.out.println("Selecciono uno de los padres");
                seleccionaPadre(i, padre1, padre2);
                //AQUI METO EL COSTE EN COSTEAUX
            }

            System.out.println("\n");
        }
    }

    public void mutacionAGG(int pos, int y) {
        Random rand = new Random();
        float prob;
        for (int i = 1; i < y; ++i) {
            prob = (float) (Math.abs(rand.nextInt() % 101)) / 100;
            if (prob < probGen) {
                descendencia.get(pos)[i] = (descendencia.get(pos)[i] == 1) ? 0 : 1;
            }      
        }
    }

    public void cruceF(int z, int padre1, int padre2, int x, int y, int matriz[][], Pair cubreOrdenado[]) { //MAS ADELANTE ARREGLARE LA SOLUCION Y QUITARE LAS REDUNDANCIAS
        int hijo[] = new int[y];
        Random rnd = new Random();
        float aleatorio;
        int costePapi1 = costes[padre1];
        int costePapi2 = costes[padre2];
        float p = (float) costePapi2 / (costePapi1 + costePapi2);
        int copia1[] = poblacion.get(padre1), copia2[] = poblacion.get(padre2);
        for (int i = 1; i < y; i++) {
            if (copia1[i] == copia2[i]) {   //Caso de ser iguales
                hijo[i] = copia1[i];
            } else {                        //Caso contrario
                aleatorio = (float) (Math.abs(rnd.nextInt() % 101)) / 100;
                if (aleatorio <= p) {
                    hijo[i] = copia1[i];
                } else {
                    hijo[i] = copia1[2];
                }
            }
        }
        System.out.println(Arrays.toString(copia1));
        System.out.println(Arrays.toString(copia2));
        System.out.println(Arrays.toString(hijo));
        descendencia.add(hijo);
        //arreglaSolucion(matriz, i, x, y, cubreOrdenado);
        //eliminaRedundancias(x, y, descendencia.get(i), cubreOrdenado, matriz);
    }

    public int[] operadorFusion(int y, int vpadre1[], int vpadre2[], int mat[][], int padre1, int padre2) {
        int hijo[] = new int[y];
        Random rnd = new Random();
        float aleatorio;
        int costePapi1 = costes[padre1];
        int costePapi2 = costes[padre2];
        float p = (float) costePapi2 / (costePapi1 + costePapi2);

        for (int i = 1; i < y; i++) {
            if (vpadre1[i] == vpadre2[i]) {   //Caso de ser iguales
                hijo[i] = vpadre1[i];
            } else {                      //Caso contrario
                aleatorio = (float) (Math.abs(rnd.nextInt() % 101)) / 100;
                //System.out.println("Aleatorio: "+aleatorio);
                if (aleatorio <= p) {
                    hijo[i] = vpadre1[i];
                } else {
                    hijo[i] = vpadre2[i];
                }
            }
        }
        return hijo;
    }

    public void seleccionaPadre(int i, int padre1, int padre2) {
        if (costes[padre1] < costes[padre2]) {
            System.out.println("He escogido el padre 1");
            descendencia.add(poblacion.get(padre1));
            costesAux[i] = costes[padre1];  //PUEDE SOBRAR A LA LARGA
        } else {
            System.out.println("He escogido el padre 1");
            descendencia.add(poblacion.get(padre2));
            costesAux[i] = costes[padre2];  //PUEDE SOBRAR A LA LARGA
        }
    }

    public int torneoBinario(int y, ArrayList<int[]> poblacion, int mat[][]) {
        Random rnd = new Random();
        int n1 = Math.abs(rnd.nextInt() % poblacion.size());
        System.out.println("Pposible padre 1 :" + n1 + "\t con coste " + costes[n1]);
        int n2 = Math.abs(rnd.nextInt() % poblacion.size());
        System.out.println("Pposible padre 2 :" + n2 + "\t con coste " + costes[n2]);
        while (n1 == n2 || n1 == tabu || n2 == tabu) {
            n1 = Math.abs(rnd.nextInt() % poblacion.size());
            n2 = Math.abs(rnd.nextInt() % poblacion.size());
        }
        int n = torneoCoste(y, costes[n1], costes[n2], mat);
        tabu = n;
        return (n == 1) ? (n1) : (n2);
    }

    public int torneoCoste(int y, int coste1, int coste2, int mat[][]) {
        return (coste1 <= coste2) ? (1) : (2);
    }

    public void generarPoblacion(int x, int y, int nPoblacion, Pair cubreOrdenado[], int matriz[][]) {
        for (int i = 0; i < nPoblacion; ++i) {
            poblacion.add(generarCromosoma(x, y, cubreOrdenado, matriz, i));
        }
    }

    public int[] generarCromosoma(int x, int y, Pair cubreOrdenado[], int matriz[][], int num) {
        int cromo[] = new int[y];
        int coste = 0;
        Random rnd = new Random();
        int nR, n;
        for (int i = 1; i < y; i++) {
            cromo[i] = 0;
        }
        ArrayList<Integer> array = new ArrayList<>(); //DEBERIA SACAR ESTO FUERA
        for (int i = 1; i < y; i++) {
            array.add(i);
        }
        do {
            nR = Math.abs(rnd.nextInt() % array.size());
            n = array.remove(nR);
            ++cromo[n];
            coste += matriz[0][n];
        } while (!esSolucion(x, y, matriz, cromo));
        eliminaRedundancias(y, x, cromo, cubreOrdenado, matriz);

        costes[num] = coste;
        return cromo;
    }

    public boolean esSolucion(int x, int y, int matriz[][], int solucion[]) {
        boolean ok;
        for (int i = 1; i < x; i++) {
            ok = false;
            for (int j = 1; j < y; j++) {
                if (solucion[j] == 1) {
                    if (matriz[i][j] == 1) {
                        j = y;
                        ok = true;
                    }
                }
            }
            if (!ok) {
                return false;
            }
        }
        return true;
    }

    public void eliminaRedundancias(int x, int y, int solucion[], Pair cubreOrdenado[], int matriz[][]) {
        int quito;
        int i;
        boolean columnaRedundante, sustituible;
        for (int z = 0; z < x - 1; z++) {
            if (solucion[cubreOrdenado[z].getLugar()] == 1) {
                columnaRedundante = true;
                quito = cubreOrdenado[z].getLugar();
                sustituible = false;
                for (i = 1; i < y; i++) {
                    if (matriz[i][quito] == 1) {
                        sustituible = false;
                        for (int j = 1; j < x; j++) {
                            if (matriz[i][j] == 1 && solucion[j] == 1 && quito != j) {
                                sustituible = true;
                            }
                        }
                        if (!sustituible) {
                            columnaRedundante = false;
                        }
                    }
                }
                if (columnaRedundante) {
                    //System.out.println("REDUNDANTEE");
                    solucion[quito] = 0;
                }
            }
        }
    }

    public boolean esSolucionPrint(int x, int y, int matriz[][], int solucion[]) {
        boolean ok;
        for (int i = 1; i < x; i++) {
            ok = false;
            for (int j = 1; j < y; j++) {
                if (solucion[j] == 1) {
                    if (matriz[i][j] == 1) {
                        j = y;
                        ok = true;
                    }
                }
            }
            if (!ok) {
                System.out.println("-----------------------> NO ES SOLUCION <-------------------");
                return false;
            }
        }
        System.out.println("-----------------------> si es solucion <-------------------");
        return true;
    }

    public int calculaSolucion(int y, int solucion[], int mat[][]) {
        int coste = 0;
        for (int i = 1; i < y; i++) {
            if (solucion[i] == 1) {
                coste += mat[0][i];
            }
        }
        return coste;
    }
}
