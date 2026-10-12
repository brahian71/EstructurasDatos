# Parte A. Análisis y diseño (borrador)

> Borrador para revisar en equipo. Cada integrante debe poder defender cada fila:
> por qué se eligió la estructura, cuál es la operación dominante, cuánto cuesta y qué se sacrifica.

## A.1. Selección de colecciones

| # | Necesidad | Estructura elegida | Operación principal | Complejidad esperada | Alternativa descartada | Justificación |
|---|---|---|---|---|---|---|
| 1 | Mantener los paquetes pendientes en el orden exacto de registro | `ArrayDeque<Paquete>` como `Queue` (atención FIFO) + `ArrayList<Paquete>` (registro histórico de auditoría) | `offer` al final, `poll` al frente | `offer`/`poll`: O(1) amortizado | `ArrayList` usado como cola | Quitar el primero de un `ArrayList` con `remove(0)` desplaza todos los demás: O(n). `ArrayDeque` lo hace en O(1). El `ArrayList` aparte nunca se modifica: conserva el orden original para auditorías aunque los paquetes salgan de la cola. |
| 2 | Consultar rápidamente un paquete por su código | `HashMap<String, Paquete>` | `get(codigo)`, `containsKey(codigo)` | O(1) esperado, O(n) peor caso con muchas colisiones (O(log n) desde Java 8 si el bucket se convierte en árbol) | `ArrayList` con búsqueda lineal; `TreeMap` | Con lista hay que recorrer en promedio n/2 paquetes. `TreeMap` daría O(log n) y orden por código, pero ese orden no se necesita para esta consulta. Se paga memoria extra por la tabla y por cada entrada. |
| 3 | Mantener los municipios destino sin duplicados | `LinkedHashSet<String>` | `add`, `contains` | O(1) esperado | `List` con `contains` antes de agregar; `HashSet` | Con lista, evitar duplicados cuesta O(m) por inserción. `HashSet` también es O(1), pero el orden de recorrido es impredecible; `LinkedHashSet` conserva el orden de primera aparición (coincide con el ejemplo del enunciado) a cambio de dos referencias extra por elemento. |
| 4 | Mantener los municipios ordenados alfabéticamente y sin repetidos | `TreeSet<String>` | Recorrido en orden; `add` | `add`: O(log m). Construirlo desde el `LinkedHashSet`: O(m log m) | Ordenar una `List` con `Collections.sort` | `TreeSet` garantiza a la vez orden y unicidad. Se construye bajo demanda porque m (número de municipios) es muy pequeño; si el reporte fuera muy frecuente convendría mantenerlo en paralelo (O(log m) por registro). |
| 5 | Atender primero los paquetes de mayor prioridad | `PriorityQueue<Paquete>` con `ComparadorDespacho` | `poll` (sacar el siguiente), `peek` | `offer`/`poll`: O(log n); `peek`: O(1) | Ordenar una lista cada vez que llega un paquete; `TreeSet` | Ordenar en cada llegada cuesta O(n log n). El heap solo garantiza que el tope es el mínimo según el comparador, lo cual basta. No se itera en orden (el iterador del heap NO va en orden de prioridad). |
| 6 | Mantener un historial de entregas respetando el orden de entrega | `ListaEnlazada<Paquete>` propia (exigida por el enunciado) | `agregar` al final; recorrido con `Iterator` | `agregar`: O(1) gracias a la referencia a la cola; recorrido: O(n) | `ArrayList`/`LinkedList` de Java (prohibidas por el enunciado) | Agregar al final mantiene el orden real. Sin referencia a la cola, `agregar` sería O(n). `obtener(i)` es O(n), por eso el recorrido se hace con el iterador y no con un for por índice (que sería O(n²)). |
| 7 | Asociar cada código con su objeto `Paquete` | `HashMap<String, Paquete>` (la misma estructura de la necesidad 2) | `put`, `get` | O(1) esperado | `Map` separado; `TreeMap` | Las necesidades 2 y 7 son la misma relación clave-valor; duplicarla gastaría memoria y obligaría a mantener ambas sincronizadas. El código es la clave natural porque es único. |

Estructuras de apoyo que también usa la solución:

