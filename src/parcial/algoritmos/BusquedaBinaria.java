package parcial.algoritmos;

import parcial.modelo.Paquete;

import java.util.List;


public final class   BusquedaBinaria {

    private BusquedaBinaria() {
    }

    public static int buscarIterativa(List<Paquete> ordenadaPorCodigo, String codigo) {
        int inicio = 0;
        int fin = ordenadaPorCodigo.size() - 1;

        while (inicio <= fin) {
            // pa desbordamiento con listas enormes
            int medio = inicio + (fin - inicio) / 2;
            int comparacion = ordenadaPorCodigo.get(medio).getCodigo().compareTo(codigo);

            if (comparacion == 0) {
                return medio;
            } else if (comparacion < 0) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return -1;
    }


    public static int buscarRecursiva(List<Paquete> ordenadaPorCodigo, String codigo) {
        return buscarRecursiva(ordenadaPorCodigo, codigo, 0, ordenadaPorCodigo.size() - 1);
    }

    private static int buscarRecursiva(List<Paquete> lista, String codigo, int inicio, int fin) {
        if (inicio > fin) {
            return -1;
        }
        int medio = inicio + (fin - inicio) / 2;
        int comparacion = lista.get(medio).getCodigo().compareTo(codigo);

        if (comparacion == 0) {
            return medio;
        } else if (comparacion < 0) {
            return buscarRecursiva(lista, codigo, medio + 1, fin);
        } else {
            return buscarRecursiva(lista, codigo, inicio, medio - 1);
        }
    }

    /** O(n). Verifica la precondicion; usar una vez, no antes de cada busqueda. */
    public static boolean estaOrdenadaPorCodigo(List<Paquete> lista) {
        for (int i = 1; i < lista.size(); i++) {
            if (lista.get(i - 1).compareTo(lista.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }
}
