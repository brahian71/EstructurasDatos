# Mapa del Parcial 1: decisiones justificadas y hallazgos

> Apuntes de estudio y base para el entregable. Cada ficha responde las cuatro preguntas
> que evalúa el enunciado: **por qué se eligió, cuál es la operación dominante, cuánto cuesta
> y qué se sacrifica frente a la alternativa**.
>
> Alimenta: **Parte A** (justificación de estructuras), **Parte C** (costos y alternativas)
> y **Parte D.2** (hallazgos reales sobre código generado con IA).
>
> Convención de estado: ✅ se defiende tal como está · ⚠️ funciona pero hay que decidir o justificar · ❌ error que hay que corregir.

---

## 1. Mapa del código

```
src/parcial/
├── modelo/
│   ├── Paquete.java            Comparable<Paquete> (orden natural = código), equals/hashCode por código
│   ├── Repartidor.java         equals/hashCode por identificación; 'disponible' es lo único mutable
│   └── EstadoPaquete.java      enum: PENDIENTE → DESPACHADO → ASIGNADO → ENTREGADO
├── comparadores/               criterios alternativos (Comparator), independientes del orden natural
│   ├── ComparadorPrioridad     mayor a menor
│   ├── ComparadorPeso          mayor a menor
│   ├── ComparadorTiempo        menor a mayor
│   └── ComparadorDespacho      prioridad → tiempo → código (compone los anteriores)
├── estructuras/
│   ├── Coleccion.java          INTERFAZ GENÉRICA, extends Iterable<T>
│   └── ListaEnlazada.java      CLASE GENÉRICA, lista simple propia + Nodo<T> + IteradorLista
├── algoritmos/
│   ├── Recursivos.java         peso por municipio, contar prioridad mínima (sin ciclos)
│   ├── BusquedaBinaria.java    iterativa y recursiva (+ estaOrdenadaPorCodigo)
│   └── DivideYVencerasPaquetes paquete de mayor peso
├── servicio/
│   └── CentroDistribucion.java TODAS las colecciones de Java viven aquí
├── DatosIniciales.java         30 paquetes, 7 municipios, 8 repartidores
├── MainParcial.java            demo, una sección por requisito
└── PruebasListaEnlazada.java   casos borde de la lista propia
```

## 2. Dónde está cada tema del curso

| Tema exigido | Dónde | Cómo reconocerlo |
|---|---|---|
| Interfaz genérica | `estructuras/Coleccion.java:12` | `public interface Coleccion<T> extends Iterable<T>` |
| Iterable | misma línea | `ListaEnlazada` **no** dice `implements Iterable`: lo hereda a través de `Coleccion` |
| Clase genérica | `estructuras/ListaEnlazada.java:26` | `ListaEnlazada<T> implements Coleccion<T>` |
| Nodo propio | `ListaEnlazada.java:28` | `private static class Nodo<T>`: valor + `siguiente` |
| Iterator | `ListaEnlazada.java:117` y `:126` | `iterator()` devuelve `new IteradorLista()`, que `implements Iterator<T>` |
| Recorrido sin ver nodos | `MainParcial.java:123`, `PruebasListaEnlazada.java:110` | `for (Paquete p : centro.getHistorial())` → llama `iterator()`, `hasNext()`, `next()` |
| Genérico reutilizable | `CentroDistribucion.java:58` y `PruebasListaEnlazada.java:105` | `Coleccion<Paquete>` y `Coleccion<Integer>` con la misma clase |
| Comparable | `modelo/Paquete.java:20`, `:83` | `compareTo` por código |
| Comparator | `comparadores/*`, usados en `CentroDistribucion.java:52` y `MainParcial.java:76-78` | |
| List, Set, Map, Queue, PriorityQueue | `CentroDistribucion.java:46-58` | ver fichas de la sección 3 |
| Recursividad | `algoritmos/Recursivos.java` | caso base `indice == size`, avance `indice + 1` |
| Búsqueda binaria | `algoritmos/BusquedaBinaria.java` | precondición: ordenada por código |
| Divide y vencerás | `algoritmos/DivideYVencerasPaquetes.java` | dos mitades `[inicio, medio]` y `[medio+1, fin]` + combinación |

