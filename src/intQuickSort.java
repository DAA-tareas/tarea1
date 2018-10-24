import java.lang.reflect.Field;
import java.util.List;

public class intQuickSort {

    private List<AbstractNodo> array;
    private int length;
    private Field field;

    public List<AbstractNodo> sort(List<AbstractNodo> inputArr, Field field) {

        if (inputArr == null || inputArr.size() == 0) {
            return null;
        }
        this.array = inputArr;
        length = inputArr.size();
        this.field = field;
        quickSort(0, length - 1);
        return this.array;
    }

    private void quickSort(int lowerIndex, int higherIndex) {

        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        try {
            int pivot = (int)field.get(array.get(lowerIndex + (higherIndex - lowerIndex) / 2));
            // Divide into two arrays
            while (i <= j) {
                /**
                 * In each iteration, we will identify a number from left side which
                 * is greater then the pivot value, and also we will identify a number
                 * from right side which is less then the pivot value. Once the search
                 * is done, then we exchange both numbers.
                 */
                while ((int)field.get(array.get(i)) < pivot) {
                    i++;
                }
                while ((int)field.get(array.get(j)) > pivot) {
                    j--;
                }
                if (i <= j) {
                    exchangeNumbers(i, j);
                    //move index to next position on both sides
                    i++;
                    j--;
                }
            }

            // call quickSort() method recursively
            if (lowerIndex < j)
                quickSort(lowerIndex, j);
            if (i < higherIndex)
                quickSort(i, higherIndex);
        }
        catch(java.lang.IllegalAccessException e){
            System.out.println("Illegal acces!");
        }
    }

    private void exchangeNumbers(int i, int j) {
        AbstractNodo temp = array.get(i);
        array.set(i,array.get(j));
        array.set(j, temp);
    }
}