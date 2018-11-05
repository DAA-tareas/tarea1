import java.io.IOException;
import java.util.*;

public class TestP2 {

    public void P1Inserciones(int cantidadDatos, String path, String folder) throws IOException{
        Database db = new Database(folder + "/" + path);
        List<Nodo> nodoList = new ArrayList<Nodo>();

        List<Integer> randomList = new ArrayList<>();
        //Generar datos random
        for(int n=0; n<cantidadDatos; n++){
            randomList.add(n);
        }
        Collections.shuffle(randomList);

        for(int n : randomList){
            Random random = new Random();
            Nodo nodo = new NodoCons(n, Integer.toString(10000000+n) + "-" + random.nextInt(10), random.nextInt(2000));
            nodoList.add(nodo);
            //Realizar inserciones
            if (n%Math.pow(10,5) == 0) {
                if(n > 0) {
                    db.addToFolder(nodoList, folder);
                    nodoList = new ArrayList<Nodo>();
                }
            }
        }

        if (nodoList.size() > 0) {
            db.addToFolder(nodoList, folder);
        }

        System.out.println("--- " + cantidadDatos + " inserciones realizadas---");
    }


    public void P2BTreeSearch(String path, String folder, String field, int i) throws IOException, NoSuchFieldException, IllegalAccessException{
        int iterations = 1000;
        Database db = new Database(folder + "/" + path);
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

        System.out.println("Resultados para " + (int)Math.pow(10, i) + " cantidad de datos con " + iterations + " iteraciones");
        System.out.println("Promedio inserción: " + meanInsert);
        System.out.println("Promedio búsqueda: " + meanSearch);

    }

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException{
        /*
        i: número de elementos a insertar y luego ordenar
        path: nombre del archivo que se genera al insertar, para luego ser ordenado
        field: campo por el cual se va a ordenar la base de datos
         */
        int i = 2;
        int N = (int) Math.pow(10, i);
        String path = "P2Inserciones-" + N + ".txt";
        String folder = "testP2-" + N;
        String field = "id";

        TestP2 test = new TestP2();

        //Primero generar datos
        test.P1Inserciones(N, path, folder);
        System.out.println();

        /*
        TEST2
        Se deben tener los archivos "P2Inserciones-" que se generan en las lineas anteriores
         */

        // Dentro del test, se genera un valor aleatorio para buscarlo
        test.P2BTreeSearch(path, folder, field, i);


    }
}