```
Iterable<T>                       (Java: "se puede recorrer con for-each")
   ^ extends
Coleccion<T>                      (contrato propio: QUÉ operaciones hay)
   ^ implements
ListaEnlazada<T>                  (CÓMO se hacen: nodos y referencias)
   │  iterator()  crea
   └──────────────────────► IteradorLista implements Iterator<T>
                             (recuerda el nodo 'actual'; cada iterator() = recorrido nuevo)
```

---

## 3. Fichas de decisión: colecciones en `CentroDistribucion`

### 3.1 `ordenRegistro`: `List<Paquete>` → `ArrayList` ✅
- **Necesidad:** conservar el orden exacto de registro para auditoría; registrar no debe mover a nadie.
- **Operación dominante:** `add` al final y recorrido/`get(i)` (lo usan los recursivos y divide y vencerás).
- **Costo:** `add` O(1) amortizado; `get(i)` O(1).
- **Alternativas descartadas:**
  - `LinkedList` / `ListaEnlazada` propia: `get(i)` O(n). Los recursivos que hacen `get(indice)` pasarían a O(n²).
  - `LinkedHashMap<String, Paquete>`: daría orden **y** búsqueda por código en una sola estructura, pero no tiene acceso por posición, que los algoritmos necesitan.
- **Se sacrifica:** memoria duplicada (las referencias también están en el `HashMap`); es un arreglo de referencias, no copias de paquetes.
- **Nunca se eliminan elementos:** por eso no importa que `remove(0)` sea O(n).

### 3.2 `paquetesPorCodigo`: `Map<String, Paquete>` → `HashMap` ✅ (necesidades 2 y 7)
- **Necesidad:** saber si un código existe y consultar el paquete sin recorrer todos; validar el duplicado al registrar.
- **Operación dominante:** `get` y `containsKey`.
- **Costo:** O(1) esperado (`hashCode` de la clave → bucket → `equals` con pocos elementos).
- **Alternativas descartadas:**
  - `ArrayList` + búsqueda lineal: O(n).
  - `ArrayList` ordenada + búsqueda binaria: O(log n) al buscar, pero insertar manteniendo el orden cuesta O(n).
  - `TreeMap`: O(log n) y daría orden por código, que esta consulta no necesita.
- **Se sacrifica:** memoria (tabla de buckets + un nodo por entrada) y el orden.
- **Decisión clave:** las necesidades 2 y 7 son **la misma relación código → paquete**, así que se usa un solo map; dos copias habría que mantenerlas sincronizadas.
- **Por qué funciona:** la clave es un `String`, que ya trae `hashCode`/`equals` correctos.

### 3.3 `municipios`: `Set<String>` → ¿`HashSet`, `LinkedHashSet` o `TreeSet`? ⚠️
| Opción | Orden al recorrer | `add`/`contains` | Mecanismo |
|---|---|---|---|
| `List` + `contains` antes de `add` | inserción | O(m) | recorrido |
| `HashSet` | ninguno (impredecible) | O(1) | `hashCode` + `equals` |
| **`LinkedHashSet` (elegido)** | **primera aparición** | O(1) | hash + lista doblemente enlazada |
| `TreeSet` | alfabético | O(log m) | árbol rojo-negro, `compareTo` |
- **Por qué `LinkedHashSet`:** el ejemplo del enunciado lista "Armenia, Salento, Calarcá", que es el orden de aparición. Un `HashSet` podría mostrarlos en cualquier orden.
- **Se sacrifica:** dos referencias extra por elemento frente al `HashSet`.
- **⚠️ Hallazgo H6 (redundancia):** las **claves** de `paquetesPorMunicipio` ya son exactamente los municipios sin repetir. Este `Set` duplica información que hay que mantener sincronizada. Ver la ficha 3.5.

### 3.4 Municipios ordenados → `new TreeSet<>(municipios)` bajo demanda ✅/⚠️
- **Necesidad:** orden alfabético y sin repetidos (reporte administrativo).
- **Costo:** O(m log m) cada vez que se pide, con m = número de municipios (7).
- **Alternativas:**
  - `TreeSet` mantenido en paralelo: O(log m) por registro y el reporte sale gratis. Conviene si el reporte se pide muy seguido.
  - Copiar a `List` + `Collections.sort`: también O(m log m), pero no garantiza unicidad por sí misma.
  - `TreeMap` para `paquetesPorMunicipio`: ver la ficha 3.5.
