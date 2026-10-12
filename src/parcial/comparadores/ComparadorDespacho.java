package parcial.comparadores;

import parcial.modelo.Paquete;

import java.util.Comparator;

public class ComparadorDespacho implements Comparator<Paquete> {

    private final Comparator<Paquete> regla = new ComparadorPrioridad()
            .thenComparing(new ComparadorTiempo())
            .thenComparing(Comparator.naturalOrder());

    @Override
    public int compare(Paquete a, Paquete b) {
        return regla.compare(a, b);
    }
}
