import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    public Database P1Ordenar(String filepath, String field, int i, String fileId) throws IOException{
        Database db = new Database(filepath);
        long iniTime = System.currentTimeMillis();
        db.ordenar(field, fileId);
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

    public Database P2BTree(String filepath, String field, int i, String fileId) throws IOException, NoSuchFieldException, IllegalAccessException{
        Database db = P1Ordenar(filepath, field, i, fileId);
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

    public void P2BTreeSearch(String filepath, String field, int i, String key, String fileId) throws IOException, NoSuchFieldException, IllegalAccessException{
        Database db = new Database(filepath);
        db.ordenar(field, fileId);
        db.bTreeIni();
        Map<String, Nodo> m = db.firstOfPaths();
        for (Map.Entry<String, Nodo> entry : m.entrySet()){
            db.insertBTree(field, entry.getValue(), entry.getKey());
        }
        System.out.println(db.getBTree().toString());
        long iniTime = System.currentTimeMillis();
        Nodo n = db.searchInFile(key);
        long finTime = System.currentTimeMillis();
        long deltaTime = finTime - iniTime;

        if(n == null){
            System.out.println("No Encontrado");
        }else{
            System.out.println(n.makeSerial());
        }


        System.out.println("Numero potencia (N): " + i);
        System.out.println("Tiempo total: " + deltaTime);
        //System.out.println("Secondary Path: " + db.getSecondaryPaths());
    }

    public void printAllInFile(String path){

    }

    public static void crearConsumidores( int i, String name) throws IOException{
        File file = new File(name);
        file.createNewFile();
        Database db = new Database(name);
        List<Nodo> nodoList = new ArrayList<Nodo>();

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
                }
            }
        }

        if (nodoList.size() > 0) {
            db.add(nodoList);
        }

    }


    public static void crearProductos( int i, String name) throws IOException{
        File file = new File(name);
        file.createNewFile();
        Database db = new Database(name);
        List<Nodo> nodoList = new ArrayList<Nodo>();

        List<Integer> randomList = new ArrayList<>();
        //Generar datos random
        for(int n=0; n<i; n++){
            randomList.add(n);
        }
        Collections.shuffle(randomList);

        long iniTime = System.currentTimeMillis();
        for(int n : randomList){
            Random random = new Random();
            Nodo nodo = new NodoProd(n, random.nextInt(40000) + 10000, random.nextInt(2000), random.nextInt(1000));
            nodoList.add(nodo);
            //Realizar inserciones
            if (n%Math.pow(10,5) == 0) {
                if(n > 0) {
                    db.add(nodoList);
                    nodoList = new ArrayList<Nodo>();
                }
            }
        }

        if (nodoList.size() > 0) {
            db.add(nodoList);
        }
    }

    public void P3ai(int cons, int prods) throws IOException, NoSuchFieldException, IllegalAccessException{

        crearConsumidores(cons, "Consumidores.txt");
        crearProductos( prods, "Productos.txt");

        Database dbCons = new Database("Consumidores.txt");
        dbCons.ordenar("ptosAc", "cons");
        dbCons.bTreeIni();
        Map<String, Nodo> m = dbCons.firstOfPaths();
        for (Map.Entry<String, Nodo> entry : m.entrySet()){
            dbCons.insertBTree("ptosAc", entry.getValue(), entry.getKey());
        }

        Database dbProd = new Database("Productos.txt");
        dbProd.ordenar("ptosNec", "prod");
        dbProd.bTreeIni();
        Map<String, Nodo> n = dbProd.firstOfPaths();
        for (Map.Entry<String, Nodo> entry : n.entrySet()){
            dbProd.insertBTree("ptosNec", entry.getValue(), entry.getKey());
        }

        List<String> pathsCons = dbCons.getSecondaryPaths();
        List<String> pathsProd = dbProd.getSecondaryPaths();


        long iniTime = System.currentTimeMillis();

        for (String path: pathsCons){
            BufferedReader br = new BufferedReader(new FileReader(path));
            //Busqueda sobre el archivo
            String line;
            while((line = br.readLine()) != null){
                if(line.split(" ").length!=3) ;
                else {

                    NodoCons readNodo = new NodoCons(line);
                    System.out.println(readNodo.makeSerial());

                    int acumulados = readNodo.getPtosAc();
                    String acumuladoS = Integer.toString(acumulados);

                    String inPath = dbProd.getBTree().get(acumuladoS);

                    Boolean inFileRead = false;

                    for (String pathProd : pathsProd) {
                        if (pathProd.compareTo(inPath) == 0) {
                            inFileRead = true;
                            BufferedReader brProd = new BufferedReader(new FileReader(pathProd));
                            String lineProd;

                            while ((lineProd = brProd.readLine()) != null) {
                                if (lineProd.split(" ").length != 4) ;
                                else {
                                    NodoProd readNodoProd = new NodoProd(lineProd);

                                    int necesarios = readNodoProd.getPtosNec();

                                    if (readNodoProd != null && (acumulados==necesarios || acumulados > necesarios) ){
                                        System.out.println("       " + readNodoProd.makeSerial());
                                    }
                                    if (readNodoProd != null && (acumulados < necesarios)) {
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                        else if (!inFileRead){
                            printAllInFile(pathProd);
                        }
                        else break;
                    }
                }
            }
        }
        long finTime = System.currentTimeMillis();
        long deltaTime = finTime - iniTime;

        System.out.println("Tiempo total: " + deltaTime);
        System.out.println("Accesos totales a discos: " + dbProd.getAccessDisk() + dbCons.getAccessDisk());

        for (String path: pathsCons){
            File file = new File(path);
            file.delete();
        }
        for (String path: pathsProd){
            File file = new File(path);
            file.delete();
        }
        File file = new File("Consumidores.txt");
        file.delete();
        File file2 = new File("Productos.txt");
        file2.delete();
    }

    public void P3aii(int cons, int prods) throws IOException, NoSuchFieldException, IllegalAccessException{

        crearConsumidores(cons, "Consumidores.txt");
        crearProductos( prods, "Productos.txt");

        Database dbCons = new Database("Consumidores.txt");
        dbCons.ordenar("ptosAc", "cons");
        dbCons.bTreeIni();
        Map<String, Nodo> m = dbCons.firstOfPaths();
        for (Map.Entry<String, Nodo> entry : m.entrySet()){
            dbCons.insertBTree("ptosAc", entry.getValue(), entry.getKey());
        }

        Database dbProd = new Database("Productos.txt");
        dbProd.ordenar("ptosNec", "prod");
        dbProd.bTreeIni();
        Map<String, Nodo> n = dbProd.firstOfPaths();
        for (Map.Entry<String, Nodo> entry : n.entrySet()){
            dbProd.insertBTree("ptosNec", entry.getValue(), entry.getKey());
        }

        List<String> pathsCons = dbCons.getSecondaryPaths();
        List<String> pathsProd = dbProd.getSecondaryPaths();


        long iniTime = System.currentTimeMillis();

        for (String path: pathsProd){
            BufferedReader brProd = new BufferedReader(new FileReader(path));
            //Busqueda sobre el archivo
            String line;
            while((line = brProd.readLine()) != null){
                if(line.split(" ").length!=4) ;
                else {

                    NodoProd readNodo = new NodoProd(line);
                    System.out.println(readNodo.makeSerial());

                    int necesarios = readNodo.getPtosNec();
                    String necesariosS = Integer.toString(necesarios);

                    String inPath = dbCons.getBTree().get(necesariosS);

                    Boolean inFileRead = false;

                    for (String pathCons : pathsCons) {
                        if (pathCons.compareTo(inPath) == 0) {
                            inFileRead = true;
                            BufferedReader brCons = new BufferedReader(new FileReader(pathCons));
                            String lineCons;

                            while ((lineCons = brCons.readLine()) != null) {
                                if (lineCons.split(" ").length != 3) ;
                                else {
                                    NodoCons readNodoCons = new NodoCons(lineCons);

                                    int acumulados = readNodoCons.getPtosAc();

                                    if (readNodoCons != null && (acumulados==necesarios || acumulados > necesarios) ){
                                        System.out.println("       " + readNodoCons.makeSerial());
                                    }

                                }
                            }
                        }
                        else if (inFileRead){
                            printAllInFile(pathCons);
                        }
                    }
                }
            }
        }
        long finTime = System.currentTimeMillis();
        long deltaTime = finTime - iniTime;

        System.out.println("Tiempo total: " + deltaTime);
        System.out.println("Accesos totales a discos: " + dbProd.getAccessDisk() + dbCons.getAccessDisk());

        for (String path: pathsCons){
            File file = new File(path);
            file.delete();
        }
        for (String path: pathsProd){
            File file = new File(path);
            file.delete();
        }
        File file = new File("Consumidores.txt");
        file.delete();
        File file2 = new File("Productos.txt");
        file2.delete();
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
/*
        Tests t3 = new Tests();
        t3.P2BTree("testP1Inserciones-1000000.txt", "id", 6);
*/

        /*
        Tests t4 = new Tests();
        t4.P2BTreeSearch("testP1Inserciones-1000000.txt", "ptosAc", 6, "32005", "cons");
        */

        Tests t5 = new Tests();
        t5.P3aii(100, 100);



        //Database db = new Database("testP1Inserciones.txt");
        //db.segmentar(1837, "id");

        /*
        for(int i : t.getN()){
            System.out.println(i);
        }
        */

    }

}
