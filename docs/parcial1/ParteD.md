# Parte D. Uso justificado de inteligencia artificial

> Espejo de la Parte D del informe (`src/parcial/Parcial 1 – QuindíoExpress Informe técnico.docx`).
> Sincronizado con el informe el 2026-10-10: 12 filas en D.1, en primera persona (autor único).
> Durante el repaso solo se **agregan o cierran filas aquí**; al final se copia la tabla completa al `.docx` una sola vez.
> El uso de IA no baja nota: se evalúa que cada respuesta se haya verificado y que se pueda explicar.

La IA se usó para generar la base del proyecto, revisarla y redactar este informe; cada resultado se verificó ejecutando el programa y con pruebas de escritorio a mano. Las filas salen del registro de revisión del repositorio (`docs/parcial1/Mapa.md`); las marcadas «por confirmar» las debe revisar el autor.

## D.1. Bitácora

| Parte | Herramienta | Qué pregunté | Por qué la consulté | Cómo verifiqué la respuesta | Qué modifiqué |
|---|---|---|---|---|---|
| A, B, C | OpenCode (modelo Claude) | Leer el enunciado, proponer un plan y generar la base: modelo, comparadores, lista propia, servicio, algoritmos, datos, demo y borradores de A y C. | Arrancar con una estructura completa y detectar inconsistencias del enunciado. | Ejecuté `MainParcial` y `PruebasListaEnlazada` y comparé con los ejemplos del enunciado (14.0 kg, 4 paquetes, 18.7 kg, orden PQ105, PQ731, PQ942, PQ420). | Reorganicé por paquetes de tema y corregí los hallazgos de las filas siguientes. _(Desde el 8 de octubre de 2026.)_ |
| A | Claude | Rango de prioridad: el enunciado dice 0–10 con 5 por defecto y también "entre 1 y 5". | El valor por defecto depende del rango y el enunciado se contradice. | Lo consulté con el profesor. | Rango 1–5 con 3 por defecto, en constantes de `Paquete`. |
| B | Claude | Revisar el ciclo de vida de un paquete en `asignar` y `registrarEntrega`. | No sabía cómo impedir que un paquete se asignara dos veces. | Reproduje el error: el historial quedó `PQ731 -> PQ731 -> PQ318 -> null`. | Enum `EstadoPaquete`, mapa `estados` y método `exigirEstado` (ver D.2). |
| B | Claude | Qué hacer con un paquete despachado sin repartidor libre en su zona. | PQ407 (Circasia) desaparecía: no estaba pendiente, ni asignado, ni en el historial. | Corrí la demo y seguí PQ407 hasta su entrega. | Cola `enEsperaDeRepartidor`, `reintentarEnEspera()` con `Iterator` + `it.remove()` y `habilitarRepartidor`. |
| B | Claude | Comparar zona y destino con `equalsIgnoreCase` o con `equals`. | El `Set` y los `Map` distinguen mayúsculas: "armenia" y "Armenia" quedaban como municipios distintos. | Prueba con un destino en minúsculas. | Todo usa `equals` exacto, la misma igualdad de las colecciones. |
| A | Claude | `HashMap` o `TreeMap`, y `ArrayList` o `LinkedHashSet`, para agrupar paquetes por municipio. | Ambos valores conservan el orden de inserción; no tenía claro el criterio. | Comparé operaciones y costos en una tabla. | `TreeMap<String, List<Paquete>>` con `ArrayList`: el grupo nunca elimina y el reporte sale alfabético. |
| C | Claude | Verificar que las pruebas de la lista cubrían `Paquete`. | El borrador de la Parte C decía que se probó con `Paquete` y no era cierto. | Revisé el archivo de pruebas. | Agregué la prueba `conPaquetes()`: 26 OK, 0 FALLO. |
| A, B, C, D | Claude Code | Generar este informe a partir del código y de los borradores de `docs/parcial1/`. | Pasar los borradores a un documento entregable. | Compilé y ejecuté el programa; las trazas del informe coinciden con la salida real. | _Por confirmar tras la revisión final._ |
| C | Claude Code | Revisar mi laboratorio de colecciones (Word, Excel de divide y vencerás y repositorio) para reutilizar el formato de pila. | No sabía qué formato de prueba de escritorio y pila esperaba el curso. | La IA señaló errores en el Excel del laboratorio: arreglo `{1,7,4,5}` en vez de `{3,4,2,6}`, `(2+3)/2 = 3` en vez de 2, y `main` llamando con 4 argumentos a un método de 3. Los comprobé a mano. | Adopté el formato del laboratorio para el parcial, sin esos errores. |
| C | Claude Code | Generar el Excel de pruebas de escritorio y pilas (`PilaLlamadas_Parcial1.xlsx`) para los tres métodos recursivos. | Representar la pila con el formato del curso para los datos del enunciado. | Las fórmulas dan 14.0, 4 y 18.7, igual que `MainParcial`; lo revisé contra mis pilas a mano. | _Por confirmar (ajustes de formato)._ |
| C | Claude Code | Qué pide exactamente la Parte C. | Confundía la tabla de colecciones (Parte A) con el análisis de complejidad (Parte C). | Lo comparé con la sección de entrega del enunciado, que lista cuatro puntos. | Reorganicé la Parte C según el enunciado: C.1 pila de llamadas, C.2 otras pruebas de escritorio (con la nueva prueba del despacho prioritario), C.3 complejidad y C.4 alternativas y respuestas argumentadas. |
| B, E | OpenCode (modelo Claude) | Repaso clase por clase para la sustentación: `Paquete` (validaciones, `final`, `equals`/`hashCode`/`compareTo` por código), `Repartidor` y `EstadoPaquete`, comparadores (orden natural frente a alternativo, inversión `(b, a)`, qué son `a`/`b`, `thenComparing`, flujo enunciado→código), lista propia (`agregar` con `cola` en O(1), `eliminar` con `anterior`/`actual`, O(n), `Iterable` frente a `Iterator`), registro en `CentroDistribucion` (`computeIfAbsent`, `TreeMap`+`ArrayList`, `estados`, dos colas), despacho (cambia a DESPACHADO y sale de ambas colas), `exigirEstado` y el estado intermedio DESPACHADO, `asignar` (orden de validaciones y ciclo de `disponible`), `reintentarEnEspera` (`it.remove()` y la lista `asignados` como recibo), `Recursivos` (ternario, base/recursivo, pública/privada), binaria (`medio` antidesbordamiento, `comparacion`, precondición) y DyV (base `inicio == fin` y 5 etapas). | Preparar la sustentación. Dificultades: por qué `final` en el código, por qué invertir `(b, a)`, por qué dos punteros en `eliminar`, por qué `remove` en la otra cola es O(n), para qué sirve DESPACHADO, cuándo cambia `disponible`, para qué la lista `asignados`, por qué dos métodos público/privado, de dónde sale `inicio + (fin - inicio) / 2`. | Respondí con mis palabras cada pregunta y la IA corrigió las respuestas. No hubo ejecución nueva: el código no cambió. | Sin cambios de código. Repaso teórico cerrado al ~90%; descartadas las prácticas de `Sustentacion.md` (secciones 2-4). |

