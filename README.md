# Estructuras de Datos

Proyecto de la materia. Java 21, IntelliJ IDEA.

## Como correr

Opcion 1: menu central.

```
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
java -cp out Main
```

Opcion 2: en IntelliJ, clic derecho sobre cualquier clase -> Run. Todas tienen
su propio `main()`, asi que se pueden correr sueltas.

## Estructura

```
src/
  Main.java                          menu central (opcional)
  modelo/
    Producto.java                    entidad de dominio, la usan varios temas
  recursividad/
    AcumuladorAlRegresar.java        estilo 1: factorial, digitos, matrioska, sumarArreglo
    AcumuladorComoParametro.java     estilo 2: mayor, segundo mayor
    TareaRecursiva.java              estilo 2: sumar, sumarMayoresN, contarMayoresN
    DivideYVenceras.java             estilo 3: sumar
    ArreglosRecursivos.java          PENDIENTE: invertir, buscar, estaOrdenado
    MultiplesLlamadas.java           PENDIENTE: Fibonacci, memoizado, seguro, Hanoi
  colecciones/
    IteratorSeguro.java              el bug de remove(i) vs Iterator.remove()
    ClaseArrays.java                 PENDIENTE: ordenamientos, busqueda binaria, asList
    ClaseCollections.java            PENDIENTE: sort, frequency, disjoint, Map
  sets/
    TipoHashSet.java                 HashSet vs LinkedHashSet vs TreeSet, == vs equals
  genericos/
    CarroCompraGenerics.java         carrito generico + Iterable
    IteradorCarrito.java             Iterator<T> hecho desde cero
    IteradorCarritoImpares.java      variante: posiciones impares
```

Un paquete por tema, dos niveles como maximo. Lo que se comparte entre temas
(`Producto`) va en `modelo`. Lo pendiente son stubs con el enunciado y las
firmas: no tienen solucion a proposito.

## Estado

| Tema | Estado |
|---|---|
| 1. Recursividad, 3 estilos | completo |
| 1. Recursion sobre arreglos | pendiente |
| 1. Multiples llamadas (Fibonacci, Hanoi) | pendiente |
| 2. Colecciones: iterator seguro | completo |
| 2. Arrays / Collections | pendiente |
| 3. Sets: HashSet/LinkedHashSet/TreeSet | completo |
| 4. Genericos + iterador propio | completo |

## Errores que ya se corrigieron

Quedan documentados en el codigo, con el porque, en el `main` del archivo donde
viven.

- `TipoHashSet.eliminar` usaba `==` en vez de `.equals()`. Comparaba
  referencias, no texto. Solo funcionaba porque el dato era un literal
  (string interning); con un `Scanner` no borraba nada.
- `IteratorSeguro.eliminarA` hacia `lista.remove(i)` dentro de un `for` con
  indice, que se saltaba elementos. Se conserva el metodo como contraejemplo,
  comentado en el main.
- `TareaRecursiva.sumaNumeros` hacia `suma = sumaNumeros(..., suma += arreglo[i])`:
  actualizaba la misma variable dos veces. Ahora solo una.
- `DivideYVenceras.sumarDyV` fallaba con `ArrayIndexOutOfBoundsException` en
  arreglos vacios. Falta el caso base. Sigue documentado en el metodo.
- `CarroCompraGenerics` usaba un `HashSet` para una demo de "posiciones
  impares", pero el `HashSet` no garantiza orden, asi que las posiciones no
  significaban nada. Ahora la demo de posiciones usa `LinkedHashSet`.

## Nota sobre sustentaciones

Las evaluaciones son mini proyectos sustentados. Lo que mas rinde es poder
explicar el *por que*: causa raiz de un error, no solo el fix. Los comentarios
de este repo estan escritos pensando en eso: cada archivo explica la trampa
que tiene, no solo lo que hace.

## Git

`faa9e7c` es el estado original del proyecto, antes de reorganizar.
