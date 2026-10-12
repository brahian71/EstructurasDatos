package parcial.servicio;

import parcial.comparadores.ComparadorDespacho;
import parcial.estructuras.Coleccion;
import parcial.estructuras.ListaEnlazada;
import parcial.modelo.EstadoPaquete;
import parcial.modelo.Paquete;
import parcial.modelo.Repartidor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Operaciones de una jornada de QuindioExpress.

 *
 *   ordenRegistro        ArrayList      conserva el orden exacto de registro (auditoria)
 *   paquetesPorCodigo    HashMap        consulta por codigo en O(1) esperado
 *   municipios           LinkedHashSet  sin duplicados, O(1); conserva orden de aparicion
 *   paquetesPorMunicipio TreeMap->List  agrupa por destino, municipios en orden alfabetico;
 *                                       historico: un paquete entregado no sale de su grupo
 *   estados              HashMap        codigo -> EstadoPaquete, consulta del ciclo de vida en O(1)
 *   colaLlegada          ArrayDeque     atencion FIFO de pendientes
 *   colaPrioridad        PriorityQueue  siguiente paquete segun ComparadorDespacho
 *   historial            ListaEnlazada  propia, orden real de entregas
 *   repartidores         LinkedHashMap  id unica + orden de registro
 *   asignaciones         HashMap        codigo de paquete -> repartidor que lo lleva
 *   enEsperaDeRepartidor ArrayDeque     despachados sin repartidor libre en su zona
 *
 *
 * Municipios y zonas se comparan con equals
 */
public class CentroDistribucion {

    private final List<Paquete> ordenRegistro = new ArrayList<>();
    private final Map<String, Paquete> paquetesPorCodigo = new HashMap<>();
    private final Set<String> municipios = new LinkedHashSet<>();
    private final Map<String, List<Paquete>> paquetesPorMunicipio = new TreeMap<>();
    private final Map<String, EstadoPaquete> estados = new HashMap<>();

    private final Queue<Paquete> colaLlegada = new ArrayDeque<>();
    private final PriorityQueue<Paquete> colaPrioridad = new PriorityQueue<>(new ComparadorDespacho());

    private final Map<String, Repartidor> repartidores = new LinkedHashMap<>();
    private final Map<String, Repartidor> asignaciones = new HashMap<>();
    private final Queue<Paquete> enEsperaDeRepartidor = new ArrayDeque<>();

    private final Coleccion<Paquete> historial = new ListaEnlazada<>();

    /**
     * Registra un paquete al final del orden de llegada.
     * La verificacion de duplicado es O(1) gracias al HashMap.

     */
    public void registrarPaquete(Paquete paquete) {
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo");
        }
        if (paquetesPorCodigo.containsKey(paquete.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un paquete con codigo " + paquete.getCodigo());
        }

        paquetesPorCodigo.put(paquete.getCodigo(), paquete);
        ordenRegistro.add(paquete);
        municipios.add(paquete.getDestino());
        paquetesPorMunicipio
                .computeIfAbsent(paquete.getDestino(), m -> new ArrayList<>())
                .add(paquete);
        estados.put(paquete.getCodigo(), EstadoPaquete.PENDIENTE);
        colaLlegada.offer(paquete);
        colaPrioridad.offer(paquete);
    }

    public EstadoPaquete getEstado(String codigo) {
        return estados.get(codigo);
    }

    public boolean existePaquete(String codigo) {
        return paquetesPorCodigo.containsKey(codigo);
    }

    public Paquete buscarPaquete(String codigo) {
        return paquetesPorCodigo.get(codigo);
    }

    /** Copia de solo lectura en orden de registro (nadie puede reordenarla desde afuera). */
    public List<Paquete> getPaquetesEnOrdenRegistro() {
        return Collections.unmodifiableList(ordenRegistro);
    }

    public int cantidadPaquetes() {
        return ordenRegistro.size();
    }


    /** Municipios con al menos un paquete, sin repetidos (orden de primera aparicion). */
    public Set<String> getMunicipios() {
        return Collections.unmodifiableSet(municipios);
    }

