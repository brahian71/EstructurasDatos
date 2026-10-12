import recursividad.AcumuladorAlRegresar;
import recursividad.AcumuladorComoParametro;
import recursividad.ArreglosRecursivos;
import recursividad.DivideYVenceras;
import recursividad.MultiplesLlamadas;
import recursividad.TareaRecursiva;
import colecciones.ClaseArrays;
import colecciones.ClaseCollections;
import colecciones.IteratorSeguro;
import sets.TipoHashSet;
import genericos.CarroCompraGenerics;
import parcial.MainParcial;
import parcial.PruebasListaEnlazada;

import java.util.Scanner;

/**
 * Menu central. Cada clase tiene su propio main(), asi que igual podes correr
 * cualquiera con clic derecho -> Run. Este menu solo sirve para ver de una
 * que se corra todo.
 *
 * Tema 1, 3 y 4 tienen contenido. Tema 2 esta a medias:
 * IteratorSeguro funciona, ClaseArrays y ClaseCollections todavia tiran
 * UnsupportedOperationException porque los ejercicios estan pendientes.
 */
public class Main {

    private static final Scanner LECTOR = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("  ESTRUCTURAS DE DATOS - menu de demos");
        System.out.println("===========================================");

        int opcion;

        do {
            System.out.println("\n--- TEMA 1: RECURSIVIDAD ---");
            System.out.println("  1) Acumulador al regresar (factorial, digitos, matrioska)");
            System.out.println("  2) Acumulador como parametro (mayor, segundo mayor)");
            System.out.println("  3) Suma con acumulador como parametro (TareaRecursiva)");
            System.out.println("  4) Divide y venceras (suma)");
            System.out.println("  5) Arreglos recursivos [PENDIENTE]");
            System.out.println("  6) Multiples llamadas [PENDIENTE: Fibonacci, Hanoi]");

            System.out.println("\n--- TEMA 2: COLECCIONES ---");
            System.out.println("  7) Iterator seguro para eliminar");
            System.out.println("  8) Clase Arrays [PENDIENTE]");
            System.out.println("  9) Clase Collections [PENDIENTE]");

            System.out.println("\n--- TEMA 3: SETS ---");
            System.out.println(" 10) HashSet vs LinkedHashSet vs TreeSet");

            System.out.println("\n--- TEMA 4: GENERICOS ---");
            System.out.println(" 11) Carrito generico + iterador propio");

            System.out.println("\n--- PARCIAL 1: QUINDIOEXPRESS ---");
            System.out.println(" 12) Demo completa del parcial");
            System.out.println(" 13) Pruebas de la lista enlazada propia");

            System.out.println("\n  0) salir");
            System.out.print("\nopcion: ");
            opcion = LECTOR.nextInt();

            ejecutar(opcion);

        } while (opcion != 0);

        LECTOR.close();
        System.out.println("chao");
    }

    private static void ejecutar(int opcion) {

        switch (opcion) {
            case 1 -> {
                encabezado("Acumulador construido al regresar");
                AcumuladorAlRegresar.main(new String[0]);
            }
            case 2 -> {
                encabezado("Acumulador como parametro");
                AcumuladorComoParametro.main(new String[0]);
            }
            case 3 -> {
                encabezado("Suma con acumulador como parametro");
                TareaRecursiva.main(new String[0]);
            }
            case 4 -> {
                encabezado("Divide y venceras");
                DivideYVenceras.main(new String[0]);
            }
            case 5 -> {
                encabezado("Arreglos recursivos [PENDIENTE]");
                ArreglosRecursivos.main(new String[0]);
            }
            case 6 -> {
                encabezado("Multiples llamadas recursivas [PENDIENTE]");
                MultiplesLlamadas.main(new String[0]);
            }
            case 7 -> {
                encabezado("Iterator seguro para eliminar");
                IteratorSeguro.main(new String[0]);
            }
            case 8 -> {
                encabezado("Clase Arrays [PENDIENTE]");
                ClaseArrays.main(new String[0]);
            }
            case 9 -> {
                encabezado("Clase Collections [PENDIENTE]");
                ClaseCollections.main(new String[0]);
            }
            case 10 -> {
                encabezado("HashSet vs LinkedHashSet vs TreeSet");
                TipoHashSet.main(new String[0]);
            }
            case 11 -> {
                encabezado("Carrito generico + iterador propio");
                CarroCompraGenerics.main(new String[0]);
            }
            case 12 -> {
                encabezado("Parcial 1: QuindioExpress");
                MainParcial.main(new String[0]);
            }
            case 13 -> {
                encabezado("Pruebas de la lista enlazada propia");
                PruebasListaEnlazada.main(new String[0]);
            }
            case 0 -> System.out.println("saliendo...");
            default -> System.out.println("opcion invalida");
        }
    }

    private static void encabezado(String titulo) {
        System.out.println("\n===========================================");
        System.out.println("  " + titulo);
        System.out.println("===========================================");
    }
}
