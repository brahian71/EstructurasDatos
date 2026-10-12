package parcial.algoritmos;

import parcial.modelo.Paquete;

import java.util.List;


public final class Recursivos {

    private Recursivos() {
    }

    public static double pesoTotalPorMunicipio(List<Paquete> paquetes, String municipio) {
        return pesoTotalPorMunicipio(paquetes, municipio, 0);
    }
    private static double pesoTotalPorMunicipio(List<Paquete> paquetes, String municipio, int indice) {
        if (indice == paquetes.size()) {
            return 0.0;
        }
        Paquete actual = paquetes.get(indice);
        double aporte = actual.getDestino().equals(municipio) ? actual.getPeso() : 0.0;
        return aporte + pesoTotalPorMunicipio(paquetes, municipio, indice + 1);
    }

    public static int contarPrioridadMinima(List<Paquete> paquetes, int prioridadMinima) {
        return contarPrioridadMinima(paquetes, prioridadMinima, 0);
    }
    private static int contarPrioridadMinima(List<Paquete> paquetes, int prioridadMinima, int indice) {
        if (indice == paquetes.size()) {
            return 0;
        }
        int aporte = paquetes.get(indice).getPrioridad() >= prioridadMinima ? 1 : 0;
        return aporte + contarPrioridadMinima(paquetes, prioridadMinima, indice + 1);
    }

}
