# Parte C. Análisis algorítmico y pruebas (borrador)

> Las trazas de este documento se pueden reproducir ejecutando `parcial.MainParcial`
> (secciones 8, 9 y 10). Antes de la sustentación, háganlas **a mano** en papel:
> en la sustentación pueden pedir otra entrada.

## C.1. Pila de llamadas: peso total por municipio (recursivo)

Método: `Recursivos.pesoTotalPorMunicipio(paquetes, municipio, indice)`

```java
if (indice == paquetes.size()) return 0.0;                 // caso base
aporte = (destino == municipio) ? peso : 0.0;
return aporte + pesoTotalPorMunicipio(paquetes, municipio, indice + 1); // caso recursivo
```

**Datos:** PQ101 Armenia 4.5, PQ203 Salento 3.0, PQ415 Armenia 7.2, PQ730 Armenia 2.3. Consulta: `"Armenia"`.

| Elemento | Respuesta |
|---|---|
| Caso base | `indice == paquetes.size()` (índice 4): no quedan paquetes, retorna 0.0 |
| Caso recursivo | `aporte + pesoTotal(indice + 1)` |
| Cómo avanza al caso base | `indice` aumenta en 1 en cada llamada; la lista no cambia de tamaño |
| Parámetros de cada llamada | la misma lista y el mismo municipio; solo cambia `indice` (0, 1, 2, 3, 4) |
| Llamadas pendientes | al llegar al caso base quedan pendientes 4 llamadas (índices 0 a 3), cada una esperando sumar su aporte |
| Cuándo se alcanza el caso base | en la 5.ª llamada, `indice = 4` |
| Orden de los retornos | inverso al de las llamadas: 4, 3, 2, 1, 0 |

**Fase de apilado (llamadas):**

| Paso | Llamada | Paquete evaluado | Aporte | Pila (tope a la derecha) |
|---|---|---|---|---|
| 1 | `pesoTotal(0)` | PQ101 Armenia | 4.5 | [p(0)] |
| 2 | `pesoTotal(1)` | PQ203 Salento | 0.0 | [p(0), p(1)] |
| 3 | `pesoTotal(2)` | PQ415 Armenia | 7.2 | [p(0), p(1), p(2)] |
| 4 | `pesoTotal(3)` | PQ730 Armenia | 2.3 | [p(0), p(1), p(2), p(3)] |
| 5 | `pesoTotal(4)` | — caso base | — | [p(0), p(1), p(2), p(3), p(4)] |

**Fase de desapilado (retornos):**

| Paso | Llamada que retorna | Operación | Valor retornado | Pila después |
|---|---|---|---|---|
| 6 | `pesoTotal(4)` | caso base | 0.0 | [p(0), p(1), p(2), p(3)] |
| 7 | `pesoTotal(3)` | 2.3 + 0.0 | 2.3 | [p(0), p(1), p(2)] |
| 8 | `pesoTotal(2)` | 7.2 + 2.3 | 9.5 | [p(0), p(1)] |
| 9 | `pesoTotal(1)` | 0.0 + 9.5 | 9.5 | [p(0)] |
| 10 | `pesoTotal(0)` | 4.5 + 9.5 | **14.0** | [] |

**Complejidad:** n + 1 llamadas → tiempo O(n). Profundidad máxima de la pila n + 1 → espacio O(n).
Con 500.000 paquetes la profundidad provocaría `StackOverflowError`: la recursión lineal no escala a ese tamaño.

## C.2. Pila de llamadas: paquetes con prioridad ≥ mínima (recursivo)

Método: `Recursivos.contarPrioridadMinima(paquetes, minima, indice)`. **Datos:** prioridades 5, 2, 4, 1, 5, 3, 4. Mínima = 4.

| Elemento | Respuesta |
|---|---|
| Caso base | `indice == size` (índice 7) → retorna 0 |
| Caso recursivo | `(prioridad >= 4 ? 1 : 0) + contar(indice + 1)` |
| Avance | `indice + 1` |
| Llamadas pendientes al llegar al caso base | 7 (índices 0 a 6) |

| Llamada | Prioridad | Aporte | Retorna (en orden de retorno ↑) |
|---|---|---|---|
| `contar(0)` | 5 | 1 | 1 + 3 = **4** (último en retornar) |
| `contar(1)` | 2 | 0 | 0 + 3 = 3 |
| `contar(2)` | 4 | 1 | 1 + 2 = 3 |
| `contar(3)` | 1 | 0 | 0 + 2 = 2 |
| `contar(4)` | 5 | 1 | 1 + 1 = 2 |
| `contar(5)` | 3 | 0 | 0 + 1 = 1 |
| `contar(6)` | 4 | 1 | 1 + 0 = 1 |
| `contar(7)` | — | caso base | 0 (primero en retornar) |

