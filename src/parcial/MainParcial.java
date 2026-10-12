package parcial;

import parcial.algoritmos.BusquedaBinaria;
import parcial.algoritmos.DivideYVencerasPaquetes;
import parcial.algoritmos.Recursivos;
import parcial.comparadores.ComparadorPeso;
import parcial.comparadores.ComparadorPrioridad;
import parcial.comparadores.ComparadorTiempo;
import parcial.modelo.Paquete;
import parcial.modelo.Repartidor;
import parcial.servicio.CentroDistribucion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainParcial {

    public static void main(String[] args) {
        CentroDistribucion centro = DatosIniciales.crearCentro();

        registroYOrden(centro);
        validaciones(centro);
        consultaPorCodigo(centro);
        municipios(centro);
        ordenamientos(centro);
        ejemploDespachoDelEnunciado();
        atencionYEntregas(centro);
        recursividad(centro);
        busquedaBinaria(centro);
        divideYVenceras(centro);
    }

    private static void  registroYOrden(CentroDistribucion centro) {
        titulo("1. Registro y orden de llegada");
        System.out.println("Paquetes registrados: " + centro.cantidadPaquetes());
        System.out.println("Repartidores: " + centro.getRepartidores().size());
        imprimirCodigos("Orden de registro", centro.getPaquetesEnOrdenRegistro());
    }

    private static void validaciones(CentroDistribucion centro) {
        titulo("2. Reglas de validacion");
        intentar("Codigo duplicado PQ731",
                () -> centro.registrarPaquete(new Paquete("PQ731", "Armenia", 1.0, 3, 10)));
        intentar("Peso cero", () -> new Paquete("PQ001", "Armenia", 0, 3, 10));
        intentar("Prioridad 7", () -> new Paquete("PQ002", "Armenia", 1.0, 7, 10));
        intentar("Tiempo negativo", () -> new Paquete("PQ003", "Armenia", 1.0, 3, -5));
        intentar("Destino vacio", () -> new Paquete("PQ004", "  ", 1.0, 3, 10));
        intentar("Repartidor duplicado R001",
                () -> centro.registrarRepartidor(new Repartidor("R001", "Otro", "Armenia", true)));
        System.out.println("Prioridad por defecto: " + new Paquete("PQ005", "Armenia", 1.0, 10).getPrioridad());
    }

    private static void consultaPorCodigo(CentroDistribucion centro) {
        titulo("3. Consulta por codigo (HashMap, O(1) esperado)");
        System.out.println("Existe PQ845? " + centro.existePaquete("PQ845") + " -> " + centro.buscarPaquete("PQ845"));
        System.out.println("Existe PQ999? " + centro.existePaquete("PQ999") + " -> " + centro.buscarPaquete("PQ999"));
    }

    private static void municipios(CentroDistribucion centro) {
        titulo("4. Municipios y agrupacion");
        System.out.println("Sin repetidos (orden de aparicion): " + centro.getMunicipios());
        System.out.println("Ordenados alfabeticamente:          " + centro.getMunicipiosOrdenados());
        // Map no es Collection: se recorre con entrySet(). Al ser TreeMap, sale en orden alfabetico.
        System.out.println("Paquetes por municipio (TreeMap):");
        for (Map.Entry<String, List<Paquete>> grupo : centro.getPaquetesPorMunicipio().entrySet()) {
            imprimirCodigos("  " + grupo.getKey(), grupo.getValue());
        }
    }

    private static void ordenamientos(CentroDistribucion centro) {
        titulo("5. Orden natural (Comparable) y criterios alternativos (Comparator)");
        imprimirCodigos("Natural (codigo)", centro.ordenarPorCodigo());
        imprimir("Prioridad desc", centro.ordenarPor(new ComparadorPrioridad()), 5);
        imprimir("Peso desc", centro.ordenarPor(new ComparadorPeso()), 5);
        imprimir("Tiempo asc", centro.ordenarPor(new ComparadorTiempo()), 5);
        imprimirCodigos("Orden de registro sigue intacto", centro.getPaquetesEnOrdenRegistro());
    }

    private static void ejemploDespachoDelEnunciado() {
        titulo("6. Ejemplo del enunciado: despacho prioritario (esperado PQ105, PQ731, PQ942, PQ420)");
        CentroDistribucion ejemplo = new CentroDistribucion();
        ejemplo.registrarPaquete(new Paquete("PQ420", "Armenia", 1.0, 3, 30));
        ejemplo.registrarPaquete(new Paquete("PQ731", "Armenia", 1.0, 5, 45));
        ejemplo.registrarPaquete(new Paquete("PQ105", "Armenia", 1.0, 5, 25));
        ejemplo.registrarPaquete(new Paquete("PQ942", "Armenia", 1.0, 4, 20));

        StringBuilder orden = new StringBuilder();
        while (ejemplo.hayPendientes()) {
            orden.append(ejemplo.despacharPorPrioridad().getCodigo()).append(" ");
        }
        System.out.println("Obtenido: " + orden.toString().trim());
    }

    private static void atencionYEntregas(CentroDistribucion centro) {
        titulo("7. Atencion, asignacion y entregas");

        System.out.println("Siguiente por llegada:   " + centro.verSiguientePorLlegada());
        System.out.println("Siguiente por prioridad: " + centro.verSiguientePorPrioridad());

        System.out.println("\n-- Despacho por orden de llegada (3 paquetes) --");
        for (int i = 0; i < 3; i++) {
            despacharAsignarYEntregar(centro, centro.despacharPorLlegada());
        }

        System.out.println("\n-- Despacho prioritario (4 paquetes) --");
        for (int i = 0; i < 4; i++) {
            despacharAsignarYEntregar(centro, centro.despacharPorPrioridad());
        }

        System.out.println("\n-- Reglas de asignacion --");
        // PQ407 (Circasia) esta DESPACHADO y en espera: el unico de Circasia (R005) no esta disponible
        Paquete enEspera = centro.buscarPaquete("PQ407");
        intentar("Asignar PQ407 a R005 (no disponible)", () -> centro.asignar(enEspera, "R005"));
        intentar("Asignar PQ407 (Circasia) a R001 (zona Armenia)", () -> centro.asignar(enEspera, "R001"));

        System.out.println("\n-- Ciclo de vida del paquete (estado: " + centro.getEstado("PQ731") + " para PQ731) --");
        intentar("Asignar otra vez PQ731, ya ENTREGADO",
                () -> centro.asignar(centro.buscarPaquete("PQ731"), "R001"));
        intentar("Registrar otra vez la entrega de PQ731",
                () -> centro.registrarEntrega("PQ731"));
        intentar("Asignar PQ318, todavia PENDIENTE (no despachado)",
                () -> centro.asignar(centro.buscarPaquete("PQ318"), "R004"));
        intentar("Asignar un paquete que no esta registrado en el centro",
                () -> centro.asignar(new Paquete("PQ999", "Armenia", 1.0, 3, 10), "R001"));

        System.out.println("\n-- Cola de espera --");
        imprimirCodigos("En espera de repartidor", centro.getEnEsperaDeRepartidor());
        intentar("Habilitar un repartidor que no existe (R999)",
                () -> centro.habilitarRepartidor("R999"));
        System.out.println("Se habilita a R005 (Circasia) y se reintenta la espera");
        centro.habilitarRepartidor("R005");
        for (Paquete paquete : centro.reintentarEnEspera()) {
            Repartidor repartidor = centro.getRepartidorAsignado(paquete.getCodigo());
            System.out.println(paquete.getCodigo() + " -> asignado a " + repartidor.getNombre());
            centro.registrarEntrega(paquete.getCodigo());
            System.out.println("        entregado, estado: " + centro.getEstado(paquete.getCodigo()));
        }
        imprimirCodigos("En espera de repartidor", centro.getEnEsperaDeRepartidor());

        System.out.println("\nPendientes: " + centro.cantidadPendientes());
        System.out.println("Historial (lista enlazada propia, " + centro.getHistorial().tamanio() + " entregas):");
        int posicion = 1;
        for (Paquete p : centro.getHistorial()) {
            System.out.println("  " + posicion++ + ". " + p);
        }
    }

    private static void despacharAsignarYEntregar(CentroDistribucion centro, Paquete paquete) {
        // despachar...() devuelve null cuando ya no hay pendientes
        if (paquete == null) {
            System.out.println("No hay paquetes pendientes para despachar");
            return;
        }
        Repartidor repartidor = centro.asignarAutomaticamente(paquete);
        if (repartidor == null) {
            System.out.println(paquete.getCodigo() + " -> sin repartidor disponible para " + paquete.getDestino()
                    + ", queda en espera");
            return;
        }
        System.out.println(paquete.getCodigo() + " -> asignado a " + repartidor.getNombre()
                + " (disponible ahora: " + repartidor.isDisponible() + ")");
        centro.registrarEntrega(paquete.getCodigo());
        System.out.println("        entregado, " + repartidor.getNombre()
                + " disponible otra vez: " + repartidor.isDisponible());
    }

    private static void recursividad(CentroDistribucion centro) {
        titulo("8. Recursividad");
        List<Paquete> todos = centro.getPaquetesEnOrdenRegistro();
        System.out.printf("Peso total Armenia (30 paquetes): %.2f kg%n",
                Recursivos.pesoTotalPorMunicipio(todos, "Armenia"));
        System.out.println("Paquetes con prioridad >= 4: " + Recursivos.contarPrioridadMinima(todos, 4));

        System.out.println("\n-- Ejemplo del enunciado (esperado 14.0) --");
        List<Paquete> ejemploPeso = List.of(
                new Paquete("PQ101", "Armenia", 4.5, 3, 10),
                new Paquete("PQ203", "Salento", 3.0, 3, 10),
                new Paquete("PQ415", "Armenia", 7.2, 3, 10),
                new Paquete("PQ730", "Armenia", 2.3, 3, 10));
        System.out.println("Peso total Armenia: " + Recursivos.pesoTotalPorMunicipio(ejemploPeso, "Armenia"));

        System.out.println("\n-- Ejemplo del enunciado (prioridades 5,2,4,1,5,3,4, minimo 4, esperado 4) --");
        int[] prioridades = {5, 2, 4, 1, 5, 3, 4};
        List<Paquete> ejemploPrioridad = new ArrayList<>();
        for (int i = 0; i < prioridades.length; i++) {
            ejemploPrioridad.add(new Paquete("PQ" + (500 + i * 37), "Armenia", 1.0, prioridades[i], 10));
        }
        System.out.println("Con prioridad >= 4: " + Recursivos.contarPrioridadMinima(ejemploPrioridad, 4));
    }

    private static void busquedaBinaria(CentroDistribucion centro) {
        titulo("9. Busqueda binaria (precondicion: ordenada por codigo)");
        List<Paquete> desordenada = centro.getPaquetesEnOrdenRegistro();
        List<Paquete> ordenada = centro.ordenarPorCodigo();
        System.out.println("Orden de registro ordenado por codigo? " + BusquedaBinaria.estaOrdenadaPorCodigo(desordenada));
        System.out.println("Copia ordenada ordenada por codigo?    " + BusquedaBinaria.estaOrdenadaPorCodigo(ordenada));

        System.out.println("\nBuscar PQ731 (" + ordenada.size() + " paquetes):");
        int posicion = BusquedaBinaria.buscarIterativa(ordenada, "PQ731");
        System.out.println("  posicion " + posicion + " -> " + ordenada.get(posicion));

        System.out.println("\nBuscar PQ500 (no existe):");
        System.out.println("  posicion " + BusquedaBinaria.buscarIterativa(ordenada, "PQ500"));

        System.out.println("\nRecursiva PQ963 -> posicion " + BusquedaBinaria.buscarRecursiva(ordenada, "PQ963"));
        System.out.println("Sobre la lista SIN ordenar, buscar PQ034 -> "
                + BusquedaBinaria.buscarIterativa(desordenada, "PQ034") + " (PQ034 si existe: precondicion violada)");
    }

    private static void divideYVenceras(CentroDistribucion centro) {
        titulo("10. Divide y venceras: paquete de mayor peso");
        System.out.println("Mayor de los 30: " + DivideYVencerasPaquetes.mayorPeso(centro.getPaquetesEnOrdenRegistro()));

        System.out.println("\n-- Ejemplo del enunciado (4.5, 12.0, 3.2, 18.7, 7.4, 5.1) --");
        double[] pesos = {4.5, 12.0, 3.2, 18.7, 7.4, 5.1};
        List<Paquete> ejemplo = new ArrayList<>();
        for (int i = 0; i < pesos.length; i++) {
            ejemplo.add(new Paquete("PQ" + (100 + i * 111), "Armenia", pesos[i], 3, 10));
        }
        System.out.println("Mayor peso: " + DivideYVencerasPaquetes.mayorPeso(ejemplo));
    }

    // ======================= utilidades de impresion =======================

    private static void titulo(String texto) {
        System.out.println("\n==========================================================");
        System.out.println(texto);
        System.out.println("==========================================================");
    }

    private static void imprimirCodigos(String etiqueta, List<Paquete> paquetes) {
        StringBuilder sb = new StringBuilder();
        for (Paquete p : paquetes) {
            if (!sb.isEmpty()) {
                sb.append(", ");
            }
            sb.append(p.getCodigo());
        }
        System.out.println(etiqueta + ": " + sb);
    }

    private static void imprimir(String etiqueta, List<Paquete> paquetes, int cuantos) {
        System.out.println(etiqueta + " (primeros " + cuantos + "):");
        for (int i = 0; i < Math.min(cuantos, paquetes.size()); i++) {
            System.out.println("  " + paquetes.get(i));
        }
    }

    private static void intentar(String descripcion, Runnable accion) {
        try {
            accion.run();
            System.out.println("[FALLO] " + descripcion + ": se esperaba un error y no ocurrio");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[OK] " + descripcion + " -> rechazado: " + e.getMessage());
        }
    }
}
