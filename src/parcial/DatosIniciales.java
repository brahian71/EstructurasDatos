package parcial;

import parcial.modelo.Paquete;
import parcial.modelo.Repartidor;
import parcial.servicio.CentroDistribucion;

/**
 * Datos minimos exigidos por el enunciado:
 *   - 30 paquetes con codigos no consecutivos y sin relacion con el orden de llegada
 *   - 7 destinos diferentes, varios paquetes por municipio
 *   - prioridades de 1 a 5 y pesos variados
 *   - 8 repartidores, uno de ellos NO disponible (R005) para probar la regla

 */
public final class DatosIniciales {

    private DatosIniciales() {
    }

    public static CentroDistribucion crearCentro() {
        CentroDistribucion centro = new CentroDistribucion();
        cargarPaquetes(centro);
        cargarRepartidores(centro);
        return centro;
    }

    public static void cargarPaquetes(CentroDistribucion centro) {
        //                          codigo   destino       peso  prio  min
        centro.registrarPaquete(new Paquete("PQ731", "Armenia",     8.4, 5, 45));
        centro.registrarPaquete(new Paquete("PQ105", "Salento",     3.2, 5, 25));
        centro.registrarPaquete(new Paquete("PQ942", "Armenia",    12.0, 4, 20));
        centro.registrarPaquete(new Paquete("PQ318", "Calarca",     5.5, 2, 35));
        centro.registrarPaquete(new Paquete("PQ567", "Salento",     2.1, 3, 50));
        centro.registrarPaquete(new Paquete("PQ829", "Armenia",     6.7, 1, 15));
        centro.registrarPaquete(new Paquete("PQ420", "Montenegro",  4.0, 3, 30));
        centro.registrarPaquete(new Paquete("PQ214", "Circasia",    9.3, 4, 40));
        centro.registrarPaquete(new Paquete("PQ653", "Quimbaya",    1.8, 2, 55));
        centro.registrarPaquete(new Paquete("PQ087", "Filandia",   15.6, 5, 60));
        centro.registrarPaquete(new Paquete("PQ476", "Calarca",     7.1, 3, 25));
        centro.registrarPaquete(new Paquete("PQ390", "Armenia",     2.9, 5, 10));
        centro.registrarPaquete(new Paquete("PQ158", "Montenegro", 11.2, 1, 45));
        centro.registrarPaquete(new Paquete("PQ702", "Circasia",    3.6, 3, 30));
        centro.registrarPaquete(new Paquete("PQ845", "Salento",    18.7, 4, 65));
        centro.registrarPaquete(new Paquete("PQ263", "Quimbaya",    4.4, 5, 50));
        centro.registrarPaquete(new Paquete("PQ519", "Filandia",    6.0, 2, 70));
        centro.registrarPaquete(new Paquete("PQ034", "Armenia",    10.5, 3, 20));
        centro.registrarPaquete(new Paquete("PQ991", "Calarca",     2.5, 4, 30));
        centro.registrarPaquete(new Paquete("PQ612", "Montenegro",  5.9, 5, 40));
        centro.registrarPaquete(new Paquete("PQ447", "Circasia",   13.3, 2, 35));
        centro.registrarPaquete(new Paquete("PQ378", "Armenia",     1.2, 4, 15));
        centro.registrarPaquete(new Paquete("PQ706", "Quimbaya",    8.8, 3, 45));
        centro.registrarPaquete(new Paquete("PQ125", "Salento",     4.7, 1, 55));
        centro.registrarPaquete(new Paquete("PQ880", "Filandia",    3.3, 4, 75));
        centro.registrarPaquete(new Paquete("PQ541", "Calarca",     9.9, 5, 25));
        centro.registrarPaquete(new Paquete("PQ296", "Armenia",     7.6, 2, 10));
        centro.registrarPaquete(new Paquete("PQ963", "Montenegro",  2.0, 3, 50));
        centro.registrarPaquete(new Paquete("PQ407", "Circasia",    5.0, 5, 30));
        centro.registrarPaquete(new Paquete("PQ754", "Armenia",    14.1, 3, 25));
    }

    public static void cargarRepartidores(CentroDistribucion centro) {
        centro.registrarRepartidor(new Repartidor("R001", "Ana Gomez",       "Armenia",    true));
        centro.registrarRepartidor(new Repartidor("R002", "Carlos Rios",     "Armenia",    true));
        centro.registrarRepartidor(new Repartidor("R003", "Lucia Marin",     "Salento",    true));
        centro.registrarRepartidor(new Repartidor("R004", "Jorge Patino",    "Calarca",    true));
        centro.registrarRepartidor(new Repartidor("R005", "Marta Castano",   "Circasia",   false));
        centro.registrarRepartidor(new Repartidor("R006", "Pedro Londono",   "Montenegro", true));
        centro.registrarRepartidor(new Repartidor("R007", "Sofia Arango",    "Quimbaya",   true));
        centro.registrarRepartidor(new Repartidor("R008", "Diego Velez",     "Filandia",   true));
    }
}