    /**
     * Municipios en orden alfabetico, sin repetidos.
     * Se construye bajo demanda: O(m log m), con m = numero de municipios (pequeno).
     * Alternativa: mantener un TreeSet en paralelo (cada registro costaria O(log m)).
     */
    public Set<String> getMunicipiosOrdenados() {
        return new TreeSet<>(municipios);
    }

    /**
     * Paquetes de un municipio en orden de registro. Lista vacia si no hay.
     * Es historico: incluye los ya entregados (sirve para reportes de carga por zona).
     */
    public List<Paquete> getPaquetesDeMunicipio(String municipio) {
        return Collections.unmodifiableList(paquetesPorMunicipio.getOrDefault(municipio, List.of()));
    }

    /** Municipio -> paquetes, recorrido en orden alfabetico de municipio (TreeMap). */
    public Map<String, List<Paquete>> getPaquetesPorMunicipio() {
        return Collections.unmodifiableMap(paquetesPorMunicipio);
    }

    public List<Paquete> ordenarPorCodigo() {
        List<Paquete> copia = new ArrayList<>(ordenRegistro);
        Collections.sort(copia);
        return copia;
    }

    public List<Paquete> ordenarPor(Comparator<Paquete> criterio) {
        List<Paquete> copia = new ArrayList<>(ordenRegistro);
        copia.sort(criterio);
        return copia;
    }

    public boolean hayPendientes() {
        return !colaLlegada.isEmpty();
    }

    public int cantidadPendientes() {
        return colaLlegada.size();
    }

    public Paquete verSiguientePorLlegada() {
        return colaLlegada.peek();
    }

    public Paquete verSiguientePorPrioridad() {
        return colaPrioridad.peek();
    }

    /**
     * Saca el primero en llegar (FIFO). Tambien debe salir de la cola de
     * prioridad, porque ya no esta pendiente: colaPrioridad.remove(o) es O(n).
     */
    public Paquete despacharPorLlegada() {
        Paquete paquete = colaLlegada.poll();
        if (paquete != null) {
            colaPrioridad.remove(paquete);
            estados.put(paquete.getCodigo(), EstadoPaquete.DESPACHADO);
        }
        return paquete;
    }

    /**
     * Saca el de mayor precedencia segun ComparadorDespacho: poll() es O(log n).
     * Tambien sale de la cola de llegada: colaLlegada.remove(o) es O(n).
     */
    public Paquete despacharPorPrioridad() {
        Paquete paquete = colaPrioridad.poll();
        if (paquete != null) {
            colaLlegada.remove(paquete);
            estados.put(paquete.getCodigo(), EstadoPaquete.DESPACHADO);
        }
        return paquete;
    }

    public void registrarRepartidor(Repartidor repartidor) {
        if (repartidor == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo");
        }
        if (repartidores.containsKey(repartidor.getIdentificacion())) {
            throw new IllegalArgumentException("Ya existe un repartidor con identificacion "
                    + repartidor.getIdentificacion());
        }
        repartidores.put(repartidor.getIdentificacion(), repartidor);
    }

    public Repartidor buscarRepartidor(String identificacion) {
        return repartidores.get(identificacion);
    }

    public List<Repartidor> getRepartidores() {
        return List.copyOf(repartidores.values());
    }

    public Repartidor buscarRepartidorDisponible(String municipio) {
        for (Repartidor r : repartidores.values()) {
            if (r.isDisponible() && r.getZona().equals(municipio)) {
                return r;
            }
        }
        return null;
    }

    /**
     * podria recibir dos asignaciones a la vez. containsValue es O(a), con a = asignaciones.
     */
    public void habilitarRepartidor(String identificacion) {
        Repartidor repartidor = repartidores.get(identificacion);
        if (repartidor == null) {
            throw new IllegalArgumentException("No existe el repartidor " + identificacion);
        }
        if (asignaciones.containsValue(repartidor)) {
            throw new IllegalStateException("El repartidor " + identificacion + " tiene un paquete en curso");
        }
        repartidor.setDisponible(true);
    }