## D.2. Una respuesta cuestionable de IA

La primera versión de `CentroDistribucion` generada por IA permitía volver a asignar y entregar un paquete ya entregado, que quedaba dos veces en el historial.

| Respuesta original | Problema detectado | Análisis | Solución final |
|---|---|---|---|
| `asignar(paquete, id)` solo validaba el repartidor (existe, disponible, misma zona). `registrarEntrega` hacía `asignaciones.remove(codigo)` y agregaba el paquete al historial. | Se podía asignar un paquete ya entregado, uno todavía pendiente o uno no registrado en el centro. Reproducido: el historial quedó `PQ731 -> PQ731 -> PQ318 -> null`. | **Comportamiento:** historial duplicado y un `null` dentro de la lista propia. **Estructura:** `asignaciones` solo sabe qué está asignado ahora; al hacer `remove` se borraba la única evidencia. **Restricción incumplida:** "el historial deberá conservar el orden real de las entregas". | Un `HashMap<String, EstadoPaquete>` con el ciclo PENDIENTE → DESPACHADO → ASIGNADO → ENTREGADO, validado en O(1) por `exigirEstado`. La demo (sección 7) muestra los cuatro rechazos. |
| Despachar por una cola quita el paquete de la otra con `remove(objeto)`. | Funciona, pero cuesta O(n) por despacho. | **Complejidad:** O(n) en lugar de O(1) u O(log n); despachar los 500.000 paquetes costaría O(n²). | Se mantiene por simplicidad con 30 paquetes; se documenta la eliminación perezosa como alternativa (A.1). |
| La primera versión ponía al paquete despachado sin repartidor en `enEsperaDeRepartidor`, pero no había ningún método para volver a intentarlo. | PQ407 (Circasia) salía de las dos colas y quedaba congelado en DESPACHADO para siempre: ni pendiente, ni asignado, ni entregado. | **Comportamiento:** el paquete nunca volvía al flujo aunque R005 volviera a estar disponible. **Estructura:** cola sin mecanismo de salida (estado sin transición). **Restricción incumplida:** todo paquete despachado debe poder llegar a asignado y entregado. | `reintentarEnEspera()` (recorre la espera con `Iterator` + `it.remove()` y asigna a los que ya tienen repartidor) y `habilitarRepartidor(id)`. La demo habilita a R005, reintenta y entrega PQ407. |
| Zona y destino se comparaban con `equalsIgnoreCase` en `asignar` y en `Recursivos`, mientras el `Set` y los `Map` usaban `equals`. | Dos nociones de igualdad: "armenia" y "Armenia" eran el mismo municipio para asignar, pero dos municipios distintos para agrupar y reportar. | **Comportamiento:** municipios duplicados y reportes inconsistentes. **Conceptual:** la igualdad de la asignación no coincidía con la de las colecciones (que usan `equals` + `hashCode`). **Restricción incumplida:** "la zona del repartidor debe corresponder al destino del paquete". | Todo usa `equals` exacto, la misma igualdad del `Set` y los `Map`, con la regla de escribir los municipios siempre igual. |

---

## Notas de trabajo (no van al informe)

Errores típicos de IA que vale la pena vigilar en el repaso (los menciona el enunciado):

- aplicar búsqueda binaria sobre la lista en orden de registro (no ordenada);
- confundir `Iterable` (la colección) con `Iterator` (el objeto que recorre);
- reemplazar la lista propia por `LinkedList`;
- resolver con `for`/`while` una operación que se pidió recursiva;
- llamar "divide y vencerás" a una recursión lineal (`indice + 1`);
- usar `obtener(i)` dentro de un `for` sobre la lista enlazada (O(n²)).

Pendientes de esta parte:

- Confirmar las 3 marcas "por confirmar" (fecha de la fila 1, cambios tras la revisión final, ajustes del Excel).
- Al terminar: copiar D.1 y D.2 completas al `.docx` y verificar que queden iguales a este archivo.
- Ya resuelto: el caso del `remove(objeto)` O(n) queda en D.2 (segundo caso) y como argumento en C.4.
- Ya resuelto: fila del repaso cerrada (prácticas de `Sustentacion.md` descartadas por el autor).
