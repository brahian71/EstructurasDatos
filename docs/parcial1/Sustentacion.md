# Guía de sustentación: Parcial 1 (QuindíoExpress)

> Para estudiar y practicar antes de sustentar. La nota final se multiplica por el factor de
> sustentación: hay que poder **explicar, ejecutar y modificar** el código en vivo.
>
> Las justificaciones largas de cada estructura están en `Mapa.md` (fichas de decisión).
> Aquí va lo mínimo para defender cada paquete, ejercicios de modificación con solución y
> entradas nuevas para practicar pilas a mano.

---

## 0. Plan de 3 días

| Día | Estudiar | Practicar | Entregable |
|---|---|---|---|
| 1 | `modelo/`, `comparadores/`, `estructuras/`; Parte A del informe | Ejercicios 1 a 4 (lista propia) | — |
| 2 | `servicio/CentroDistribucion`, `algoritmos/`; Parte C | Ejercicios 5 a 8; las 3 pilas de la sección 3 a mano | Capturas del Excel en el informe |
| 3 | Parte D (bitácora y caso D.2) | Ensayo cronometrado (sección 4) | Datos, PDF, commits y push |

---

## 1. Lo mínimo por paquete

### `modelo/`
- **Qué hace.** `Paquete` y `Repartidor` validan en el constructor: un objeto inválido no puede existir. `EstadoPaquete` es el ciclo de vida.
- **Decisión clave.** `equals`, `hashCode` y `compareTo` de `Paquete` usan **el código**. Son consistentes: `compareTo == 0` si y solo si `equals`. Los atributos son `final`, así que el `hashCode` nunca cambia y el paquete no se "pierde" dentro de un `HashMap`.
- **Pregunta probable.** *¿Qué pasa si `compareTo` usara el peso?* Un `TreeSet` trataría como iguales dos paquetes distintos con el mismo peso y descartaría uno.

### `comparadores/`
- **Qué hace.** Criterios alternativos: prioridad (mayor a menor), peso (mayor a menor), tiempo (menor a mayor) y despacho (prioridad → tiempo → código).
- **Decisión clave.** "Mayor a menor" se logra invirtiendo los argumentos: `Integer.compare(b, a)`. `ComparadorDespacho` compone los otros con `thenComparing`; el último desempate es el código, que es único, así que nunca hay empate.
- **Pregunta probable.** *¿Por qué `Double.compare` y no `(int) (a - b)`?* Restar 8.4 − 8.1 da 0.3, y al convertir a `int` queda 0: diría que son iguales.

### `estructuras/`
- **Qué hace.** `Coleccion<T>` es el contrato (qué operaciones hay); `ListaEnlazada<T>` es la implementación con nodos (cómo se hacen).
- **Decisión clave.** Se guarda `cola` para que `agregar` sea O(1), y `tamanio` para que `tamanio()` sea O(1). `Nodo` e `IteradorLista` son privados: desde afuera solo se recorre con el iterador.
- **Iterable vs Iterator.** `Iterable` es la lista ("se puede recorrer"); `Iterator` es el objeto que recorre y recuerda el nodo actual. `ListaEnlazada` no dice `implements Iterable`: lo hereda de `Coleccion<T> extends Iterable<T>`.
- **Pregunta probable.** *¿Por qué recorrer con `obtener(i)` en un `for` es O(n²)?* Cada `obtener(i)` camina i nodos desde la cabeza: 1 + 2 + … + n.

### `servicio/CentroDistribucion`
- **Qué hace.** Todas las colecciones de Java viven aquí; cada una atiende una necesidad (tabla A.1 del informe).
- **Decisión clave.** El estado de cada paquete vive en `Map<String, EstadoPaquete>` y `exigirEstado` lo valida antes de asignar o entregar. Eso corrigió el error de D.2 (historial duplicado).
- **Pregunta probable.** *¿Por qué al despachar se hace `remove` en la otra cola?* Un pendiente está en las dos colas; si sale por una, debe salir de la otra. Cuesta O(n); la alternativa es la eliminación perezosa.

