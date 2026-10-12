package parcial.comparadores;

import parcial.modelo.Paquete;

import java.util.Comparator;

public class ComparadorPeso implements Comparator<Paquete> {

    @Override
    public int compare(Paquete a, Paquete b) {
        return Double.compare(b.getPeso(), a.getPeso());
    }
}