Orden de llamadas: 0 → 7. Orden de retornos: 7 → 0. Resultado: **4 paquetes**. Tiempo O(n), espacio O(n).

## C.3. Prueba de escritorio: búsqueda binaria

**Precondición:** la lista debe estar ordenada por código. Sobre el orden de registro (no ordenado), la búsqueda de `PQ034` retorna -1 aunque el paquete existe (se demuestra en `MainParcial`, sección 9).

**Datos (ejemplo del enunciado), índices 0 a 7:**
`PQ105, PQ214, PQ318, PQ421, PQ567, PQ731, PQ842, PQ942`

**Buscar PQ731:**

| Paso | inicio | fin | medio | código en medio | Comparación | Decisión |
|---|---|---|---|---|---|---|
| 1 | 0 | 7 | 3 | PQ421 | PQ421 < PQ731 | inicio = 4 |
| 2 | 4 | 7 | 5 | PQ731 | igual | **encontrado en 5** |

2 comparaciones en vez de 6 con búsqueda lineal.

**Buscar PQ500 (no existe):**

| Paso | inicio | fin | medio | código en medio | Comparación | Decisión |
|---|---|---|---|---|---|---|
| 1 | 0 | 7 | 3 | PQ421 | PQ421 < PQ500 | inicio = 4 |
| 2 | 4 | 7 | 5 | PQ731 | PQ731 > PQ500 | fin = 4 |
| 3 | 4 | 4 | 4 | PQ567 | PQ567 > PQ500 | fin = 3 |
| 4 | 4 | 3 | — | — | inicio > fin | **retorna -1** |

**Complejidad:** cada paso descarta la mitad → como máximo ⌊log₂ n⌋ + 1 comparaciones. Tiempo O(log n). Espacio O(1) iterativa, O(log n) recursiva (pila).

## C.4. Divide y vencerás: paquete de mayor peso

**Datos:** pesos 4.5, 12.0, 3.2, 18.7, 7.4, 5.1 (índices 0 a 5).

| Etapa | Dónde está en el código |
|---|---|
| Caso base | `inicio == fin` → un solo paquete, es el mayor de su rango |
| División | `medio = inicio + (fin - inicio) / 2` |
| Subproblema izquierdo | `mayorPeso(inicio, medio)` |
| Subproblema derecho | `mayorPeso(medio + 1, fin)` |
| Combinación | el más pesado entre los dos ganadores |
| Resultado final | lo que retorna `mayorPeso(0, 5)` |

**Árbol de llamadas:**

```
                        mayor(0,5) → 18.7
                     /                    \
          mayor(0,2) → 12.0           mayor(3,5) → 18.7
           /          \                 /          \
   mayor(0,1)→12.0  mayor(2,2)=3.2  mayor(3,4)→18.7  mayor(5,5)=5.1
     /       \                        /       \
mayor(0,0)  mayor(1,1)          mayor(3,3)  mayor(4,4)
  = 4.5      = 12.0               = 18.7      = 7.4
```

**Orden de ejecución (la pila resuelve primero toda la mitad izquierda):**

| # | Llamada | Acción | Retorna |
|---|---|---|---|
| 1 | mayor(0,5) | divide en [0,2] y [3,5] | (pendiente) |
| 2 | mayor(0,2) | divide en [0,1] y [2,2] | (pendiente) |
| 3 | mayor(0,1) | divide en [0,0] y [1,1] | (pendiente) |
| 4 | mayor(0,0) | caso base | 4.5 |
| 5 | mayor(1,1) | caso base | 12.0 |
| 6 | mayor(0,1) | combina 4.5 vs 12.0 | 12.0 |
| 7 | mayor(2,2) | caso base | 3.2 |
| 8 | mayor(0,2) | combina 12.0 vs 3.2 | 12.0 |
| 9 | mayor(3,5) | divide en [3,4] y [5,5] | (pendiente) |
| 10 | mayor(3,4) | divide en [3,3] y [4,4] | (pendiente) |
| 11 | mayor(3,3) | caso base | 18.7 |
| 12 | mayor(4,4) | caso base | 7.4 |
| 13 | mayor(3,4) | combina 18.7 vs 7.4 | 18.7 |
| 14 | mayor(5,5) | caso base | 5.1 |
| 15 | mayor(3,5) | combina 18.7 vs 5.1 | 18.7 |
| 16 | mayor(0,5) | combina 12.0 vs 18.7 | **18.7** |