### `algoritmos/`
- **Qué hace.** `Recursivos` (peso por municipio y conteo por prioridad, sin ciclos), `BusquedaBinaria` (precondición: ordenada por código) y `DivideYVencerasPaquetes` (mayor peso).
- **Decisión clave.** El método público oculta el índice; el privado lo recibe. Divide y vencerás parte en dos mitades y combina: profundidad O(log n) en lugar de O(n).
- **Pregunta probable.** *¿Por qué la búsqueda binaria no verifica que la lista esté ordenada?* Verificarlo cuesta O(n) y anularía la ventaja de O(log n).

### `DatosIniciales`, `MainParcial`, `PruebasListaEnlazada`
- 30 paquetes con códigos no consecutivos, 7 municipios, 8 repartidores (R005 no disponible, para probar la regla).
- `MainParcial` tiene una sección por requisito; `PruebasListaEnlazada` cubre los casos borde (26 OK).

---

## 2. Ejercicios de modificación en vivo

Intenta cada uno **sin mirar la solución**, compila y ejecuta. Tiempo objetivo: 10 minutos cada uno.

### Ejercicio 1. `agregarInicio` en la lista propia
**Pedido:** agregar un elemento al principio de la lista en O(1).
**Dónde:** `estructuras/ListaEnlazada.java` (y opcionalmente en `Coleccion`).

<details><summary>Solución</summary>

```java
public void agregarInicio(T elemento) {
    Nodo<T> nuevo = new Nodo<>(elemento);
    nuevo.siguiente = cabeza;
    cabeza = nuevo;
    if (cola == null) {
        // la lista estaba vacia: el nuevo nodo tambien es el ultimo
        cola = nuevo;
    }
    tamanio++;
}
```
**Trampa:** si la lista estaba vacía y no se actualiza `cola`, el siguiente `agregar` hace `cola.siguiente` sobre `null` y lanza `NullPointerException`.
</details>

### Ejercicio 2. Eliminar por posición
**Pedido:** eliminar el elemento de la posición `i` y devolverlo.
**Dónde:** `ListaEnlazada.java`.

<details><summary>Solución</summary>

```java
public T eliminarEn(int posicion) {
    if (posicion < 0 || posicion >= tamanio) {
        throw new IndexOutOfBoundsException("Posicion " + posicion + " fuera de rango [0, " + tamanio + ")");
    }
    T valor;
    if (posicion == 0) {
        valor = cabeza.valor;
        cabeza = cabeza.siguiente;
        if (cabeza == null) {
            cola = null;
        }
    } else {
        // en lista simple hay que detenerse en el ANTERIOR
        Nodo<T> anterior = cabeza;
        for (int i = 0; i < posicion - 1; i++) {
            anterior = anterior.siguiente;
        }
        Nodo<T> actual = anterior.siguiente;
        valor = actual.valor;
        anterior.siguiente = actual.siguiente;
        if (actual == cola) {
            cola = anterior;
        }
    }
    tamanio--;
    return valor;
}
```
**Trampa del nombre:** si se llamara `eliminar(int posicion)`, con una `ListaEnlazada<Integer>` la llamada `eliminar(10)` escogería esta versión (por posición) y no `eliminar(T)` (por valor). Por eso se llama `eliminarEn`.
</details>

### Ejercicio 3. `eliminarPrimero` y `eliminarUltimo`
**Pedido:** ambos devuelven el elemento eliminado; si la lista está vacía, lanzan `NoSuchElementException`.

<details><summary>Solución</summary>

```java
public T eliminarPrimero() {
    if (estaVacia()) {
        throw new NoSuchElementException("La lista esta vacia");
    }
    return eliminarEn(0);
}

public T eliminarUltimo() {
    if (estaVacia()) {
        throw new NoSuchElementException("La lista esta vacia");
    }
    return eliminarEn(tamanio - 1);
}
```
**Pregunta probable:** *¿Por qué `eliminarUltimo` es O(n) si tenemos `cola`?* Porque hay que mover `cola` al anterior, y en una lista simple no hay enlace hacia atrás: hay que caminar desde la cabeza. Con una lista doble sería O(1).
</details>

### Ejercicio 4. `contiene` recursivo
**Pedido:** saber si un elemento está en la lista, **sin ciclos**.

<details><summary>Solución</summary>

