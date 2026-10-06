import java.util.*;

public class CarroCompraGenerics<T extends Producto> implements Iterable<T> {


    private final HashSet<T> carrito = new HashSet<>();
    public Set<T> getCarrito() {
        return Collections.unmodifiableSet(carrito);
    }

    @Override
    public Iterator<T> iterator() {
        return new CarritoIterador<>(carrito);
    }


    public boolean agregarCarrito(T p) {
        return carrito.add(p);
    }

    public T productoMasCaro() {
        if (carrito.isEmpty()) {
            return null;}
        T pMasCaro = null;
        for (T p : carrito) {
            if (pMasCaro == null || p.getPrecio() > pMasCaro.getPrecio()) {
                pMasCaro = p;}
        }
        return pMasCaro;
    }

    public double calcularTotal() {
        double totalCarrito = 0.0;
        Iterator<T> carritoIterable = this.iterator();
        while (carritoIterable.hasNext()) {
            totalCarrito += carritoIterable.next().getPrecio();}
        return totalCarrito;
    }

    public static void main(String[] args) {
        CarroCompraGenerics<Producto> carrito = new CarroCompraGenerics<>();

        carrito.agregarCarrito(new Producto("Teclado", 80000));
        carrito.agregarCarrito(new Producto("Mouse", 35000));
        carrito.agregarCarrito(new Producto("Monitor", 650000));
        carrito.agregarCarrito(new Producto("usb", 30000));
        carrito.agregarCarrito(new Producto("play", 2000000));
        carrito.agregarCarrito(new Producto("xbox", 1180000));

        System.out.println("Total del carrito:       " + carrito.calcularTotal());
        System.out.println("Producto mas caro:      " + carrito.productoMasCaro());

        System.out.println("\nRecorrido de todos los productos:");
        for (Producto p : carrito) {
            System.out.println("  " + p);}

        System.out.println("\nRecorrido en posiciones impares (1a, 3a, 5a, 7a):");
        Iterator<Producto> impares = new CarritoIteradorImpares<>(carrito.getCarrito());
        while (impares.hasNext()) {
            System.out.println("  " + impares.next());}
    }
}

// tarea del iterador generico, hice otra clase pa el q tocaba inventar

class CarritoIterador<T> implements Iterator<T> {

    private final List<T> productos;
    private int i = 0;

    public CarritoIterador(Collection<T> carrito) {
        this.productos = new ArrayList<>(carrito);}

    @Override
    public boolean hasNext() {
        return i < productos.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {throw new NoSuchElementException("no hay mas productos");}
        T actual = productos.get(i);
        i++;
        return actual;}
}


class CarritoIteradorImpares<T> implements Iterator<T> {

    private final List<T> productos;
    private int i = 0;

    public CarritoIteradorImpares(Collection<T> carrito) {
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


class Producto {

    private final String nombre;
    private final double precio;

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public boolean equals(Object otro) {
        if (!(otro instanceof Producto)) {
            return false;
        }

        Producto p = (Producto) otro;
        return Objects.equals(nombre, p.nombre)
                && Double.compare(precio, p.precio) == 0;

    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, precio);
    }

    @Override
    public String toString() {
        return nombre + " ($" + precio + ")";
    }
}