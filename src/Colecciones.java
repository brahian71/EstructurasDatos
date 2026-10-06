import java.util.ArrayList;
import java.util.Iterator;

public class Colecciones {

    public static void main(String[] args) {

        ArrayList<String> lista = new ArrayList<>();
        lista.add("luis");
        lista.add("alberto");
        lista.add("robinson") ;
        lista.add("carlos") ;
        lista.add("andrea") ;
        lista.add("pedro");
        lista.add("baca");
        lista.add("ana");

        //eliminarA(lista);
        //System.out.println(lista);

        iteradorRemoveA(lista);
        System.out.println(lista);
    }

    public static void eliminarA ( ArrayList<String> lista){

        for (int i=0 ; i < lista.size() ; i++){
            if (lista.get(i).toLowerCase().startsWith("a") ){
                lista.remove(i);}
        }
    }

    public static  void iteradorRemoveA (ArrayList<String> lista){

        Iterator<String> listaIterable = lista.iterator();

        while(listaIterable.hasNext()){
            if(listaIterable.next().toLowerCase().startsWith("a")){
            listaIterable.remove();
        }}
    }

}
