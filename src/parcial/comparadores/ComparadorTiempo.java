package parcial.comparadores;

import parcial.modelo.Paquete;

import java.util.Comparator;

public class ComparadorTiempo implements Comparator<Paquete> {

    @Override
    public int compare(Paquete a, Paquete b) {
        return Integer.compare(a.getTiempoEstimado(), b.getTiempoEstimado());
    }
}
