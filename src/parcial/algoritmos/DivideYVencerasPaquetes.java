package parcial.algoritmos;

import parcial.modelo.Paquete;

import java.util.List;

public final class DivideYVencerasPaquetes {

    private DivideYVencerasPaquetes() {
    }
    public static Paquete mayorPeso(List<Paquete> paquetes) {
        if (paquetes == null || paquetes.isEmpty()) {
            throw new IllegalArgumentException("No hay paquetes para comparar");
        }
        return mayorPeso(paquetes, 0, paquetes.size() - 1);
    }
    private static Paquete mayorPeso(List<Paquete> paquetes, int inicio, int fin) {
        if (inicio == fin) {
            return paquetes.get(inicio);
        }
        int medio = inicio + (fin - inicio) / 2;

        Paquete mayorIzquierda = mayorPeso(paquetes, inicio, medio);
        Paquete mayorDerecha = mayorPeso(paquetes, medio + 1, fin);

        return mayorIzquierda.getPeso() >= mayorDerecha.getPeso() ? mayorIzquierda : mayorDerecha;
    }
}