- **Defensa:** con m tan pequeño, construirlo bajo demanda es más simple y evita otra estructura que sincronizar.

### 3.5 `paquetesPorMunicipio`: `Map<String, List<Paquete>>` → **`TreeMap`** con valor `ArrayList` ✅ (decidido, H6)
- **Necesidad:** municipio → paquetes destinados a él (Armenia → PQ731, PQ942, PQ829). Según el enunciado,
  sirve "para generar reportes y analizar la carga de distribución de cada zona", así que es un
  **registro histórico de la jornada**: un paquete entregado **no** sale de su grupo.
- **Operación dominante:** `computeIfAbsent(destino, ...).add(paquete)` al registrar; `get(municipio)` y recorrer por municipio al hacer el reporte.
- **Clave en `TreeMap` (y no `HashMap`):** el reporte por municipio sale en orden alfabético sin ordenar nada aparte.
  Costo O(log m) por registro en lugar de O(1); con m = 7 municipios no importa.
- **Valor `ArrayList` (y no `LinkedHashSet`)**. Se evaluó:

  | | `ArrayList` (elegido) | `LinkedHashSet` (descartado) |
  |---|---|---|
  | Orden de inserción | sí | sí |
  | Evita duplicados | no por sí solo | sí |
  | `remove(paquete)` | O(n) | O(1) |
  | `get(i)` | O(1) | no existe |
  | Memoria por elemento | 1 referencia | entrada de hash + 2 enlaces |

  - Los dos conservan el orden de inserción: ese **no** es el criterio para elegir.
  - Los duplicados ya los rechaza `registrarPaquete` con el `HashMap` de códigos **antes** de llegar al grupo.
  - La única ventaja real del `LinkedHashSet` es `remove` en O(1), y el grupo **nunca** elimina (es histórico).
  - Conclusión: el `LinkedHashSet` pagaría más memoria por una ventaja que no se usa, y perdería `get(i)`
    (útil para pasar el grupo a un recursivo o a divide y vencerás).
- **Relación con los municipios (fichas 3.3 y 3.4):** `keySet()` de este `TreeMap` ya da los municipios
  sin repetir y ordenados. El equipo **decide mantener aparte** el `LinkedHashSet` (sin repetir, orden de
  aparición) y el `TreeSet` (alfabético) para que cada necesidad del enunciado tenga su estructura explícita.
  Se sacrifica memoria y hay que mantenerlas sincronizadas al registrar (todo ocurre en `registrarPaquete`).
  Pregunta probable: *"¿por qué no usaste `keySet()`?"*.

### 3.6 `colaLlegada`: `Queue<Paquete>` → `ArrayDeque` ✅ (necesidad 1)
- **Necesidad:** atender estrictamente en orden de llegada (FIFO).
- **Operación dominante:** `offer` al final, `poll` al frente.
- **Costo:** O(1) amortizado (arreglo circular).
- **Alternativas descartadas:** `ArrayList.remove(0)` es O(n) porque desplaza todo; `LinkedList` también es O(1), pero usa un nodo por elemento (más memoria y peor localidad de caché).
- **Por qué la variable es de tipo `Queue`:** solo se usan operaciones de cola; el tipo de la interfaz documenta la intención.

### 3.7 `colaPrioridad`: `PriorityQueue` + `ComparadorDespacho` ✅/⚠️ (necesidad 5)
- **Necesidad:** obtener **repetidamente** el siguiente según prioridad → tiempo → código.
- **Operación dominante:** `poll` y `peek`.
- **Costo:** `offer`/`poll` O(log n); `peek` O(1) (montículo binario).
- **Alternativas descartadas:**
  - Ordenar una lista cada vez: O(n log n) por llegada.
  - `TreeSet` con el comparador: también O(log n) y sí recorre en orden, pero es más pesado y depende de que el comparador nunca devuelva 0 para paquetes distintos (aquí se cumple por el desempate final por código).