```java
public boolean contiene(T elemento) {
    return contiene(cabeza, elemento);
}

private boolean contiene(Nodo<T> nodo, T elemento) {
    if (nodo == null) {
        return false;                        // caso base: se acabo la lista
    }
    if (Objects.equals(nodo.valor, elemento)) {
        return true;                         // caso base: lo encontro
    }
    return contiene(nodo.siguiente, elemento); // avanza al siguiente nodo
}
```
El método privado recibe el nodo: desde afuera nadie ve nodos, igual que con el iterador. Tiempo O(n), pila O(n).
</details>

### Ejercicio 5. Reintento automático de la cola de espera
**Pedido:** que al registrar una entrega se intente asignar de inmediato lo que esté esperando repartidor.
**Dónde:** `servicio/CentroDistribucion.java`, al final de `registrarEntrega`.

<details><summary>Solución</summary>

```java
public void registrarEntrega(String codigoPaquete) {
    // ... codigo actual sin cambios ...
    repartidor.setDisponible(true);
    reintentarEnEspera();   // el repartidor recien liberado puede tomar un paquete en espera
}
```
**Explicación:** el repartidor queda libre justo en ese momento, así que es el mejor punto para reintentar. Lo mismo se puede hacer al final de `habilitarRepartidor`. En la demo actual nada cambia: PQ407 espera a R005, que solo vuelve con `habilitarRepartidor`.
</details>

### Ejercicio 6. Nuevo criterio de ordenamiento: por destino
**Pedido:** ordenar los paquetes por municipio (alfabético) y, dentro del mismo municipio, por código.
**Dónde:** nueva clase `comparadores/ComparadorDestino.java`.

<details><summary>Solución</summary>

```java
package parcial.comparadores;

import parcial.modelo.Paquete;

import java.util.Comparator;

/** Destino alfabetico; si empatan, codigo (orden natural). */
public class ComparadorDestino implements Comparator<Paquete> {

    @Override
    public int compare(Paquete a, Paquete b) {
        int porDestino = a.getDestino().compareTo(b.getDestino());
        return porDestino != 0 ? porDestino : a.compareTo(b);
    }
}
```
Uso: `centro.ordenarPor(new ComparadorDestino())`. El orden natural de `Paquete` no cambia: se ordena una copia.
</details>

### Ejercicio 7. Peso total por municipio con divide y vencerás
**Pedido:** la misma operación de `Recursivos.pesoTotalPorMunicipio`, pero dividiendo en mitades.
**Dónde:** `algoritmos/DivideYVencerasPaquetes.java`.

<details><summary>Solución</summary>

```java
public static double pesoTotalDyV(List<Paquete> paquetes, String municipio) {
    if (paquetes.isEmpty()) {
        return 0.0;
    }
    return pesoTotalDyV(paquetes, municipio, 0, paquetes.size() - 1);
}

private static double pesoTotalDyV(List<Paquete> paquetes, String municipio, int inicio, int fin) {
    // 1. caso base: un solo paquete
    if (inicio == fin) {
        Paquete actual = paquetes.get(inicio);
        return actual.getDestino().equals(municipio) ? actual.getPeso() : 0.0;
    }
    // 2. division
    int medio = inicio + (fin - inicio) / 2;
    // 3 y 4. subproblemas
    double izquierda = pesoTotalDyV(paquetes, municipio, inicio, medio);
    double derecha = pesoTotalDyV(paquetes, municipio, medio + 1, fin);
    // 5. combinacion
    return izquierda + derecha;
}
```
Mismo O(n) en tiempo, pero la pila baja de O(n) a O(log n): con 500.000 paquetes ya no habría `StackOverflowError`.
</details>

### Ejercicio 8. Búsqueda binaria que devuelva el paquete
**Pedido:** en vez de la posición, devolver el `Paquete` (o `null` si no existe).
**Dónde:** `algoritmos/BusquedaBinaria.java`.

<details><summary>Solución</summary>

```java
public static Paquete buscarPaquete(List<Paquete> ordenadaPorCodigo, String codigo) {
    int posicion = buscarIterativa(ordenadaPorCodigo, codigo);
    return posicion == -1 ? null : ordenadaPorCodigo.get(posicion);
}
```
Se reutiliza `buscarIterativa` en vez de copiarla. La precondición sigue siendo la misma: lista ordenada por código.
</details>

