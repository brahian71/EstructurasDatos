package sets;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * TEMA 3 - HashSet vs LinkedHashSet vs TreeSet.
 *
 * Los tres son Set: deduplican igual, usando equals() + hashCode().
 * Lo que cambia es el ORDEN y el COSTO.
 *
 * ----------------------------------------------------------------------------
 * QUE PIDE CADA UNO Y POR QUE
 *
 *   HashSet       SIN orden. Usa una tabla (buckets) indexada por
 *                 hashCode(). Agregar es O(1) promedio. Es el mas rapido
 *                 para solo deduplicar.
 *
 *   LinkedHashSet CON orden de INSERCION. Guarda ademas una lista doblemente
 *                 enlazada de los elementos en el orden en que entraron.
 *                 Sigue siendo O(1) promedio, casi tan rapido como HashSet.
 *
 *   TreeSet        CON orden NATURAL (compareTo) o el que le passes por
 *                 constructor. NO usa hashCode: usa un arbol (red-black).
 *                 Agregar es O(log n), y es el UNICO de los tres que te da
 *                 metodos que inherently necesitan orden:
 *                   first(), last(), floor(), ceiling(), headSet(), subSet()
 *
 * ----------------------------------------------------------------------------
 * LA CONSECUENCIA PRACTICA (esta es la que va a la sustentacion)
 *
 * Como Producto.hashCode() es Objects.hash(nombre, precio), si CAMBIAS EL
 * PRECIO de un producto, su hash cambia, su bucket cambia, y en el HashSet
 * el producto se MUEVE de lugar. El conjunto no cambio de contenido pero su
 * ITERACION cambio.
 *
 * Si tu codigo depende de que "el primero" sea el primero que agregaste,
 * HashSet es el Set equivocado. Para eso es LinkedHashSet.
 *
 * ----------------------------------------------------------------------------
 * EL BUG DE ESTA CLASE ORIGINAL (esta linea estaba mal)
 *
 *   if (iteraList.next() == palabra)   <-- era ESTE
 *   if (iteraList.next().equals(palabra))
 *
 * El == compara REFERENCIAS, no texto. Funcionaba solo porque "robinson" es
 * un literal y Java mete los literales identicos en una tabla compartida
 * (string interning). Si la palabra llega desde un Scanner o un readLine(),
 * es otro objeto, el == da false, y eliminar no borra nada.
 *
 * No hay error visible. El programa corre "bien". Solo falla con otro origen
 * de datos. Ese es el peor tipo de bug.
 *
 * Pista para verlo: probalo con una palabra construida en runtime,
 *   new String("robinson"),  y el == da false mientras que equals da true.
 */
public class TipoHashSet {

    public static void main(String[] args) {

        // Se cargan los TRES con la misma secuencia, para que la comparacion sea justa.
        String[] entrada = {"luis", "alberto", "robinson", "carlos",
                "andrea", "pedro", "baca", "ana", "juice", "ana"};

        Set<String> hash = new HashSet<>();
        Set<String> conInsercion = new LinkedHashSet<>();
        TreeSet<String> alfabetico = new TreeSet<>();

        for (String s : entrada) {
            agregar(hash, s);
            conInsercion.add(s);
            alfabetico.add(s);
        }

        // El ultimo "ana" se ignora en los tres: por eso los tres tienen 8.
        System.out.println("cantidad de elementos: " + hash.size()
                + " (se agregaron " + entrada.length + ")");

        eliminar(hash, "robinson");

        System.out.println("\nHashSet:       " + hash);
        System.out.println("LinkedHashSet: " + conInsercion);
        System.out.println("TreeSet:       " + alfabetico);

        // Notar que el HashSet NO sale ni en orden de insercion ni alfabetico:
        // sale en el orden en que los elementos caen en la tabla de hash.
        // Y que el LinkedHashSet SI respeta el orden de entrada (con "ana"
        // appearing en su posicion original, no al final).

        // Solo el TreeSet tiene metodos que necesitan orden:
        System.out.println("\nSolo el TreeSet tiene metodos que requieren orden:");
        System.out.println("  primero (first):  " + alfabetico.first());
        System.out.println("  ultimo  (last):   " + alfabetico.last());

        // ------------------------------------------------------------------------
        // ORDEN DEL HASH NO ES FIJO NI ENTRE EJECUCIONES
        //
        // HashSet no garantiza orden. En general el mismo codigo da el mismo
        // orden, pero eso no es un contrato de Java: si el String tiene el
        // flag de hash deshabilitado, o si cambia la version de Java, el
        // orden puede cambiar sin que cambie una sola linea de codigo.
        //
        // Con los Producto del tema 4 el problema es peor, porque alli el
        // hashCode depende del PRECIO. Cambiar el precio de un producto
        // cambia su hash y mueve su posicion en el recorrido.
        //
        // Por eso la regla es: si el orden importa, usar LinkedHashSet.
        // ------------------------------------------------------------------------

        // ------------------------------------------------------------------------
        // COMPARAR == CON .equals()  (descomentar para verlo)
        //
        // String a = "robinson";
        // String b = new String("robinson");
        // System.out.println(a == b);       // false  -> referencias distintas
        // System.out.println(a.equals(b));  // true   -> mismo texto
        //
        // Con literales el == SI da true, por el interning. Por eso el bug
        // se oculta hasta que el dato viene de otro lado.
        // ------------------------------------------------------------------------

        // DEMOSTRACION DE QUE EL BUG ES REAL:
        Set<String> prueba = new HashSet<>();
        prueba.add("robinson");
        System.out.println("\neliminar con == falla: " + (eliminarConIgualIgual(prueba, new String("robinson"))));
        System.out.println("eliminar con equals:  " + (eliminarConEquals(prueba, "robinson")));
    }

    public static void agregar(Set<String> lista, String palabra) {
        lista.add(palabra);
    }

    /** Version CORRECTA. */
    public static void eliminar(Set<String> lista, String palabra) {

        Iterator<String> iteraList = lista.iterator();

        while (iteraList.hasNext()) {
            if (iteraList.next().equals(palabra)) {
                iteraList.remove();
            }
        }
    }

    /** Version con el bug, para demostrar que falla. */
    public static boolean eliminarConIgualIgual(Set<String> lista, String palabra) {

        Iterator<String> iteraList = lista.iterator();
        boolean borro = false;

        while (iteraList.hasNext()) {
            if (iteraList.next() == palabra) {
                iteraList.remove();
                borro = true;
            }
        }
        return borro;
    }

    /** Version correcta, pero devuelve si borro (util para el main). */
    public static boolean eliminarConEquals(Set<String> lista, String palabra) {

        Iterator<String> iteraList = lista.iterator();
        boolean borro = false;

        while (iteraList.hasNext()) {
            if (iteraList.next().equals(palabra)) {
                iteraList.remove();
                borro = true;
            }
        }
        return borro;
    }
}
