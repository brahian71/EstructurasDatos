package colecciones;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * TEMA 2 - Colecciones: EL ITERADOR COMO PATRON SEGURO PARA ELIMINAR.
 *
 * Este archivo tiene los DOS metodos a proposito, porque juntos explican el
 * error mas comun de la materia:
 *
 *   eliminarA()      -> la version que FALLA (queda comentada en el main)
 *   iteradorRemoveA()-> la version que funciona
 *
 * ----------------------------------------------------------------------------
 * POR QUE eliminarA() FALLA
 *
 *   for (int i = 0; i < lista.size(); i++) {
 *       if (condicion) lista.remove(i);
 *   }
 *
 * Al remover en i, todos los elementos de la derecha se CORREN una posicion
 * hacia la izquierda. El for despues hace i++, asi que salta ese elemento
 * que se acabo de mover. No es que se pierda memoria ni que la lista este
 * dañada: simplemente jamas se revisa el elemento que quedo en i.
 *
 * Pista para verlo en una traza: comentariosa el i++ y la lista se borra bien.
 * Ese es el experimento.
 *
 * ----------------------------------------------------------------------------
 * LA REGLA
 *
 *   Nunca modificar la estructura de la coleccion con el forEach/indice
 *   mientras se recorre con el forEach/indice.
 *   Para eliminar: Iterator + Iterator.remove().
 *
 * El Iterator es el unico recorrido que sabe en que posicion interna esta y
 * por eso puede compactar la estructura sin saltarse nada. El forEach de Java
 * delega en un Iterator, asi que tecnicamente for + it.remove() tambien
 * funciona. Con el metodo remove() DE LA LISTA, no.
 */
public class IteratorSeguro {

    /** VERSION QUE FALLA. No usar: se salta elementos. */
    public static void eliminarA(ArrayList<String> lista) {

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).toLowerCase().startsWith("a")) {
                lista.remove(i);
                // ACA ESTA EL BUG: remove(i) corrio todo a la izquierda y el
                // i++ del for pisa el elemento que quedo en i.
            }
        }
    }

    /** VERSION CORRECTA: el Iterator Compacta la lista sin saltarse nada. */
    public static void iteradorRemoveA(ArrayList<String> lista) {

        Iterator<String> listaIterable = lista.iterator();

        while (listaIterable.hasNext()) {
            if (listaIterable.next().toLowerCase().startsWith("a")) {
                listaIterable.remove();
            }
        }
    }

    public static void main(String[] args) {

        ArrayList<String> original = new ArrayList<>();
        original.add("luis");
        original.add("alberto");
        original.add("robinson");
        original.add("carlos");
        original.add("andrea");
        original.add("pedro");
        original.add("baca");
        original.add("ana");

        System.out.println("Original:            " + original);

        // Descomentar esta linea para ver el bug en accion:
        // eliminarA(copia(original));
        // Original:            [luis, alberto, robinson, carlos, andrea, pedro, baca, ana]
        // Con eliminarA():     [luis, carlos, pedro, ana]   <-- se comio "andrea"
        // Con iteradorRemoveA:[luis, robinson, carlos, pedro, baca]

        iteradorRemoveA(original);
        System.out.println("Con Iterator.remove: " + original);
    }

    /** Copia util para comparar sin arruinar el original del main. */
    private static ArrayList<String> copia(ArrayList<String> lista) {
        return new ArrayList<>(lista);
    }
}
