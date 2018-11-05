import javax.xml.crypto.Data;
import java.lang.StringBuffer;
import java.io.IOException;
import java.util.*;

public class Tests {

    public int[] N;

    public Tests(){
        N = new int[7];
        for(int i=0; i<N.length; i++){
            this.N[i] = (int) Math.pow(10, i+1);
        }
    }


    public void P1Inserciones(int i) throws IOException{
        Database db = new Database("testP1Inserciones-" + i + ".txt");
        long deltaTime = 0;
        List<Nodo> nodoList = new ArrayList<Nodo>();
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
                    db.add(nodoList);
                    nodoList = new ArrayList<Nodo>();
                    diskAccess++;
                }
            }
        }

        if (nodoList.size() > 0) {
            db.add(nodoList);
            diskAccess++;
        }

        long finTime = System.currentTimeMillis();

        deltaTime = finTime - iniTime;

        System.out.println("--- Inserciones ---");
        System.out.println("Numero potencia (N): " + i);
        System.out.println("Tiempo total: " + deltaTime);
        System.out.println("Accesos totales a discos: " + diskAccess);
    }

    public Database P1Ordenar(String filepath, String field, int i) throws IOException{
        Database db = new Database(filepath);
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

    int[] getN(){
        return this.N;
    }


    public void P2BTreeSearch(String filepath, String field, int i, String key) throws IOException, NoSuchFieldException, IllegalAccessException{
        Database db = new Database(filepath);
        db.ordenar(field);
        db.bTreeIni();
        long iniTimeInsert = System.currentTimeMillis();
        Map<String, Nodo> m = db.firstOfPaths();
        for (Map.Entry<String, Nodo> entry : m.entrySet()){
            db.insertBTree(field, entry.getValue(), entry.getKey());
        }
        long finTimeInsert = System.currentTimeMillis();
        long deltaTimeInsert = finTimeInsert - iniTimeInsert;
        System.out.println("Accesos totales a discos: " + db.getAccessDisk());
        //System.out.println(db.getBTree());
        long iniTimeSearch = System.currentTimeMillis();
        Nodo n = db.searchInFile(key);
        long finTimeSearch = System.currentTimeMillis();
        long deltaTimeSearch = finTimeSearch - iniTimeSearch;

        if(n == null){
            System.out.println("No Encontrado");
        }else{
            System.out.println(n.makeSerial());
        }


        System.out.println("Numero potencia (N): " + i);
        System.out.println("Tiempo total Inserción: " + deltaTimeInsert);
        System.out.println("Tiempo total Búsqueda: " + deltaTimeSearch);

        //System.out.println("Secondary Path: " + db.getSecondaryPaths());
    }

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException{

        // Es necesario comentar cada test  de cada parte para ejecutar los siguientes.
        // Además se deben borrar los archivos generados.

        /*
        i: número de elementos a insertar y luego ordenar
        path: nombre del archivo que se genera al insertar, para luego ser ordenado
        field: campo por el cual se va a ordenar la base de datos
         */
        int i = 7;
        int N = (int) Math.pow(10, i);
        String path = "testP1Inserciones-" + N + ".txt";
        String field = "id";

        /*
        TEST - PARTE 1
         */

        Tests t1 = new Tests();
        t1.P1Inserciones(N);
        System.out.println();
        /*
        Tests t2 = new Tests();
        t2.P1Ordenar(path ,field, i);
        System.out.println();
        */

        /*
        TEST - PARTE 2
        Se deben tener los archivos "testP1Inserciones-" correspondiente previamente generado
         */

        Tests t4 = new Tests();
        //key: valor de la llave de la fila que estamos buscando
        t4.P2BTreeSearch(path, field, i, "43");


    }

}
