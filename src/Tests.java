import com.sun.org.glassfish.external.statistics.Stats;

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

        System.out.println("Numero potencia (N): " + i);
        System.out.println("Tiempo total: " + deltaTime);
        System.out.println("Accesos totales a discos: " + db.getAccessDisk());

        return db;
    }

    int[] getN(){
        return this.N;
    }

    public Database P2BTree(String filepath, String field, int i) throws IOException, NoSuchFieldException, IllegalAccessException{
        Database db = P1Ordenar(filepath, field, i);
        db.bTreeIni();
        long iniTime = System.currentTimeMillis();
        Map<String, Nodo> m = db.firstOfPaths();
        for (Map.Entry<String, Nodo> entry : m.entrySet()){
            db.insertBTree(field, entry.getValue(), entry.getKey());
        }
        long finTime = System.currentTimeMillis();
        long deltaTime = finTime - iniTime;

        System.out.println(db.getPartionPaths());
        System.out.println("--o--");
        //System.out.println(db.getBTree().toString());

        System.out.println("Numero potencia (N): " + i);
        System.out.println("Tiempo total: " + deltaTime);
        System.out.println("Accesos totales a discos: " + db.getAccessDisk());

        return db;
    }

    public void P2BTreeSearch(String filepath, String field, int i) throws IOException, NoSuchFieldException, IllegalAccessException{
        int iterations = 1000;
        Database db = new Database(filepath);
        db.ordenar(field);
        // Para 1000 iteraciones, insertar en el bTree
        long iniTimeInsert = System.currentTimeMillis();
        for(int n=0; n<iterations; n++){
            db.bTreeIni();
            Map<String, Nodo> m = db.firstOfPaths();
            for (Map.Entry<String, Nodo> entry : m.entrySet()){
                db.insertBTree(field, entry.getValue(), entry.getKey());
            }
        }
        long finTimeInsert = System.currentTimeMillis();
        long deltaTimeInsert = finTimeInsert - iniTimeInsert;
        double meanInsert = 1.0*deltaTimeInsert/iterations;

        System.out.println("Promedio inserción: " + meanInsert);

        Random random = new Random();

        // Para 1000 iteraciones, busquedas en el bTree
        long iniTimeSearch = System.currentTimeMillis();
        for(int n=0; n<iterations; n++){
            int rint = random.nextInt(i);
            db.searchInFile(Integer.toString(rint));
        }
        long finTimeSearch = System.currentTimeMillis();
        long deltaTimeSearch = finTimeSearch - iniTimeSearch;

        double meanSearch = 1.0*deltaTimeSearch/iterations;

        System.out.println("Promedio búsqueda: " + meanSearch);

    }

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException{
        /*
        Tests t1 = new Tests();
        for(int n : t1.getN()){
            t1.P1Inserciones(n);
            System.out.println();
        }
*/
        /*
        Tests t2 = new Tests();
        //for(int n : t2.getN()){
        for(int n=0; n<1; n++){
            t2.P1Ordenar("testP1Inserciones-1000000.txt" ,"id", 6);
            System.out.println();
        }
        */
        Random random = new Random();
        Tests t4 = new Tests();
        int datos = 10000000;
        t4.P2BTreeSearch("testP1Inserciones-"+ datos +".txt", "id", 6);



        //Database db = new Database("testP1Inserciones.txt");
        //db.segmentar(1837, "id");

        /*
        for(int i : t.getN()){
            System.out.println(i);
        }
        */

    }

}