    /**
     * al entregar borraba la unica evidencia y un paquete entregado se podia
     * volver a asignar (y entrar dos veces al historial).
     *
     * Se compara contra paquetesPorCodigo para que solo se acepten paquetes
     * registrados aqui; uno creado afuera con new Paquete(...) no tiene estado.
     */
    private void exigirEstado(Paquete paquete, EstadoPaquete esperado) {
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo");
        }
        EstadoPaquete actual = estados.get(paquete.getCodigo());
        if (actual == null) {
            throw new IllegalArgumentException("El paquete " + paquete.getCodigo() + " no esta registrado");
        }
        if (actual != esperado) {
            throw new IllegalStateException("El paquete " + paquete.getCodigo() + " esta " + actual
                    + "; se esperaba " + esperado);
        }
    }

    public void asignar(Paquete paquete, String idRepartidor) {
        exigirEstado(paquete, EstadoPaquete.DESPACHADO);
        Repartidor repartidor = repartidores.get(idRepartidor);
        if (repartidor == null) {
            throw new IllegalArgumentException("No existe el repartidor " + idRepartidor);
        }
        if (!repartidor.isDisponible()) {
            throw new IllegalStateException("El repartidor " + idRepartidor + " no esta disponible");
        }
        if (!repartidor.getZona().equals(paquete.getDestino())) {
            throw new IllegalStateException("La zona de " + repartidor.getNombre() + " (" + repartidor.getZona()
                    + ") no corresponde al destino " + paquete.getDestino());
        }

        enEsperaDeRepartidor.remove(paquete);
        asignaciones.put(paquete.getCodigo(), repartidor);
        estados.put(paquete.getCodigo(), EstadoPaquete.ASIGNADO);
        repartidor.setDisponible(false);
    }

    /**
     * Si no hay, el paquete (que ya salio de las colas de pendientes) queda en
     * la cola de espera para no perderlo; se reintenta con reintentarEnEspera().
     *
     * @return el repartidor asignado, o null si quedo en espera
     */
    public Repartidor asignarAutomaticamente(Paquete paquete) {
        exigirEstado(paquete, EstadoPaquete.DESPACHADO);
        Repartidor repartidor = buscarRepartidorDisponible(paquete.getDestino());
        if (repartidor == null) {
            if (!enEsperaDeRepartidor.contains(paquete)) {
                enEsperaDeRepartidor.offer(paquete);
            }
            return null;
        }
        asignar(paquete, repartidor.getIdentificacion());
        return repartidor;
    }

    /**
     * Intenta asignar los paquetes en espera, en el orden en que llegaron a ella.
     * Se llama despues de que un repartidor queda libre (entrega) o se habilita.

     * Costo: O(e * r), con e = paquetes en espera y r = repartidores.
     */
    public List<Paquete> reintentarEnEspera() {
        List<Paquete> asignados = new ArrayList<>();
        Iterator<Paquete> it = enEsperaDeRepartidor.iterator();
        while (it.hasNext()) {
            Paquete paquete = it.next();
            Repartidor repartidor = buscarRepartidorDisponible(paquete.getDestino());
            if (repartidor != null) {
                it.remove();
                asignar(paquete, repartidor.getIdentificacion());
                asignados.add(paquete);
            }
        }
        return asignados;
    }

    /** Despachados que no tenian repartidor libre en su zona, en orden de llegada a la espera. */
    public List<Paquete> getEnEsperaDeRepartidor() {
        return List.copyOf(enEsperaDeRepartidor);
    }

    public Repartidor getRepartidorAsignado(String codigoPaquete) {
        return asignaciones.get(codigoPaquete);
    }


    public void registrarEntrega(String codigoPaquete) {
        Paquete paquete = paquetesPorCodigo.get(codigoPaquete);
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete " + codigoPaquete + " no esta registrado");
        }
        exigirEstado(paquete, EstadoPaquete.ASIGNADO);

        Repartidor repartidor = asignaciones.remove(codigoPaquete);
        historial.agregar(paquete);
        estados.put(codigoPaquete, EstadoPaquete.ENTREGADO);
        repartidor.setDisponible(true);
    }

    /** El historial se expone como Coleccion: se recorre con for-each, sin ver nodos. */
    public Coleccion<Paquete> getHistorial() {
        return historial;
    }
}
