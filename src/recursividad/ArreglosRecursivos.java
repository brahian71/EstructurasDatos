package recursividad;

/**
 * PENDIENTE - Recursion sobre arreglos (lo que falta del tema 1).
 *
 * NO hay solucion escrita aca a proposito. Cada metodo tiene la firma lista
 * y tira UnsupportedOperationException. Escribi el cuerpo vos.
 *
 * Son 3 ejercicios distintos y la trampa es la misma en los tres:
 * la recursion que MODIFICA el arreglo no puede ignorar que el return no
 * devuelve una copia nueva, sino la MISMA referencia.
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 1 - invertir el arreglo en el mismo arreglo (in place)
 *
 *   void invertir(int[] a, int i, int j)
 *
 *   Idea: i arranca en 0 y j en a.length-1. Cada paso intercambia a[i] con a[j]
 *   y los dos indices se mueven hacia el medio. El caso base es i >= j.
 *   El metodo devuelve void porque no hay nada que devolver: el arreglo original
 *   YA quedo modificado. Ese es el punto.
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 2 - buscar un valor en un arreglo (devuelve el indice, -1 si no esta)
 *
 *   int buscar(int[] a, int valor, int i)
 *
 *   Idea: si i == a.length, no lo encontre -> -1. Si a[i] == valor -> i.
 *   Si no, sigo con i + 1.
 *   OJO: comparar con == solo funciona si el arreglo es de primitivos.
 *   Con String[] hay que usar .equals() (mismo tema que TipoHashSet).
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 3 - el arreglo esta ordenado de menor a mayor?
 *
 *   boolean estaOrdenado(int[] a, int i)
 *
 *   Idea: si i == a.length - 1, llego al final sin romper nada -> true.
 *   Si a[i] > a[i+1], esta desordenado -> false. Si no, sigo con i + 1.
 *
 * ----------------------------------------------------------------------------
 * PARA VERIFICAR: probalos con estos casos.
 *
 *   invertir:
 *     {1,2,3,4,5}     -> {5,4,3,2,1}
 *     {1,2,3,4}       -> {4,3,2,1}   (longitud par: no hay elemento central fijo)
 *     {7}             -> {7}         (un solo elemento)
 *
 *   buscar:
 *     buscar({4,8,15,16,23}, 15) -> 2
 *     buscar({4,8,15,16,23}, 99) -> -1
 *
 *   estaOrdenado:
 *     {1,2,2,5}  -> true
 *     {1,4,2,5}  -> false
 */
public class ArreglosRecursivos {

    public static void main(String[] args) {
        System.out.println("TODO: implementar los 3 ejercicios de este archivo");
    }

    /** EJERCICIO 1 */
    public static void invertir(int[] a, int i, int j) {
        throw new UnsupportedOperationException("TODO: intercambiar a[i] con a[j], mover i++ y j--, base i >= j");
    }

    /** EJERCICIO 2 */
    public static int buscar(int[] a, int valor, int i) {
        throw new UnsupportedOperationException("TODO: base i == a.length -> -1; si a[i] == valor -> i; si no i + 1");
    }

    /** EJERCICIO 3 */
    public static boolean estaOrdenado(int[] a, int i) {
        throw new UnsupportedOperationException("TODO: base i == a.length - 1 -> true; si a[i] > a[i+1] -> false; si no i + 1");
    }
}
