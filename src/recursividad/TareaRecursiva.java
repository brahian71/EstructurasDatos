package recursividad;

/**
 * TEMA 1 - Recursividad, estilo 2: ACUMULADOR COMO PARAMETRO.
 *
 * El acumulador viaja HACIA ADENTRO en la llamada recursiva. Cuando se llega
 * al caso base no hay nada que "armar": el acumulador ya esta completo y
 * solo se devuelve.
 *
 *   sumaNumeros(i+1, arreglo, suma + arreglo[i])
 *      ^^^^^^^^             ^^^^^^^^^^^^^^^^^^^^
 *      avanza                 lleva el dato acumulado
 *
 * Ventaja: cada llamada es "pura", no toca variables compartidas.
 */
public class TareaRecursiva {

    /** Suma todos los numeros de un arreglo. Acumulador como parametro. */
    public static int sumaNumeros(int i, int[] arreglo, int suma) {

        if (i == arreglo.length) {
            return suma;
        }

        suma = sumaNumeros(i + 1, arreglo, suma + arreglo[i]);
        return suma;
    }

    /** Suma solo los numeros del arreglo que sean mayores o iguales a num. */
    public static int sumaNumerosMayoresN(int i, int[] arreglo, int suma, int num) {

        if (i == arreglo.length) {
            return suma;
        }

        if (arreglo[i] >= num) {
            return sumaNumerosMayoresN(i + 1, arreglo, suma + arreglo[i], num);
        }
        suma = sumaNumerosMayoresN(i + 1, arreglo, suma, num);
        return suma;
    }

    /**
     * Variante con el filtro al reves: si NO cumple la condicion, avanza igual.
     * Se puede dejar en una sola llamada fusionando el if, queda mas corto.
     */
    public static int contarMayoresN(int i, int[] arreglo, int num) {

        if (i == arreglo.length) {
            return 0;
        }

        int cuenta = (arreglo[i] >= num) ? 1 : 0;
        return cuenta + contarMayoresN(i + 1, arreglo, num);
    }

    public static void main(String[] args) {

        int[] arreglo = {12, 13, 6, 345, 7, 888, 643};

        System.out.println("Suma total:              " + sumaNumeros(0, arreglo, 0));
        System.out.println("Suma mayores o iguales a 800: " + sumaNumerosMayoresN(0, arreglo, 0, 800));
        System.out.println("Cantidad mayores o iguales a 800: " + contarMayoresN(0, arreglo, 800));
    }
}
