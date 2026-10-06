package genericos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Variante INVENTADA: recorre solo las posiciones impares (1a, 3a, 5a, 7a...).
 *
 * Comparalo con IteradorCarrito: 3 de cada 4 lineas son identicas.
 * Lo unico que cambia es el paso del indice: i++ paso 1, i += 2 paso 2.
 *
 * Por que el for-each no sirve aca: el for-each del compilador esta cableado
 * a hasNext() y next(). El no puede "saber" que este next() avanza de a dos.
 * Si quiseras usar for-each, habria que meter un Set con solo las posiciones
 * impares (pre-filtrado), que es otra solucion distinta.
 *
 * PENDIENTE (mismo esqueleto, cambiar solo i):
 *   - IteradorCarritoAtras        -> arrancar en size()-1 y i--
 *   - IteradorCarritoDeDosEnDos   -> arrancar en 0 y i += 2 (este mismo, renombrado)
 */
public class IteradorCarritoImpares<T> implements Iterator<T> {

    private final List<T> productos;
    private int i = 0;

    public IteradorCarritoImpares(Collection<T> carrito) {
        this.productos = new ArrayList<>(carrito);
    }

    @Override
    public boolean hasNext() {
        return i < productos.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("no hay mas posiciones impares");
        }
        T actual = productos.get(i);
        i += 2;
        return actual;
    }
}
