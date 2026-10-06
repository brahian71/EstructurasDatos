package recursividad;

/**
 * TEMA 1 - Recursividad, estilo 1: ACUMULADOR CONSTRUIDO AL REGRESAR.
 *
 * El acumulador NO viaja como parametro. Cada llamada devuelve UN pedazo del
 * resultado y lo va acumulando en su propio marco de pila mientras regresa:
 *
 *   int suma = sumarArreglo(arreglo, i + 1);   // 1. bajar primero
 *   return suma + arreglo[i];                  // 2. Combining al subir
 *
 * El orden NO se puede invertir. Si escribo primero el return, la llamada
 * recursiva nunca se ejecuta.
 *
 * Ventaja: la firma queda limpia, no necesita parametro extra.
 * Desventaja: si la rama recursiva esta en la mitad de un if con dos
 * llamadas, cada rama necesita su propia variable.
 */
public class AcumuladorAlRegresar {

    /** factorial(n) = n * factorial(n-1). El caso base es 1. */
    public static long factorial(int n) {
        if (n <= 1) {
            return 1;
        }
        long resultado = factorial(n - 1);
        return n * resultado;
    }

    /** Cuenta digitos partiendo el numero entre 10. Caso base: n < 10 -> 1 digito. */
    public static int contarDigitos(int n) {
        if (n < 10) {
            return 1;
        }
        int digitos = contarDigitos(n / 10);
        return digitos + 1;
    }

    /** Suma los elementos de un arreglo desde el indice i. */
    public static int sumarArreglo(int[] arreglo, int i) {
        if (i == arreglo.length) {
            return 0;
        }
        int suma = sumarArreglo(arreglo, i + 1);
        return suma + arreglo[i];
    }

    /**
     * Matrioska (o palindromo de parentesis/brackets/llaves).
     *
     * El truco: se resuelve con DOS indices que se acercan desde los extremos.
     * No es divide y venceras porque no parte el problema, lo CORRE.
     *
     * El caso base es ini >= fin (se cruzaron): todo quedo pareado.
     */
    public static boolean esMatrioska(String s, int ini, int fin) {
        if (ini >= fin) {
            return true;
        }
        char abre = s.charAt(ini);
        char cierra = s.charAt(fin);

        boolean casan = (abre == '[' && cierra == ']')
                || (abre == '(' && cierra == ')')
                || (abre == '{' && cierra == '}');

        if (!casan) {
            return false;
        }
        return esMatrioska(s, ini + 1, fin - 1);
    }

    public static boolean esMatrioska(String s) {
        return esMatrioska(s, 0, s.length() - 1);
    }

    // ------------------------------------------------------------------
    // VARIANTE void con println directo.
    //
    // Se documento aparte porque es la que "si hace clic" cuando el boolean
    // todavia no: la recursion devuelve un String que se va concatenando
    // hacia arriba y es dificil de seguir mentalmente. Imprimir en cada
    // nivel muestra la ejecucion de adentro hacia afuera, paso a paso.
    // ------------------------------------------------------------------

    public static void esMatrioskaVerbose(String s, int ini, int fin) {

        int nivel = nivel(s, ini);
        String sangria = "  ".repeat(nivel + 1);

        if (ini >= fin) {
            System.out.println(sangria + "[nivel " + nivel + "] se cruzaron (ini=" + ini
                    + ", fin=" + fin + ") -> es matrioska");
            return;
        }

        char abre = s.charAt(ini);
        char cierra = s.charAt(fin);

        System.out.println(sangria + "[nivel " + nivel + "] reviso '" + abre + "' (pos " + ini
                + ") contra '" + cierra + "' (pos " + fin + ")");

        boolean casan = (abre == '[' && cierra == ']')
                || (abre == '(' && cierra == ')')
                || (abre == '{' && cierra == '}');

        if (!casan) {
            System.out.println(sangria + "[nivel " + nivel + "] '" + abre + "' NO casa con '" + cierra
                    + "' -> NO es matrioska (el if corta el camino de regreso)");
            return;
        }

        System.out.println(sangria + "[nivel " + nivel + "] casan, entro al interior");
        esMatrioskaVerbose(s, ini + 1, fin - 1);
    }

    /** Solo para que la traza se lea bien: cuantas veces se metio hacia adentro. */
    private static int nivel(String s, int ini) {
        return Math.max(0, ((s.length() - 1) / 2) - ini);
    }

    public static void main(String[] args) {

        System.out.println("5! = " + factorial(5));
        System.out.println("0! = " + factorial(0));
        System.out.println("digitos de 908172: " + contarDigitos(908172));
        System.out.println("digitos de 7: " + contarDigitos(7));

        int[] arreglo = {12, 13, 6, 345, 7, 888, 643};
        System.out.println("suma del arreglo: " + sumarArreglo(arreglo, 0));

        // OJO al leer los casos: comparar el PRIMER caracter contra el ULTIMO
        // no alcanza para saber si algo esta balanceado. "[][]{}" empieza
        // con '[' y termina con '}' -> no casa -> false, aunque por dentro
        // SI este balanceado. Ese es el limite de este metodo: resuelve
        // palindromos de parentesis, no balanceo general de anidamiento.

        System.out.println("\nmatrioska [][]{}:      " + esMatrioska("[][]{}") + "   (esperado false: extremos '[' vs '}' NO casan)");
        System.out.println("matrioska [()]:        " + esMatrioska("[()]") + "   (esperado true: extremos '[' vs ']' casan)");
        System.out.println("matrioska {[]}:        " + esMatrioska("{[]}") + "   (esperado true)");
        System.out.println("matrioska [({})]:      " + esMatrioska("[({})]") + "   (esperado true)");
        System.out.println("matrioska [({])}:      " + esMatrioska("[({])}") + "   (esperado false)");
        System.out.println("matrioska ([]):        " + esMatrioska("([])") + "   (esperado true)");
        System.out.println("matrioska ][:          " + esMatrioska("][") + "   (esperado false)");

        System.out.println("\ntraza paso a paso de [({})]:");
        esMatrioskaVerbose("[({})]", 0, "[({})]".length() - 1);

        System.out.println("\ntraza paso a paso de [({])}:");
        esMatrioskaVerbose("[({])}", 0, "[({])}".length() - 1);
    }
}