- **Ojo:** el iterador de `PriorityQueue` **no** recorre en orden de prioridad; solo garantiza el tope.
- **⚠️ Decisión a defender (dos colas):** cada paquete pendiente está en `colaLlegada` **y** en `colaPrioridad`. Al despachar por una se hace `remove(objeto)` en la otra, que cuesta **O(n)**.
  - Ventaja: simple y siempre consistente.
  - Alternativa: eliminación perezosa con un `Set<String>` de pendientes (al hacer `poll`, descartar los que ya no estén). Despachar queda en O(1) u O(log n) a cambio de más lógica.

### 3.8 `repartidores`: `Map<String, Repartidor>` → `LinkedHashMap` ✅
- **Necesidad:** identificación única + consulta.
- **Costo:** `containsKey`/`get` O(1).
- **Por qué `Linked`:** `buscarRepartidorDisponible` recorre en orden de registro, así siempre se elige el mismo repartidor (comportamiento determinista y reproducible en la sustentación).
- **Costo oculto:** buscar uno disponible por zona es O(r). Alternativa: `Map<zona, Queue<Repartidor>>` en O(1); no se justifica con 8 repartidores.

### 3.9 `asignaciones`: `Map<String, Repartidor>` → `HashMap` ✅
- **Necesidad:** al registrar la entrega, saber en O(1) a qué repartidor liberar.
- **Límite que causó la H1:** como `registrarEntrega` hace `remove`, este map solo sabe "asignado **ahora**",
  no "ya entregado". Por eso **no** sirve para validar el ciclo de vida; esa tarea pasó a `estados` (ficha 3.11).

### 3.10 `enEsperaDeRepartidor`: `Queue<Paquete>` → `ArrayDeque` ✅ (H2 corregido)
- **Necesidad:** no perder un paquete despachado cuando no hay repartidor libre en su zona.
- **Costo:** `offer` O(1).
- **Reintento:** `reintentarEnEspera()` recorre la cola en orden FIFO y asigna los que ya tienen repartidor libre.
  Se llama explícitamente después de una entrega o de `habilitarRepartidor(id)`.
  - Recorre con **`Iterator` + `it.remove()`**: un `for` por índice se saltaría elementos, y un `remove` de la cola
    dentro de un for-each lanzaría `ConcurrentModificationException`.
  - Costo O(e · r), con e = en espera y r = repartidores.
- **Por qué explícito y no automático en cada entrega:** es más simple de seguir en una traza. Hacerlo automático
  (llamarlo al final de `registrarEntrega`) es una modificación posible en la sustentación.

### 3.11 `estados`: `Map<String, EstadoPaquete>` → `HashMap` ✅ (nueva, corrige H1)
- **Necesidad:** saber en O(1) en qué parte del ciclo de vida va un paquete:
  `PENDIENTE → DESPACHADO → ASIGNADO → ENTREGADO` (enum `modelo/EstadoPaquete.java`).
- **Operación dominante:** `get(codigo)` para validar y `put(codigo, nuevoEstado)` en cada transición.
- **Costo:** O(1) esperado; una entrada por paquete.
- **Alternativas descartadas:**
  - `Set<String> entregados` + `Set<String> pendientes`: funciona, pero son varias estructuras que sincronizar
    y el estado "despachado" quedaría implícito (ni en uno ni en otro).
  - Atributo `estado` dentro de `Paquete`: obligaría a quitar el `final` y a dar un setter público; cualquiera
    podría saltarse las reglas. El estado pertenece a la **jornada** (el centro), no al paquete.
  - Buscar en el historial: es la lista propia, O(n).
- **Dónde se valida:** el método privado `exigirEstado(paquete, esperado)`, que usan `asignar`,
  `asignarAutomaticamente` y `registrarEntrega`. También rechaza paquetes que no están registrados en el centro.
- **Se sacrifica:** una entrada más por paquete en memoria.

### 3.12 `historial`: `Coleccion<Paquete>` → `ListaEnlazada` propia ✅ (necesidad 6, obligatoria)
- **Necesidad:** orden real de entregas, con estructura propia y sin colecciones de Java por dentro.
- **Operación dominante:** `agregar` al final y recorrer.
- **Costo:** `agregar` O(1) **gracias a la referencia `cola`** (sin ella sería O(n)); recorrer con el iterador O(n).
- **Por qué la variable es de tipo `Coleccion` y no `ListaEnlazada`:** se programa contra el contrato; afuera solo se ve agregar/eliminar/obtener/recorrer, nunca nodos.
- **No usar** `for (i...) obtener(i)`: cada `obtener` camina i nodos → O(n²).

