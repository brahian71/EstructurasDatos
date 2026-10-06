public class TareaRecursiva {

    ///Realizar un metodo recursivo que sume los números de un arreglo.
    public static int sumaNumeros(int i, int []arreglo, int suma) {

        if (i == arreglo.length) {
            return suma;
        }
      suma = sumaNumeros(i+1, arreglo, suma+=arreglo[i]);
        return suma;
    }

    /// Realizar un metodo recursivo que sume los números de un arreglo mayores a un número dado.
    public static int sumaNumerosMayoresN(int i, int []arreglo, int suma, int num) {

        if (i == arreglo.length) {
            return suma;
        }

        if(arreglo[i] >= num){
            return sumaNumerosMayoresN(i+1, arreglo,suma+=arreglo[i], num);
        }
        suma = sumaNumerosMayoresN(i+1, arreglo,suma, num);
        return suma;
    }

    public static void main(String[] args) {

        int [] arreglo = {12,13,6,345,7,888,643};

        int suma = sumaNumeros(0,arreglo,0);
        int sumaM = sumaNumerosMayoresN(0,arreglo,0,800);

        System.out.println(suma);
        System.out.println(sumaM);

    }
}