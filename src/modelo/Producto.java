package modelo;

import java.util.Objects;

/**
 * Entidad de dominio. Vive fuera de `genericos` a proposito: no es un tema,
 * es el objeto que los temas manipulan.
 *
 * NOTA IMPORTANTE (tema 3 - Sets):
 * Esta clase define DOS reglas de igualdad distintas y a proposito distintas:
 *
 *   1. equals() + hashCode()  -> las usa HashSet / LinkedHashSet
 *      "Son el mismo producto si tiene el mismo nombre Y el mismo precio."
 *
 *   2. compareTo()            -> lo usa TreeSet
 *      "El orden entre productos lo define el precio."
 *
 * Son mecanismo INDEPENDIENTES. Si sobreescribo equals/hashCode y NO compareTo,
 * un TreeSet de Producto no compila. Y al reves: un HashSet NO ordena por precio.
 */
public class Producto implements Comparable<Producto> {

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

    // ---------- Regla 1: identidad para HashSet / LinkedHashSet ----------

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Producto)) {
            return false;
        }

        Producto p = (Producto) otro;
        return Objects.equals(nombre, p.nombre)
                && Double.compare(precio, p.precio) == 0;
    }

    @Override
    public int hashCode() {
        // Contrato: si equals() es true, hashCode() DEBE ser el mismo.
        // Si cambio equals() y no cambio hashCode(), el HashSet se rompe en silencio.
        return Objects.hash(nombre, precio);
    }

    // ---------- Regla 2: orden natural para TreeSet ----------

    @Override
    public int compareTo(Producto otro) {
        return Double.compare(this.precio, otro.precio);
    }

    @Override
    public String toString() {
        return nombre + " ($" + precio + ")";
    }
}
