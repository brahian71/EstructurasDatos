package recursividad;

/**
 * TEMA 1 - Recursividad, estilo 3: DIVIDE Y VENCERAS.
 *
 * La diferencia con los otros dos estilos: aca NO hay acumulador. Cada llamada
 * devuelve un resultado que se COMBINA al regresar, y para obtener la mitad
 * izquierda de la respuesta hace falta resolver la mitad derecha primero.
 *
 *   sumar = sumar(izq) + sumar(der)
 *
 * Eso obliga a guardar los dos resultados en variables separadas
 * (no se puede escribir un solo return con la suma de dos llamadas).
 *
 * Estructura obligatoria de una recursion:
 *   1. CASO BASE     -> cuando llego al ultimo elemento (inicio == fin)
 *   2. CASO RECURSIVO -> partir en dos y llamar
 *   3. COMBINACION   -> unir los dos resultados parciales
 *
 * Si falta el caso base, la recursion nunca frena (StackOverflowError).
 * Si la division es mala (mitad siempre igual a inicio), tambien.
 */
public class DivideYVenceras {

    public static void main(String[] args) {
        int[] arreglo = {3, 4, 2, 6, 5};
        int suma = sumarDyV(arreglo, 0, arreglo.length - 1);

        System.out.println("Suma: " + suma);

        // Arreglo de longitud impar (5): ini=0, fin=4 -> nunca se cruzan.
        // Con longitud PAR hay que tener cuidado: si ini == fin, el caso base
        // devuelve arreglo[fin], pero si ini > fin (vacio) nunca se cumple.
        // Ese es el error tipico al pasarle un arreglo vacio. Ver la nota
        // que esta abajo en sumarDyV.
        int[] par = {3, 4, 2, 6};
        System.out.println("Suma par: " + sumarDyV(par, 0, par.length - 1));
    }

    public static int sumarDyV(int[] arreglo, int inicio, int fin) {

        // 1. CASO BASE
        if (inicio == fin) {
            return arreglo[fin];
        }

        // OJO: si arreglo.length == 0 entonces inicio = 0 y fin = -1, y
        // inicio == fin es false, asi que se parte infinitamente.
        // Por eso el main llama sumarDyV con un arreglo de tamanho impar,
        // y el arreglo vacio de arriba es un caso limite que NO se resuelve
        // con este metodo (falta el if para arreglo vacio). Ejercicio.

        // 2. PARTIR
        int mitad = (inicio + fin) / 2;

        // 3. CASOS RECURSIVOS + COMBINACION
        int izq = sumarDyV(arreglo, inicio, mitad);
        int der = sumarDyV(arreglo, mitad + 1, fin);

        return izq + der;
    }
}
