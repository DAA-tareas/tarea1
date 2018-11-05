import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestP1 {

    public void P1Inserciones(int i, String path, String folder) throws IOException{
        Database db = new Database(folder + "/" + path);
        List<Nodo> nodoList = new ArrayList<Nodo>();
        long deltaTime = 0;
        int diskAccess = 0;

        List<Integer> randomList = new ArrayList<>();
        //Generar datos random
        for(int n=0; n<i; n++){
            randomList.add(n);
        }
        Collections.shuffle(randomList);

        long iniTime = System.currentTimeMillis();
        for(int n : randomList){
            Random random = new Random();
            Nodo nodo = new NodoCons(n, Integer.toString(10000000+n) + "-" + random.nextInt(10), random.nextInt(2000));
            nodoList.add(nodo);
            //Realizar inserciones
            if (n%Math.pow(10,5) == 0) {
                if(n > 0) {
                    db.addToFolder(nodoList, folder);
                    nodoList = new ArrayList<Nodo>();
                    diskAccess++;
                }
            }
        }

        if (nodoList.size() > 0) {
            db.addToFolder(nodoList, folder);
            diskAccess++;
        }

        long finTime = System.currentTimeMillis();

        deltaTime = finTime - iniTime;

        System.out.println("--- Inserciones ---");
        System.out.println("Numero de datos (N): " + i);
        System.out.println("Tiempo total: " + deltaTime);
        System.out.println("Accesos totales a discos: " + diskAccess);
    }

    public Database P1Ordenar(String filepath, String field, String folder, int i) throws IOException{
        Database db = new Database(folder + "/" + filepath);
        long iniTime = System.currentTimeMillis();
        db.ordenar(field);
        long finTime = System.currentTimeMillis();
        long deltaTime = finTime - iniTime;

        System.out.println("--- Ordenar datos ---");
        System.out.println("Numero potencia (N): " + i);
        System.out.println("Tiempo total: " + deltaTime);
        System.out.println("Accesos totales a discos: " + db.getAccessDisk());

        return db;
    }


    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {

        // Es necesario comentar cada test  de cada parte para ejecutar los siguientes.
        // Además se deben borrar los archivos generados.

        /*
        i: número de elementos a insertar y luego ordenar
        path: nombre del archivo que se genera al insertar, para luego ser ordenado
        field: campo por el cual se va a ordenar la base de datos
         */
        int i = 2;
        int N = (int) Math.pow(10, i);
        String path = "testP1Inserciones-" + N + ".txt";
        String folder = "testP1-" + N;
        String field = "id";

        /*
        TEST - PARTE 1
         */

        TestP1 t1 = new TestP1();
        t1.P1Inserciones(N, path, folder);

        System.out.println();

        TestP1 t2 = new TestP1();
        t2.P1Ordenar(path ,field, folder, i);
        System.out.println();

    }

}
