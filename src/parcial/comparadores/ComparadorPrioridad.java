package parcial.comparadores;

import parcial.modelo.Paquete;

import java.util.Comparator;

/** Prioridad de MAYOR a menor. Por eso los argumentos van invertidos (b, a). */
public class ComparadorPrioridad implements Comparator<Paquete> {

    @Override
    public int compare(Paquete a, Paquete b) {
        return Integer.compare(b.getPrioridad(), a.getPrioridad());
    }
}