**Complejidad:** recurrencia T(n) = 2T(n/2) + O(1) → **O(n)** tiempo (2n − 1 = 11 llamadas para n = 6). Profundidad máxima de la pila ⌈log₂ n⌉ + 1 = 4 → **O(log n)** espacio.

**¿Por qué es divide y vencerás y no solo recursión?** El recursivo de C.1 reduce el problema en 1 elemento por llamada (profundidad n). Aquí el problema se parte en dos mitades independientes que se resuelven por separado y luego se combinan (profundidad log n). En tiempo ambos son O(n), porque hay que mirar cada peso al menos una vez; la ventaja está en la profundidad de la pila.

## C.5. Resumen de complejidades

| Operación | Estructura / algoritmo | Tiempo | Espacio adicional |
|---|---|---|---|
| Registrar paquete | HashMap + ArrayList + ArrayDeque + PriorityQueue + Set + Map | O(log n) por la PriorityQueue (lo demás O(1)) | O(1) por paquete en cada estructura |
| Consultar por código | `HashMap.get` | O(1) esperado | O(1) |
| Municipios sin repetir | `LinkedHashSet` | O(1) por inserción | O(m) |
| Municipios ordenados | `new TreeSet<>(municipios)` | O(m log m) | O(m) |
| Paquetes de un municipio | `TreeMap<String, List>` | O(log m) para obtener la lista (m = municipios) | O(n) en total |
| Siguiente por llegada | `ArrayDeque.peek` / `poll` | O(1) (+ O(n) para quitarlo de la PriorityQueue) | O(1) |
| Siguiente por prioridad | `PriorityQueue.peek` / `poll` | O(1) / O(log n) (+ O(n) para quitarlo de la cola FIFO) | O(1) |
| Ordenar por un criterio | `List.sort(Comparator)` (TimSort) | O(n log n) | O(n) por la copia |
| Validar el estado de un paquete | `HashMap<String, EstadoPaquete>` | O(1) esperado | O(n) en total |
| Asignar repartidor automáticamente | recorrido de repartidores | O(r) | O(1) |
| Reintentar la cola de espera | `Iterator` sobre `ArrayDeque` + búsqueda de repartidor | O(e · r) | O(1) |
| Agregar al historial | `ListaEnlazada.agregar` | O(1) | O(1) |
| Recorrer historial | `Iterator` | O(n) | O(1) |
| Eliminar del historial | `ListaEnlazada.eliminar` | O(n) | O(1) |
| Peso total por municipio | recursivo lineal | O(n) | O(n) pila |
| Contar prioridad mínima | recursivo lineal | O(n) | O(n) pila |
| Buscar por código en lista ordenada | búsqueda binaria | O(log n) | O(1) iterativa / O(log n) recursiva |
| Paquete de mayor peso | divide y vencerás | O(n) | O(log n) pila |

## C.6. Comparación de alternativas (posibles preguntas de sustentación)

- **¿Por qué no usar búsqueda binaria en vez de HashMap?** Exige mantener la lista ordenada; registrar pasaría a O(n). El `HashMap` da O(1) esperado sin precondición de orden.
- **¿Por qué el recorrido del historial usa el iterador y no `obtener(i)`?** En una lista enlazada `obtener(i)` camina i nodos; un `for` con índice cuesta 1 + 2 + … + n = O(n²). El iterador recuerda el nodo actual: O(n).
- **¿Qué pasaría con `pesoTotalPorMunicipio` sobre un `LinkedList`?** Cada `get(indice)` sería O(n) → total O(n²).
- **¿Se podría hacer el peso total con divide y vencerás?** Sí, sumando mitades: mismo O(n) en tiempo, pero profundidad O(log n) en vez de O(n).

## C.7. Pruebas de la lista enlazada propia

Ejecutar `parcial.PruebasListaEnlazada`. Cubre los casos exigidos:

| Caso | Resultado esperado | Estado |
|---|---|---|
| Lista vacía | `estaVacia` true, `tamanio` 0, `eliminar` false | OK |
| Un único elemento | eliminar lo deja vacío; agregar después funciona (cola reiniciada) | OK |
| Eliminar el primero | la cabeza pasa al segundo | OK |
| Eliminar intermedio | el anterior enlaza con el siguiente | OK |
| Eliminar el último | la cola pasa al anterior; agregar después queda bien enlazado | OK |
| Eliminar inexistente | retorna false y el tamaño no cambia | OK |
| `obtener` fuera de rango | lanza `IndexOutOfBoundsException` | OK |
| Iterador | recorre en orden de inserción; `next()` sin elementos lanza `NoSuchElementException` | OK |
| Genericidad | funciona con `String`, `Integer` y `Paquete` (`conPaquetes`: eliminar usa el `equals` por código) | OK |
