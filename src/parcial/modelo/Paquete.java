package parcial.modelo;

import java.util.Objects;

/**
 * Paquete registrado en una jornada de QuindioExpress.

 *   1. equals() + hashCode() -> identidad. Dos paquetes son el mismo si
 *      tienen el mismo codigo. La usan HashMap/HashSet y ListaEnlazada.eliminar.
 *   2. compareTo() -> ORDEN NATURAL, por codigo ascendente.
 *      Los otros criterios pa no cambiar este orden

 */
public class Paquete implements Comparable<Paquete> {

    public static final int PRIORIDAD_MINIMA = 1;
    public static final int PRIORIDAD_MAXIMA = 5;
    public static final int PRIORIDAD_POR_DEFECTO = 3;

    private final String codigo;
    private final String destino;
    private final double peso;
    private final int prioridad;
    private final int tiempoEstimado;

    public Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El codigo no puede ser nulo ni vacio");
        }
        if (destino == null || destino.isBlank()) {
            throw new IllegalArgumentException("El destino no puede ser nulo ni vacio");
        }
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero: " + peso);
        }
        if (prioridad < PRIORIDAD_MINIMA || prioridad > PRIORIDAD_MAXIMA) {
            throw new IllegalArgumentException("La prioridad debe estar entre "
                    + PRIORIDAD_MINIMA + " y " + PRIORIDAD_MAXIMA + ": " + prioridad);
        }
        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor que cero: " + tiempoEstimado);
        }

        this.codigo = codigo.trim();
        this.destino = destino.trim();
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    /** Constructor con la prioridad por defecto. */
    public Paquete(String codigo, String destino, double peso, int tiempoEstimado) {
        this(codigo, destino, peso, PRIORIDAD_POR_DEFECTO, tiempoEstimado);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    @Override
    public int compareTo(Paquete otro) {
        return this.codigo.compareTo(otro.codigo);
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Paquete p)) {
            return false;
        }
        return codigo.equals(p.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return codigo + " [" + destino + ", " + peso + " kg, prioridad " + prioridad
                + ", " + tiempoEstimado + " min]";
    }
}
