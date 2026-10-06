package colecciones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * TEMA 2 - Colecciones: la clase Collections.
 *
 * NO hay solucion escrita aca a proposito.
 *
 * A diferencia de Arrays (que trabaja sobre arreglos), Collections trabaja
 * sobre cualquier Coleccion. Son metodos de UTILIDAD: no hacen estado,
 * solo reciben y devuelven.
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 1 - orden y busqueda
 *
 *   Collections.sort(List<T>)
 *   Collections.binarySearch(List<T>, T)          -> por eso EXIGE orden previo
 *   Collections.reverse(List<T>)
 *   Collections.shuffle(List<T>, Random)          -> barajar, seed para tests
 *   Collections.max(List<T>, Comparator)          -> por eso pide un Comparator
 *   Collections.min(List<T>, Comparator)
 *
 *   El Comparator es una interfaz funcional (un solo metodo abstracto:
 *   compare(a, b)) asi que se puede pasar con una lambda:
 *     Collections.max(lista, (a, b) -> a.getPrecio() - b.getPrecio())
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 2 - consulto sobre la coleccion
 *
 *   Collections.frequency(List<T>, T)  -> cuantas veces aparece X
 *   Collections.disjoint(Coleccion a, Coleccion b)
 *                                          -> true si NO tienen NADA en comun
 *   Collections.addAll(Coleccion destino, Coleccion... fuentes)
 *   Collections.unmodifiableList(List)   -> vista de solo lectura
 *
 *   OJO con disjoint: tiene la logica invertida a la que uno espera.
 *   Devuelve true cuando NO hay intersECCION. No hay ningun metodo que
 *   devuelva "tienen algo en comun", hay que negarlo:
 *     boolean hayComun = !Collections.disjoint(a, b);
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 3 - el error clasico de la materia: EMPTY vs NULL vs SIZE
 *
 *   List<String> vacia   = new ArrayList<>();
 *   List<String> nula    = null;
 *
 *   Cuando busquemos con iterador, el caso base NO es "si la lista es null",
 *   es "si el iterador no tieneNext()". El null solo aparece si el METODO
 *   recibio null, que es un problema del contrato del metodo, no del recorrido.
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 4 - POR QUE NO SE PUEDE ENUMERAR UN Map como si fuera una List
 *
 *   Un Map no es una Coleccion (no extiende Collection). No tiene indice ni
 *   posicion. Lo que se puede:
 *
 *     for (Map.Entry<K,V> e : mapa.entrySet()) { e.getKey(); e.getValue(); }
 *     mapa.keySet()  -> Set<K>   (SI se puede recorrer, es una Collection)
 *     mapa.values()  -> Collection<V>
 *
 *   Convertir un Map a List para poder usar sort/binarySearch:
 *     List<Map.Entry<K,V>> entradas = new ArrayList<>(mapa.entrySet());
 *     Collections.sort(entradas, (a,b) -> a.getValue() - b.getValue());
 *
 * ----------------------------------------------------------------------------
 * PARA VERIFICAR:
 *
 *   frequency({a,b,a,c,a}, "a")           -> 3
 *   disjoint({1,2,3}, {4,5,6})            -> true
 *   disjoint({1,2,3}, {3,4})              -> false
 */
public class ClaseCollections {

    public static void main(String[] args) {
        System.out.println("TODO: implementar los 4 ejercicios de este archivo");
    }

    public static int contarVeces(List<String> lista, String valor) {
        throw new UnsupportedOperationException("TODO: Collections.frequency");
    }

    public static boolean hayAlgunoEnComun(List<String> a, List<String> b) {
        throw new UnsupportedOperationException("TODO: es el NEGADO de Collections.disjoint");
    }

    public static Set<String> sinRepetidos(List<String> lista) {
        throw new UnsupportedOperationException("TODO: un HashSet resuelto al vuelo con el constructor que recibe la coleccion");
    }

    public static void probarVaciaVsNula() {
        List<String> vacia = new ArrayList<>();
        List<String> nula = null;

        // Descomentar de a uno para ver el error y el tipo de excepcion:
        // System.out.println(vacia.isEmpty());   // true
        // System.out.println(nula.isEmpty());    // NullPointerException
        // for (String s : vacia) { }            // no itera ninguna vez
        // for (String s : nula) { }             // NullPointerException

        System.out.println(vacia);
        System.out.println("descomentar las lineas de arriba para ver las excepciones");
    }
}
