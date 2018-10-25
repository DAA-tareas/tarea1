import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Ordenador {

    public Ordenador(){

    }

    public static void printNodos(List<Nodo> lista){
        Field[] fieldsE = lista.get(0).getClass().getDeclaredFields();
        Field[] fieldsS = lista.get(0).getClass().getSuperclass().getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        for (Field field: fieldsS){
            fields.add(field);
        }
        for (Field field: fieldsE){
            fields.add(field);
        }
        for (int i = 0; i<lista.size();i++){
            for(int field = 0; field < fields.size(); field++){
                fields.get(field).setAccessible(true);
                try {
                    System.out.print(fields.get(field).get(lista.get(i)));
                    System.out.print("/");
                }
                catch(java.lang.IllegalAccessException e){
                    System.out.println("Illegal access en print!");
                }
            }

            System.out.println("");
        }
    }

    public static List<Nodo> ordenarSec(List<Nodo> listaNodos, String fld){
        try{
            Field field = listaNodos.get(0).getClass().getDeclaredField(fld);
            field.setAccessible(true);
            try{
                if(field.get(listaNodos.get(0)) instanceof String){
                    stringQuickSort sorter = new stringQuickSort();
                    return sorter.sort(listaNodos, field);
                }
                else{
                    System.out.println("es int!");
                    intQuickSort sorter = new intQuickSort();
                    return sorter.sort(listaNodos, field);
                }
            }


            catch(java.lang.IllegalAccessException e){
                System.out.println("Illegal acces!");
            }
        }
        catch (java.lang.NoSuchFieldException e){
            try{

                Field field = listaNodos.get(0).getClass().getSuperclass().getDeclaredField(fld);
                field.setAccessible(true);
                try{
                    if(field.get(listaNodos.get(0)) instanceof String){
                        System.out.print("");
                        stringQuickSort sorter = new stringQuickSort();
                        return sorter.sort(listaNodos, field);
                    }
                    else{
                        System.out.println("es int!");
                        System.out.println(field.get(listaNodos.get(0)));
                        intQuickSort sorter = new intQuickSort();
                        return sorter.sort(listaNodos, field);
                    }
                }


                catch(java.lang.IllegalAccessException b){
                    System.out.println("Illegal acces!");
                }

            }
            catch(java.lang.NoSuchFieldException a){
                System.out.println("Atributo no existe!");
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Nodo nodo = new NodoProd(4,1002, 2003, 2001 );
        Nodo nodo2 = new NodoProd(2,1003, 2001, 2004 );
        Nodo nodo3 = new NodoProd(3,1001, 2004, 2002 );
        Nodo nodo4 = new NodoProd(1,1004, 2002, 2003 );
        List<Nodo> lista = new ArrayList<>();
        lista.add(nodo);
        lista.add(nodo2);
        lista.add(nodo3);
        lista.add(nodo4);
        printNodos(ordenarSec(lista, "ptosNec"));

    }
}
