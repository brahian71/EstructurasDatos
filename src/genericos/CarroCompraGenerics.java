package genericos;

import modelo.Producto;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Carrito de compras generico (tema 4 - genericos).
 *
 * <T extends Producto> es un "bounded type parameter": T puede ser cualquier
 * subclase de Producto, pero nunca un Integer ni un String. El compilador lo
 * garantiza en tiempo de compilacion, entonces p.getPrecio() compila sin cast.
 *
 * implements Iterable<T> es lo que habilita el for-each. El for-each del
 * compilador NO pide un iterador con metodos raros: pide exactamente un
 * Iterator<T>, o sea hasNext() y next(). Por eso el for-each SI funciona aca
 * pero NO con IteradorCarritoImpares.
 */
public class CarroCompraGenerics<T extends Producto> implements Iterable<T> {

    private final HashSet<T> carrito = new HashSet<>();

    public Set<T> getCarrito() {
        return Collections.unmodifiableSet(carrito);
    }

    @Override
    public Iterator<T> iterator() {
        return new IteradorCarrito<>(carrito);
    }

    public boolean agregarCarrito(T p) {
        return carrito.add(p);
    }

    public T productoMasCaro() {
        if (carrito.isEmpty()) {
            return null;
        }
        T pMasCaro = null;
        for (T p : carrito) {
            if (pMasCaro == null || p.getPrecio() > pMasCaro.getPrecio()) {
                pMasCaro = p;
            }
        }
        return pMasCaro;
    }

    /**
     * NOTA: recorre el HashSet a mano con el Iterator propio, para no depender
     * del for-each y mostrar el contrato hasNext()/next() a mano.
     */
    public double calcularTotal() {
        double totalCarrito = 0.0;
        Iterator<T> carritoIterable = this.iterator();
        while (carritoIterable.hasNext()) {
            totalCarrito += carritoIterable.next().getPrecio();
        }
        return totalCarrito;
    }

    public static void main(String[] args) {

        Producto[] catalogo = {
                new Producto("Teclado", 80000),
                new Producto("Mouse", 35000),
                new Producto("Monitor", 650000),
                new Producto("usb", 30000),
                new Producto("play", 2000000),
                new Producto("xbox", 1180000),
        };

        CarroCompraGenerics<Producto> carrito = new CarroCompraGenerics<>();
        LinkedHashSet<Producto> enOrden = new LinkedHashSet<>();

        for (Producto p : catalogo) {
            carrito.agregarCarrito(p);
            enOrden.add(p);
        }

        System.out.println("Total del carrito:  " + carrito.calcularTotal());
        System.out.println("Producto mas caro: " + carrito.productoMasCaro());

        System.out.println("\n-- for-each sobre el carrito (el compilador invoca mi iterator()) --");
        for (Producto p : carrito) {
            System.out.println("  " + p);
        }

        // ---------------------------------------------------------------
        // ORDEN: los mismos 6 productos en un HashSet y en un LinkedHashSet.
        //
        // Los dos deduplican igual (mismo equals/hashCode). Lo que cambia es
        // el ORDEN: HashSet ordena por donde cae el hashCode de cada elemento
        // en la tabla, LinkedHashSet respeta el orden de insercion.
        //
        // Consecuencia practica: como Producto.hashCode() es
        // Objects.hash(nombre, precio), CAMBIAR EL PRECIO de un producto
        // cambia su hash y mueve su posicion. Si el precio cambia, este
        // recorrido cambia. Eso NO es un bug, es lo que significa "no
        // guarantee orden".
        //
        // Por eso la demo de "posiciones impares" va sobre el LinkedHashSet:
        // sobre un HashSet la palabra "posicion" no tiene significado.
        // ---------------------------------------------------------------

        System.out.println("\n-- HashSet: orden del hash, no garantizado --");
        for (Producto p : carrito) {
            System.out.println("  " + p);
        }

        System.out.println("\n-- LinkedHashSet: orden de insercion --");
        for (Producto p : enOrden) {
            System.out.println("  " + p);
        }

        System.out.println("\n-- Posiciones impares (1a, 3a, 5a, 7a) sobre el LinkedHashSet --");
        Iterator<Producto> impares = new IteradorCarritoImpares<>(enOrden);
        while (impares.hasNext()) {
            System.out.println("  " + impares.next());
        }

        System.out.println("\n(ese iterador NO se puede usar con for-each: el for-each solo");
        System.out.println(" conoce el contrato hasNext()/next(), no un next() que salta de a 2)");
    }
}
