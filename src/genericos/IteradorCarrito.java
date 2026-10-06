package genericos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterador hecho desde cero (tema 4 - genericos).
 *
 * Por que NO alcanza con el Iterator de la libreria: el de la libreria solo
 * sabe avanzar de a uno. Este es el esqueleto base; las variantes inventadas
 * (Atras, DeDosEnDos, Impares) son el MISMO esqueleto y solo cambian
 * el punto de partida y el paso del indice. Ver IteradorCarritoImpares.
 *
 * Regla de next(): validar -> sacar -> avanzar -> devolver.
 * Ese orden importa. Si se avanza antes de guardar el valor, se pierde un elemento.
 */
public class IteradorCarrito<T> implements Iterator<T> {

    private final List<T> productos;
    private int i = 0;

    /**
     * Copia la coleccion a un ArrayList.
     *
     * Ojo: el iterador esta definido sobre un ArrayList, no sobre el Set.
     * Por eso funcionan el "salto de a 2" o "hacia atras", que no tienen sentido
     * sobre un HashSet (HashSet no tiene posiciones, solo orden de recorrido).
     *
     * La alternativa de la clase es (T[]) coleccion.toArray(), que obliga a un
     * cast por type erasure: el compilador ya borro el <T>, entonces no sabe
     * que tipo de arreglo crear y hay que responderle vos. new ArrayList<>(...)
     * evita el cast y es lo correcto aca; el toArray queda como ejercicio.
     */
    public IteradorCarrito(Collection<T> carrito) {
        this.productos = new ArrayList<>(carrito);
    }

    @Override
    public boolean hasNext() {
        return i < productos.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("no hay mas productos");
        }
        T actual = productos.get(i);
        i++;
        return actual;
    }
}
