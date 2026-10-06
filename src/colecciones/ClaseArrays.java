package colecciones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * TEMA 2 - Colecciones: la clase Arrays.
 *
 * NO hay solucion escrita aca a proposito. Cada metodo tiene la firma y una
 * linea de que hay que demostrar. Los ejercicios son de conteo de
 * comparaciones, no solo de resultado: el objetivo es poder decir
 * "el ultimo se detecta en n/2 comparaciones" en la sustentacion.
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 1 - recorrido y busqueda
 *
 *   Arrays.fill(int[] a, int valor)             -> rellenar todo con un valor
 *   Arrays.toString(int[] a)                    -> "[1, 2, 3]"  (a diferencia de
 *                                                   a.length que daria "[I@1b6d"
 *   Arrays.equals(int[] a, int[] b)             ->comparar CONTENIDO
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 2 - ORDENAR (tema clave: la busqueda binaria exige orden previo)
 *
 *   void ordenarBurbuja(int[] a)
 *   void ordenarSeleccion(int[] a)
 *   void ordenarInsercion(int[] a)
 *   Arrays.sort(int[] a)                        -> la de la libreria
 *
 *   Escribi los 3 a mano y despues contame la diferencia de complejidad:
 *     burbuja   O(n^2) siempre
 *     seleccion O(n^2) siempre
 *     insercion O(n) si ya esta ordenado, O(n^2) en el peor caso
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 3 - BUSQUEDA BINARIA con conteo de comparaciones
 *
 *   int buscarBinaria(int[] a, int valor, int comparaciones)
 *
 *   La idea: en un arreglo ordenado, cada comparacion descarta la MITAD.
 *   Por eso el teorema de la busqueda binaria dice que el elemento va a estar
 *   en las comparaciones 1, 2, 3, ..., y la comparacion k solo tiene sentido
 *   sobre los primeros 2^(k-1) elementos.
 *
 *   Por eso con 3 comparaciones uno encuentra UNO entre 4.
 *   Con 20 comparaciones, uno entre 1.048.576.
 *
 *   Lo que hay que demostrar: el arreglo SIEMPRE esta ordenado antes de
 *   llamar, y el while tiene que cortar (i <= j, no i < j, porque se cruzan).
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 4 - Arrays.asList: la trampa del tamaño fijo
 *
 *   void probarAsList(int[] a)
 *
 *   Arrays.asList(a) devuelve una List backed por el arreglo:
 *   - get(i), set(i), size() funcionan
 *   - add(), remove() LANZAN UnsupportedOperationException
 *
 *   Comprobalo con un try/catch y comentalo.
 *
 *   Lo que si se puede: reconstruir la List real con
 *     new ArrayList<>(Arrays.asList(a))
 *
 * ----------------------------------------------------------------------------
 * PARA VERIFICAR:
 *
 *   ordenar todos:    {5,2,9,1,7} -> {1,2,5,7,9}
 *   buscar 9 en:      {1,2,5,7,9}
 *   buscar 6 en:      {1,2,5,7,9}  -> -1
 */
public class ClaseArrays {

    public static void main(String[] args) {
        System.out.println("TODO: implementar los 4 ejercicios de este archivo");
    }

    public static void ordenarBurbuja(int[] a) {
        throw new UnsupportedOperationException("TODO: burbuja comparando e intercambiando vecinos");
    }

    public static void ordenarSeleccion(int[] a) {
        throw new UnsupportedOperationException("TODO: buscar el menor del tramo y llevarlo a i");
    }

    public static void ordenarInsercion(int[] a) {
        throw new UnsupportedOperationException("TODO: insertar cada elemento en su lugar dentro del tramo ordenado");
    }

    public static int buscarBinaria(int[] a, int valor, int comparaciones) {
        throw new UnsupportedOperationException("TODO: i=0, j=length-1, while (i <= j), mitad=(i+j)/2");
    }

    public static void probarAsList(int[] a) {
        throw new UnsupportedOperationException("TODO: try { Arrays.asList(a).add(99); } catch (UnsupportedOperationException e) { ... }");
    }
}
