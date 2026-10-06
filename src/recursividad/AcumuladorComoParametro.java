package recursividad;

/**
 * TEMA 1 - Recursividad, estilo 2: ACUMULADOR COMO PARAMETRO (parte 2).
 *
 * El acumulado viaja HACIA ADENTRO. Al llegar al caso base no se arma nada:
 * el dato ya esta completo y se devuelve tal cual.
 *
 *   obtenerMayor(arreglo, i + 1, mayor)  <- el ganador viaja de bajada en bajada
 *
 * La version de abajo es la "correcta" para este estilo.
 * Ver TareaRecursiva.sumaNumeros para el mismo estilo aplicado a sumar.
 */
public class AcumuladorComoParametro {

    /**
     * Devuelve el mayor elemento del arreglo desde i.
     *
     * OJO con el caso base: si arreglo[i] es el ultimo, NO puedo devolver
     * Integer.MIN_VALUE porque puede que ese sea justamente el mayor.
     * Por eso el caso base devuelve arreglo[i].
     */
    public static int obtenerMayorRecursivo(int[] arreglo, int i, int mayor) {
        if (i == arreglo.length - 1) {
            return mayor > arreglo[i] ? mayor : arreglo[i];
        }

        int nuevoMayor = arreglo[i] > mayor ? arreglo[i] : mayor;
        return obtenerMayorRecursivo(arreglo, i + 1, nuevoMayor);
    }

    /** Azucar: el llamador no tiene que saber con que valor arrancar. */
    public static int obtenerMayor(int[] arreglo) {
        if (arreglo.length == 0) {
            throw new IllegalArgumentException("el arreglo esta vacio");
        }
        return obtenerMayorRecursivo(arreglo, 0, Integer.MIN_VALUE);
    }

    /** Segundo mayor DISTINTO (no el mayor repetido). */
    public static int obtenerSegundoMayor(int[] arreglo) {
        if (arreglo.length < 2) {
            throw new IllegalArgumentException("se necesitan al menos 2 elementos");
        }

        int mayor = Integer.MIN_VALUE;
        int segundo = Integer.MIN_VALUE;

        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i] > mayor) {
                segundo = mayor;
                mayor = arreglo[i];
            } else if (arreglo[i] < mayor && arreglo[i] > segundo) {
                segundo = arreglo[i];
            }
        }
        return segundo;
    }

    public static void main(String[] args) {

        int[] arreglo = {12, 13, 6, 345, 7, 888, 643};

        System.out.println("Mayor:        " + obtenerMayor(arreglo));
        System.out.println("Segundo mayor: " + obtenerSegundoMayor(arreglo));

        int[] conRepetidos = {4, 9, 4, 2, 9};
        System.out.println("\ncon repetidos " + java.util.Arrays.toString(conRepetidos));
        System.out.println("Mayor:        " + obtenerMayor(conRepetidos));
        System.out.println("Segundo mayor: " + obtenerSegundoMayor(conRepetidos)
                + "   (debe ser 4, no 9: el 9 ya esta usado como mayor)");
    }
}
