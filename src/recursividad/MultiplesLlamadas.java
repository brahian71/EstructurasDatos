package recursividad;

/**
 * PENDIENTE - Recursion con VARIAS llamadas recursivas por invocacion.
 *
 * NO hay solucion escrita aca a proposito.
 *
 * ----------------------------------------------------------------------------
 * QUE CAMBIA RESPECTO A LOS ESTILOS ANTERIORES
 *
 * En factorial/sumar la recursion hace UNA llamada y despues opera.
 * En suma binaria (2^n) o en este factorial hacen DOS llamadas por invocacion.
 *
 * Eso tiene DOS consecuencias que tenes que saber explicar en la sustentacion:
 *
 *   1. El ARBOL DE LLAMADAS se bifurca. Ya no es una linea de ejecucion,
 *      es un arbol. Para 20 ya son ~un millon de invocaciones.
 *
 *   2. El TRABAJO SE REPITE. Si el subproblema ya se resolvio, el algoritmo
 *      lo vuelve a calcular. Por eso estos son O(2^n) mientras que el
 *      divide y venceras de DivideYVenceras es O(n).
 *
 *   La solucion clasica es MEMORIZACION: guardar en un arreglo el resultado
 *   de cada n ya calculado y consultarlo antes de volver a calcular.
 *   Eso baja Fibonacci de O(2^n) a O(n).
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 1 - Fibonacci (el clasico)
 *
 *   long fibonacci(int n)
 *
 *   f(0) = 0, f(1) = 1, f(n) = f(n-1) + f(n-2)
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 2 - Fibonacci memoizado (el que va en la sustentacion)
 *
 *   long fibonacciMemo(int n, long[] memoria)
 *
 *   Misma recurrence, pero antes de calcular:
 *     if (memoria[n] != -1) return memoria[n];
 *   y despues de calcular:
 *     memoria[n] = resultado;
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 3 - que valores de n aguantan sin reventar la pila?
 *
 *   long fibonacciSeguro(int n)
 *
 *   Alternativa iterativa (sin recursion) con dos variables:
 *   long anterior = 0, actual = 1.
 *   Pista: el desbordamiento de long ocurre alrededor de n = 92, pero el
 *   limite real aca lo impone el StackOverflowError, no el numero. Averigualo.
 *
 * ----------------------------------------------------------------------------
 * EJERCICIO 4 (opcional, el que mas rinde en sustentacion) - Hanoi
 *
 *   void hanoi(int n, char origen, char auxiliar, char destino)
 *
 *   Mueve n discos del poste origen al destino usando el auxiliar.
 *   Salida esperada: contame en voz alta por que son 2^n - 1 movimientos
 *   aunque el algoritmo solo tiene 3 lineas.
 *
 * ----------------------------------------------------------------------------
 * PARA VERIFICAR:
 *
 *   fibonacci(0) = 0    fibonacci(10) = 55
 *   fibonacci(1) = 1    fibonacci(20) = 6765
 */
public class MultiplesLlamadas {

    public static void main(String[] args) {
        System.out.println("TODO: implementar los 4 ejercicios de este archivo");
    }

    /** EJERCICIO 1 */
    public static long fibonacci(int n) {
        throw new UnsupportedOperationException("TODO: base n == 0 -> 0, n == 1 -> 1, si no f(n-1) + f(n-2)");
    }

    /** EJERCICIO 2 */
    public static long fibonacciMemo(int n, long[] memoria) {
        throw new UnsupportedOperationException("TODO: consultar memoria[n] antes de calcular y guardarlo despues");
    }

    /** EJERCICIO 3 */
    public static long fibonacciSeguro(int n) {
        throw new UnsupportedOperationException("TODO: version iterativa con dos variables, sin recursion");
    }

    /** EJERCICIO 4 */
    public static void hanoi(int n, char origen, char auxiliar, char destino) {
        throw new UnsupportedOperationException("TODO: caso base n == 0; si no, hanoi(n-1, origen, destino, auxiliar), imprimir, hanoi(n-1, auxiliar, origen, destino)");
    }
}