---

## 3. Pilas para practicar a mano

Hazlas en papel con el formato del Excel (una fila por llamada y una columna por instante de la pila) y compara con la respuesta.

### 3.1. `pesoTotalPorMunicipio`
Datos: PQ210 Salento 2.0 · PQ330 Armenia 5.5 · PQ450 Salento 1.5. Consulta: **Salento**.

<details><summary>Respuesta</summary>

| Llamada | Paquete | Aporte | Retorna |
|---|---|---|---|
| `pesoTotal(3)` | — caso base | — | 0.0 |
| `pesoTotal(2)` | PQ450 Salento | 1.5 | 1.5 + 0.0 = 1.5 |
| `pesoTotal(1)` | PQ330 Armenia | 0.0 | 0.0 + 1.5 = 1.5 |
| `pesoTotal(0)` | PQ210 Salento | 2.0 | 2.0 + 1.5 = **3.5** |

4 llamadas; 3 pendientes al llegar al caso base; retornos 3, 2, 1, 0.
</details>

### 3.2. `contarPrioridadMinima`
Datos: prioridades 3, 5, 1, 4. Mínima: **3**.

<details><summary>Respuesta</summary>

| Llamada | Prioridad | Aporte | Retorna |
|---|---|---|---|
| `contar(4)` | — caso base | — | 0 |
| `contar(3)` | 4 | 1 | 1 + 0 = 1 |
| `contar(2)` | 1 | 0 | 0 + 1 = 1 |
| `contar(1)` | 5 | 1 | 1 + 1 = 2 |
| `contar(0)` | 3 | 1 | 1 + 2 = **3** |

5 llamadas; 4 pendientes al llegar al caso base.
</details>

### 3.3. `mayorPeso` (divide y vencerás)
Datos: pesos 6.0, 2.5, 9.1, 4.0, 7.7 (n = 5).

<details><summary>Respuesta</summary>

```
                 mayor(0,4) -> 9.1
               /                 \
       mayor(0,2) -> 9.1      mayor(3,4) -> 7.7
        /          \            /          \
 mayor(0,1)->6.0  mayor(2,2)  mayor(3,3)  mayor(4,4)
   /       \        = 9.1      = 4.0       = 7.7
mayor(0,0) mayor(1,1)
  = 6.0      = 2.5
```
Orden: (0,4), (0,2), (0,1), (0,0), (1,1), combina 6.0, (2,2), combina 9.1, (3,4), (3,3), (4,4), combina 7.7, combina **9.1**.
9 llamadas (2n − 1); profundidad máxima 4 marcos sobre `main`.
</details>

### 3.4. Búsqueda binaria
Lista: PQ105, PQ214, PQ318, PQ421, PQ567, PQ731, PQ842, PQ942. Buscar **PQ105** y **PQ950**.

<details><summary>Respuesta</summary>

PQ105: (0,7) medio 3 PQ421 > → fin 2; (0,2) medio 1 PQ214 > → fin 0; (0,0) medio 0 PQ105 → **posición 0** en 3 pasos.

PQ950: (0,7) medio 3 PQ421 < → inicio 4; (4,7) medio 5 PQ731 < → inicio 6; (6,7) medio 6 PQ842 < → inicio 7; (7,7) medio 7 PQ942 < → inicio 8; inicio > fin → **-1** en 4 comparaciones.
</details>

---

## 4. Ensayo de sustentación (cronometrado)

1. **Ejecutar (2 min).** Desde la raíz del proyecto:
   ```
   javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
   java -cp out parcial.MainParcial
   java -cp out parcial.PruebasListaEnlazada
   ```
2. **Explicar (5 min).** Elegir al azar una sección de `MainParcial` y contar qué estructura o algoritmo usa, por qué y cuánto cuesta.
3. **Modificar (10 min).** Elegir al azar un ejercicio de la sección 2, hacerlo sin mirar, compilar y mostrar que funciona.
4. **Pila (5 min).** Hacer en papel una de las pilas de la sección 3 con otros datos.
5. **IA (2 min).** Contar el caso D.2 con tus palabras: qué propuso la IA, cómo detectaste el error y qué cambiaste.
