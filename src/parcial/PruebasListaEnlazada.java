package parcial;

import parcial.estructuras.Coleccion;
import parcial.estructuras.ListaEnlazada;
import parcial.modelo.Paquete;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Pruebas de los casos borde que pide el enunciado para la lista propia.
 * No usa JUnit para no agregar dependencias: cada prueba imprime OK o FALLO.
 * Se usa con String, Integer y Paquete para evidenciar que la lista es generica.
 */
public class PruebasListaEnlazada {

    private static int exitos = 0;
    private static int fallos = 0;

    public static void main(String[] args) {
        listaVacia();
        unSoloElemento();
        eliminarPrimero();
        eliminarIntermedio();
        eliminarUltimoYAgregarDespues();
        eliminarInexistente();
        obtenerFueraDeRango();
        iteradorRecorreEnOrden();
        iteradorSinElementos();
        otroTipoGenerico();
        conPaquetes();

        System.out.println("\nResultado: " + exitos + " OK, " + fallos + " FALLO");
    }

    private static void listaVacia() {
        Coleccion<String> lista = new ListaEnlazada<>();
        verificar("vacia: estaVacia", lista.estaVacia());
        verificar("vacia: tamanio 0", lista.tamanio() == 0);
        verificar("vacia: eliminar retorna false", !lista.eliminar("PQ1"));
        verificar("vacia: iterador sin elementos", !lista.iterator().hasNext());
    }

    private static void unSoloElemento() {
        Coleccion<String> lista = new ListaEnlazada<>();
        lista.agregar("PQ1");
        verificar("uno: tamanio 1", lista.tamanio() == 1);
        verificar("uno: obtener(0)", "PQ1".equals(lista.obtener(0)));
        verificar("uno: eliminar", lista.eliminar("PQ1"));
        verificar("uno: queda vacia", lista.estaVacia());
        // si la cola no se limpio al eliminar, este agregar dejaria la lista rota
        lista.agregar("PQ2");
        verificar("uno: agregar tras vaciar", lista.tamanio() == 1 && "PQ2".equals(lista.obtener(0)));
    }

    private static void eliminarPrimero() {
        Coleccion<String> lista = lista("A", "B", "C");
        verificar("primero: eliminar A", lista.eliminar("A"));
        verificar("primero: queda [B, C]", contenido(lista).equals("B C"));
    }

    private static void eliminarIntermedio() {
        Coleccion<String> lista = lista("A", "B", "C");
        verificar("intermedio: eliminar B", lista.eliminar("B"));
        verificar("intermedio: queda [A, C]", contenido(lista).equals("A C"));
    }

    private static void eliminarUltimoYAgregarDespues() {
        Coleccion<String> lista = lista("A", "B", "C");
        verificar("ultimo: eliminar C", lista.eliminar("C"));
        verificar("ultimo: queda [A, B]", contenido(lista).equals("A B"));
        // si la cola siguiera apuntando a C, D quedaria colgado de un nodo fuera de la lista
        lista.agregar("D");
        verificar("ultimo: agregar despues -> [A, B, D]", contenido(lista).equals("A B D"));
    }

    private static void eliminarInexistente() {
        Coleccion<String> lista = lista("A", "B");
        verificar("inexistente: retorna false", !lista.eliminar("Z"));
        verificar("inexistente: tamanio igual", lista.tamanio() == 2);
    }

    private static void obtenerFueraDeRango() {
        Coleccion<String> lista = lista("A");
        verificar("obtener(-1) lanza excepcion", lanza(() -> lista.obtener(-1)));
        verificar("obtener(1) lanza excepcion", lanza(() -> lista.obtener(1)));
    }

    private static void iteradorRecorreEnOrden() {
        Coleccion<String> lista = lista("PQ105", "PQ942", "PQ731");
        verificar("iterador: orden de insercion", contenido(lista).equals("PQ105 PQ942 PQ731"));
    }

    private static void iteradorSinElementos() {
        Iterator<String> it = new ListaEnlazada<String>().iterator();
        boolean lanzo;
        try {
            it.next();
            lanzo = false;
        } catch (NoSuchElementException e) {
            lanzo = true;
        }
        verificar("iterador: next() sin elementos lanza NoSuchElementException", lanzo);
    }

    private static void otroTipoGenerico() {
        Coleccion<Integer> numeros = new ListaEnlazada<>();
        numeros.agregar(10);
        numeros.agregar(20);
        numeros.agregar(30);
        int suma = 0;
        for (int n : numeros) {
            suma += n;
        }
        verificar("generica con Integer: suma 60", suma == 60);
    }

    /**
     * Mismo tipo que usa el historial. eliminar compara con equals, y el equals de
     * Paquete es por codigo: un objeto DISTINTO con el mismo codigo elimina al original.
     */
    private static void conPaquetes() {
        Coleccion<Paquete> historial = new ListaEnlazada<>();
        historial.agregar(new Paquete("PQ105", "Salento", 3.2, 5, 25));
        historial.agregar(new Paquete("PQ942", "Armenia", 12.0, 4, 20));
        historial.agregar(new Paquete("PQ731", "Armenia", 8.4, 5, 45));
        verificar("Paquete: obtener(1) es PQ942", historial.obtener(1).getCodigo().equals("PQ942"));

        Paquete mismoCodigo = new Paquete("PQ942", "Calarca", 1.0, 1, 5);
        verificar("Paquete: eliminar con otro objeto de igual codigo", historial.eliminar(mismoCodigo));
        StringBuilder codigos = new StringBuilder();
        for (Paquete p : historial) {
            codigos.append(p.getCodigo()).append(" ");
        }
        verificar("Paquete: queda PQ105 PQ731", codigos.toString().trim().equals("PQ105 PQ731"));
    }

    // ---------- utilidades ----------

    private static Coleccion<String> lista(String... valores) {
        Coleccion<String> lista = new ListaEnlazada<>();
        for (String v : valores) {
            lista.agregar(v);
        }
        return lista;
    }

    private static String contenido(Coleccion<String> lista) {
        StringBuilder sb = new StringBuilder();
        for (String s : lista) {
            if (!sb.isEmpty()) {
                sb.append(" ");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    private static boolean lanza(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private static void verificar(String nombre, boolean condicion) {
        if (condicion) {
            exitos++;
            System.out.println("[OK]    " + nombre);
        } else {
            fallos++;
            System.out.println("[FALLO] " + nombre);
        }
    }
}
