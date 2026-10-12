package parcial.modelo;

/**
 * Ciclo de vida de un paquete durante la jornada:

 * PENDIENTE:  cola de llegada y en la cola de prioridad.
 * DESPACHADO: puede estar esperando repartidor (enEsperaDeRepartidor).
 * ASIGNADO:   un repartidor lo lleva (map asignaciones).
 * ENTREGADO:  esta en el historial.
 */
public enum EstadoPaquete {
    PENDIENTE,
    DESPACHADO,
    ASIGNADO,
    ENTREGADO
}