### Resumen de colecciones
| Campo | Interfaz | Implementación | Operación dominante | Costo | Estado |
|---|---|---|---|---|---|
| ordenRegistro | List | ArrayList | add, get(i) | O(1) | ✅ |
| paquetesPorCodigo | Map | HashMap | get, containsKey | O(1) esp. | ✅ |
| municipios | Set | LinkedHashSet | add | O(1) esp. | ✅ redundancia aceptada y justificada |
| municipios ordenados | Set | TreeSet (bajo demanda) | construir | O(m log m) | ✅ |
| paquetesPorMunicipio | Map | TreeMap → ArrayList | computeIfAbsent, get | O(log m) | ✅ |
| estados | Map | HashMap → EstadoPaquete | get, put | O(1) esp. | ✅ nueva (H1) |
| colaLlegada | Queue | ArrayDeque | offer, poll | O(1) | ✅ |
| colaPrioridad | PriorityQueue | — | poll, peek | O(log n), O(1) | ⚠️ remove O(n) |
| repartidores | Map | LinkedHashMap | containsKey, recorrer | O(1), O(r) | ✅ |
| asignaciones | Map | HashMap | put, remove | O(1) | ✅ |
| enEsperaDeRepartidor | Queue | ArrayDeque | offer, recorrer con Iterator | O(1), O(e·r) | ✅ |
| historial | Coleccion (propia) | ListaEnlazada | agregar, iterar | O(1), O(n) | ✅ |

---

## 4. Decisiones del modelo

### `Paquete` ✅
- **`equals`/`hashCode` por código:** la identidad del negocio es el código. Lo usan `HashMap`, `ArrayDeque.remove`, `PriorityQueue.remove` y `ListaEnlazada.eliminar`.
- **`compareTo` por código:** es el orden natural que pide el enunciado. Es **consistente con `equals`** (compareTo == 0 ⇔ equals).
- **Atributos `final`:** el código no puede cambiar después de crear el paquete, así que el `hashCode` nunca cambia y el paquete no "se pierde" de su bucket (el problema que vimos con el `precio` mutable en `Producto`).
- **Validaciones en el constructor:** un `Paquete` inválido no puede existir. Las reglas viven en un solo lugar.
- **Prioridad 1–5 con 3 por defecto:** ✅ confirmado por el profesor (H5). Las constantes `PRIORIDAD_MINIMA`,
  `PRIORIDAD_MAXIMA` y `PRIORIDAD_POR_DEFECTO` evitan números mágicos: si la regla cambia, se cambia en un solo lugar.

### `Repartidor` ✅
- `equals`/`hashCode` por identificación; `disponible` es mutable pero **no** participa en el hash, así que cambiarlo es seguro.

### Comparadores ✅
- Clases con nombre en vez de lambdas sueltas: el enunciado pide que los criterios se puedan **reutilizar**.
- "De mayor a menor" se logra **invirtiendo los argumentos**: `Integer.compare(b, a)`.
- `Double.compare` en el peso: restar doubles y castear a int daría resultados incorrectos (0.3 → 0).
- `ComparadorDespacho` **compone** los otros con `thenComparing`: no repite lógica, y el último desempate (código único) hace que nunca haya un empate real.
- Ordenar con un `Comparator` se hace sobre una **copia** (`ordenarPor`): no altera ni el orden natural ni el de registro.

---

## 5. Decisiones de la lista enlazada propia

| Decisión | Por qué | Costo / consecuencia |
|---|---|---|
| Guardar `cola` | `agregar` al final en O(1) | hay que moverla al eliminar el último y ponerla en `null` al vaciar |
| Guardar `tamanio` | `tamanio()` en O(1); `estaVacia()` usa `tamanio == 0` | hay que actualizarlo en cada agregar/eliminar |
| `Nodo` `private static` | afuera nadie toca nodos; `static` porque no necesita acceder a la lista | — |
| `IteradorLista` interna **no** static | necesita leer `cabeza` de la lista que lo creó | cada `iterator()` crea un recorrido independiente |
| `next()`: validar → guardar → avanzar → devolver | si se avanza antes de guardar, se salta un elemento | lanza `NoSuchElementException` |
| `Objects.equals` en `eliminar` | compara con `equals` (no con `==`) y tolera `null` | O(n) para buscar |
| Detenerse en el **anterior** para eliminar | en lista simple no hay enlace hacia atrás | por eso `eliminar` lleva `anterior` y `actual` |