| Necesidad | Estructura | Por qué |
|---|---|---|
| Paquetes agrupados por municipio | `TreeMap<String, List<Paquete>>` (valor `ArrayList`) | Es histórico (reportes de carga por zona): solo se agrega y se recorre. `TreeMap` da el reporte en orden alfabético con O(log m) por registro (m = 7). El valor es `ArrayList` y no `LinkedHashSet`: los dos conservan la inserción, pero el grupo nunca elimina y los duplicados ya los bloquea el `HashMap` de códigos. |
| Estado de cada paquete | `HashMap<String, EstadoPaquete>` | Valida en O(1) el ciclo de vida PENDIENTE → DESPACHADO → ASIGNADO → ENTREGADO: impide asignar uno pendiente, uno no registrado o volver a entregar uno ya entregado. |
| Repartidores con identificación única | `LinkedHashMap<String, Repartidor>` | `containsKey` O(1) para validar unicidad y orden de registro para buscar repartidor. |
| Paquete asignado → repartidor | `HashMap<String, Repartidor>` | Al registrar la entrega se sabe en O(1) qué repartidor liberar. |
| Paquetes despachados sin repartidor libre | `ArrayDeque<Paquete>` | Cola de espera FIFO; evita perder el paquete. `reintentarEnEspera()` la recorre con `Iterator` + `it.remove()`. |

### Decisión de diseño a defender: dos colas de pendientes

El mismo paquete pendiente está a la vez en la cola FIFO y en la `PriorityQueue`. Al despachar por una, hay que quitarlo de la otra con `remove(objeto)`, que cuesta **O(n)** en ambas estructuras.

- **Ventaja:** el código es simple y las dos modalidades siempre están consistentes.
- **Costo:** despachar es O(n) en vez de O(1)/O(log n).
- **Alternativa:** eliminación perezosa. Se mantiene un `Set<String>` de códigos pendientes y, al hacer `poll`, se descartan los que ya no estén pendientes. Despachar pasaría a ser O(1) amortizado (FIFO) u O(log n) (prioridad), a cambio de más memoria y una lógica más difícil de explicar.

## A.2. Tiempo frente a memoria

**Caso:** hoy hay 30 paquetes, se espera llegar a unos 500.000, y la operación más frecuente es buscar por código.

| Aspecto | `ArrayList<Paquete>` | `HashMap<String, Paquete>` |
|---|---|---|
| Búsqueda por código | Lineal: O(n). En promedio n/2 = 250.000 comparaciones de `String`; peor caso 500.000 (código inexistente). | O(1) esperado: calcular `hashCode()` del código, ir al bucket y comparar con `equals` pocos elementos. |
| Búsqueda binaria como alternativa | Si la lista se mantiene ordenada por código: O(log n), unas 19 comparaciones. Pero insertar manteniendo el orden cuesta O(n) por el desplazamiento. | No aplica. |
| Memoria adicional | Casi nula: un arreglo de referencias (≈ 4–8 bytes por paquete, más la holgura de crecimiento de hasta 50 %). | Una tabla de buckets (capacidad potencia de 2, factor de carga 0,75 → para 500.000 se redimensiona a 1.048.576 posiciones) y un objeto `Node` por entrada (hash, clave, valor, siguiente ≈ 32 bytes). Del orden de 20–25 MB adicionales. |
| Ventaja | Poca memoria, conserva el orden de llegada, acceso por posición O(1). | Búsqueda prácticamente constante sin importar el crecimiento; validar código duplicado al registrar también es O(1). |
| Desventaja | Cada consulta degrada linealmente con el crecimiento; con miles de consultas por jornada el costo total es O(consultas × n). | Más memoria; no conserva orden; su rendimiento depende de un buen `hashCode` (el de `String` es adecuado). |
| Decisión | — | **Se escoge `HashMap`** para la consulta por código, y se conserva **además** un `ArrayList` para el orden de registro, porque cada estructura atiende una necesidad distinta. |

**Justificación (no solo “HashMap es más rápido”):** con 500.000 paquetes y una búsqueda lineal promedio de 250.000 comparaciones, 1.000 consultas por jornada implican unos 250 millones de comparaciones. Con `HashMap` serían del orden de 1.000 cálculos de hash más unas pocas comparaciones cada uno. Pagar unos 20–25 MB extra es razonable para una aplicación de escritorio o servidor. Si la memoria fuera el recurso crítico, la alternativa intermedia sería una lista ordenada por código con búsqueda binaria (O(log n)), aceptando que registrar sea O(n).

## Supuestos y aclaraciones del enunciado

- **Prioridad:** el enunciado dice 0–10 con 5 por defecto en la descripción del atributo, y 1–5 en las reglas. **Confirmado con el profesor: 1 a 5, con 3 por defecto** (`Paquete.PRIORIDAD_POR_DEFECTO`).
- **Un repartidor = una asignación a la vez:** al asignar queda no disponible; al registrar la entrega vuelve a estar disponible.
- **Coincidencia de zona:** se compara `zona` con `destino` con `equals` exacto, la misma igualdad que usan el `Set` y los `Map` de municipios.
- **Municipios sin tilde** en los datos (`Calarca`) para evitar problemas de codificación en la consola de Windows.
