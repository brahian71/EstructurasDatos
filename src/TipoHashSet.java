import javax.xml.transform.SourceLocator;
import java.sql.SQLOutput;
import java.util.HashSet;
import java.util.Iterator;


public class TipoHashSet {


    public static void main(String[] args) {

        HashSet<String> lista = new HashSet<>();

        agregar(lista,"luis");
        lista.add("alberto");
        lista.add("robinson") ;
        lista.add("carlos") ;
        lista.add("andrea") ;
        lista.add("pedro");
        lista.add("baca");
        lista.add("ana");
        agregar(lista,"juice");
        agregar(lista,"ana");

        eliminar(lista,"robinson");


        System.out.println(lista);



    }


    public static void agregar (HashSet<String> lista, String palabra){
        lista.add(palabra);
    }
    public static void eliminar (HashSet<String> lista, String palabra){

        Iterator<String> iteraList = lista.iterator();

        while(iteraList.hasNext()){
            if(iteraList.next() == palabra){iteraList.remove();}

    }


    //private static boolean buscarName(String name, HashSet<String> lista){
    //    Iterator<String> iteraList = lista.iterator();

    //    while(iteraList.hasNext()){
    //        if(iteraList.next() == name){
    //            return true;}
    //    }
    //    return  false;
    //}

}}