**Casos borde cubiertos por `PruebasListaEnlazada`:** vacía, un elemento (y agregar después), primero, intermedio, último (y agregar después), inexistente, `obtener` fuera de rango, iterador vacío, genérico con `Integer`.

---

## 6. Decisiones de los algoritmos

| Algoritmo | Decisión | Justificación |
|---|---|---|
| `Recursivos` | Estilo "acumulador al regresar" con `indice + 1` | Caso base `indice == size` → 0. Funciona con la lista vacía. Pila O(n). |
| `Recursivos` | Recibe `List` (en la práctica, `ArrayList`) | `get(i)` en O(1). Con `LinkedList` sería O(n²). |
| `Recursivos` | Método público + método privado con `indice` | quien llama no tiene que saber que hay un índice |
| `BusquedaBinaria` | **No** valida que la lista esté ordenada en cada búsqueda | comprobarlo cuesta O(n) y anularía la ventaja de O(log n); existe `estaOrdenadaPorCodigo` para verificarlo una vez |
| `BusquedaBinaria` | `medio = inicio + (fin - inicio) / 2` | evita el desbordamiento de `(inicio + fin)` con listas enormes |
| `DivideYVenceras` | Caso base `inicio == fin`; la lista vacía se rechaza en el método público | evita el error de "falta el caso base para el arreglo vacío" |
| `DivideYVenceras` | Guarda las dos mitades en variables y luego combina | se ven las etapas: dividir, izquierda, derecha, combinar |
| `DivideYVenceras` | T(n) = 2T(n/2) + O(1) → O(n) tiempo, O(log n) pila | diferencia con la recursión lineal: la profundidad, no el tiempo |

---

## 7. Hallazgos (registro de revisión)

| # | Severidad | Dónde | Qué pasa | Causa raíz | Estado |
|---|---|---|---|---|---|
| H1 | ✅ Corregido | `CentroDistribucion.asignar` y `registrarEntrega` | Se podía asignar un paquete **ya entregado** (quedaba duplicado en el historial), uno **todavía pendiente** o uno **no registrado** (metía `null` al historial). **Reproducido:** el historial quedó `PQ731 -> PQ731 -> PQ318 -> null` | `asignaciones.remove` borraba la evidencia; ninguna estructura registraba el estado | `Map<String, EstadoPaquete> estados` + `exigirEstado(...)` (ficha 3.12). La demo, sección 7, muestra los 4 rechazos. Caso real para D.2 |
| H2 | ✅ Corregido | `enEsperaDeRepartidor` | Los paquetes en espera (PQ407) nunca se volvían a atender | Faltaba el paso de reintento | `reintentarEnEspera()` (Iterator + `it.remove()`) y `habilitarRepartidor(id)`; en la demo PQ407 se asigna a R005 y se entrega |
| H3 | ✅ Corregido | zona/destino, `Recursivos` | Zona y destino se comparaban con `equalsIgnoreCase`, pero el `Set` y los `Map` distinguen mayúsculas: "armenia" ≠ "Armenia" | Cada parte usaba su propia noción de igualdad | Todo usa `equals` exacto: la misma igualdad que el `Set` y los `Map`. Regla: los municipios se escriben siempre igual |
| H4 | ✅ Corregido | `MainParcial.despacharAsignarYEntregar` | Si se despachaba sin pendientes, `null` → `NullPointerException` | No se validaba el `null` | Se valida el `null`; además `asignarAutomaticamente(null)` ahora lanza `IllegalArgumentException` con mensaje claro |
| H5 | ✅ Enunciado | `Paquete.java:22-24` | El enunciado dice 0–10 con 5 por defecto y también "entre 1 y 5" | Contradicción del enunciado | **Cerrado, confirmado por el profesor:** rango de 1 a 5, valor por defecto 3. El código ya está así |
| H6 | ✅ Diseño | `CentroDistribucion.java:49` | Municipios duplicados entre el `Set` y las claves del map; el map era `HashMap` | Cada necesidad tenía su estructura sin decidir la del reporte | **Decidido:** `TreeMap<String, List<Paquete>>` con `ArrayList`; se mantienen `LinkedHashSet` y `TreeSet` aparte, con justificación (ficha 3.5). Ya implementado en `CentroDistribucion` |
| H7 | ✅ Decisión simple | `despacharPor...` | `remove(objeto)` en la otra cola: O(n) | Un mismo pendiente vive en dos colas | **Se mantiene** (es la opción simple). Defensa: con 30 paquetes no importa, siempre son consistentes y cada línea se explica sola. Con 500.000 se cambiaría a eliminación perezosa (ficha 3.7) |
| H8 | ✅ Corregido | `PruebasListaEnlazada` | La Parte C decía que se probó con `Paquete`, y no era así | — | Prueba `conPaquetes()`: también demuestra que `eliminar` usa el `equals` por código. 26 OK, 0 FALLO |
| H11 | ⚠️ Límite conocido | `Repartidor.setDisponible` es público | Cualquiera puede liberar a un repartidor sin pasar por el centro | El modelo expone el setter | Desde el centro solo se usa `habilitarRepartidor`, que valida. Saber explicarlo; no se corrige para no complicar el modelo |
| H9 | ✅ Atendido | Informe, Parte D | La bitácora y D.2 los redactó la misma IA | El enunciado pide casos **reales** | Bitácora reescrita en el informe como autor único, con las consultas reales; D.2 usa H1. Quedan filas "por confirmar" |
| H10 | ⚠️ Entrega | repo | Parcial sin commitear; el informe vive en un Claude Doc (piden PDF) | — | Autor único (Brahian): commits progresivos + exportar el informe a PDF |

