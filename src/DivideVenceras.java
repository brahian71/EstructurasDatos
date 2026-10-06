public class DivideVenceras {

    public static void main(String[] args) {
        int [] arreglo = {3,4,2,6,5};
        int n = sumarDyV(arreglo,0, arreglo.length-1);

        System.out.println(n);
    }

    public static int sumarDyV(int [] arreglo, int inicio, int fin){

        if(inicio == fin){
            return arreglo[fin];
        }

        int mitad = (inicio + fin)/2;

        int izq = sumarDyV(arreglo, inicio, mitad);
        int der = sumarDyV(arreglo, mitad+1, fin);

        return izq+der;

    }
}
