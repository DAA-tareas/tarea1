import java.lang.reflect.Field;
import java.util.List;

public class stringQuickSort {


    List<Nodo> nodos;
    int length;
    Field field;

    public stringQuickSort(){

    }

    List<Nodo> sort(List<Nodo> listaNodos, Field field) {
        if (listaNodos == null || listaNodos.size() == 0) {
            return listaNodos;
        }
        this.nodos = listaNodos;
        this.length = listaNodos.size();
        this.field = field;
        quickSort(0, length - 1);
        return this.nodos;
    }

    void quickSort(int lowerIndex, int higherIndex) {
        int i = lowerIndex;
        int j = higherIndex;
        try {
            String pivot = field.get(this.nodos.get(lowerIndex + (higherIndex - lowerIndex) / 2)).toString();

            while (i <= j) {
                while (field.get(this.nodos.get(i)).toString().compareToIgnoreCase(pivot) < 0) {
                    i++;
                }

                while (field.get(this.nodos.get(j)).toString().compareToIgnoreCase(pivot) > 0) {
                    j--;
                }

                if (i <= j) {
                    exchangeNames(i, j);
                    i++;
                    j--;
                }
            }
            //call quickSort recursively
            if (lowerIndex < j) {
                quickSort(lowerIndex, j);
            }
            if (i < higherIndex) {
                quickSort(i, higherIndex);
            }
        }
        catch(java.lang.IllegalAccessException e){
            System.out.println("Illegal acces!");
        }
    }

    void exchangeNames(int i, int j) {
        Nodo temp = this.nodos.get(i);
        this.nodos.set(i, this.nodos.get(j));
        this.nodos.set(j,temp);
    }
}