**No requerido por el parcial, pero probable en la sustentación** ("modificar en vivo"): `eliminar(posicion)`, `eliminarPrimero`, `eliminarUltimo`, `agregarInicio`, `agregar(valor, posicion)`, `modificarNodo`, `obtenerPosicionNodo`, `borrarLista`.

---

## 8. Supuestos sobre el enunciado

- **Prioridad:** de 1 a 5, con 3 por defecto (confirmado por el profesor). El 3 es el punto medio del rango:
  un paquete sin prioridad explícita no se adelanta ni se atrasa frente a los demás.
- **El map por municipio es histórico:** sirve para reportes de carga por zona, así que los paquetes entregados siguen en su grupo.
- **Un repartidor = una asignación a la vez:** queda no disponible al asignar y vuelve a estar disponible al entregar.
- **Zona = municipio**, comparadas con `equals` exacto (la misma igualdad que el `Set` y los `Map`).
- **Solo un paquete DESPACHADO se puede asignar** y solo uno ASIGNADO se puede entregar (`EstadoPaquete`).
- **Un repartidor no disponible vuelve al servicio** con `habilitarRepartidor`, y la espera se atiende con `reintentarEnEspera`.
- **Municipios sin tilde** ("Calarca") para evitar problemas de codificación en la consola de Windows.
- **Despachar ≠ entregar:** despachar saca el paquete de pendientes; entregar lo pasa al historial.

## 9. Preguntas probables en la sustentación

1. ¿Por qué `LinkedHashSet` y no `HashSet` o `TreeSet` para los municipios? ¿Qué sacrificas?
2. ¿Por qué hay un `ArrayList` **y** una `ArrayDeque` para los mismos paquetes?
3. ¿Qué pasa si `compareTo` de `Paquete` usara el peso? (Rompe la consistencia con `equals`; un `TreeSet` trataría como iguales dos paquetes con el mismo peso.)
4. ¿Dónde está el `Iterable` si `ListaEnlazada` no lo implementa? (Lo hereda a través de `Coleccion`.)
5. ¿Por qué recorrer el historial con `obtener(i)` es O(n²)?
6. ¿Qué pasa si se busca con búsqueda binaria sobre `getPaquetesEnOrdenRegistro()`? (La precondición se viola: puede decir que no existe un paquete que sí está; la demo lo muestra con PQ034.)
7. ¿Por qué `mayorPeso` es divide y vencerás y `pesoTotalPorMunicipio` no lo es?
8. ¿Qué pasaría con 500.000 paquetes en los recursivos lineales? (`StackOverflowError`, porque la pila crece O(n).)
