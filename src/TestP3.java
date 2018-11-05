import java.io.*;
import java.util.*;


public class P3 {

    public Database insertClients(int cantidad, String folder) throws IOException{
        Database dbClientes = new Database(folder + "/clientes.txt");
        List<Nodo> nodoList = new ArrayList<>();

        List<Integer> randomList = new ArrayList<>();
        //Generar datos random
        for(int n=1; n<=cantidad; n++){
            randomList.add(n);
        }
        Collections.shuffle(randomList);

        for(int n : randomList){
            Random random = new Random();
            // NodoCons : id, rut, puntosAcumulados
            Nodo nodo = new NodoCons(n, Integer.toString(10000000+n) + "-" + random.nextInt(10), random.nextInt(20000));
            nodoList.add(nodo);
            //Realizar inserciones
            if (n%Math.pow(10,5) == 0) {
                if(n > 0) {
                    dbClientes.addToFolder(nodoList, folder);
                    nodoList = new ArrayList<>();
                }
            }
        }

        if (nodoList.size() > 0) {
            dbClientes.addToFolder(nodoList, folder);
        }

        System.out.println("Insertado: " + cantidad + " clientes.");

        return dbClientes;
    }


    public Database insertProductos(int cantidad, String folder) throws IOException{
        Database dbProductos = new Database(folder + "/productos.txt");
        List<Nodo> nodoList = new ArrayList<>();

        List<Integer> randomList = new ArrayList<>();
        //Generar datos random
        for(int n=1; n<=cantidad; n++){
            randomList.add(n);
        }
        Collections.shuffle(randomList);

        for(int n : randomList){
            Random random = new Random();
            // NodoProd : id, precio, puntosNecesarios (45% del precio), puntosRecompensa (3% del precio)
            int precio = random.nextInt(100000) + 100;
            Nodo nodo = new NodoProd(n, precio, random.nextInt(precio*70/100), random.nextInt(precio*10/100));
            nodoList.add(nodo);
            //Realizar inserciones
            if (n%Math.pow(10,5) == 0) {
                if(n > 0) {
                    dbProductos.addToFolder(nodoList, folder);
                    nodoList = new ArrayList<>();
                }
            }
        }

        if (nodoList.size() > 0) {
            dbProductos.addToFolder(nodoList, folder);
        }

        System.out.println("Insertado: " + cantidad + " productos.");

        return dbProductos;
    }

    public void printAllInFile(String path) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(path));
        //Busqueda sobre el archivo
        String line;
        while((line = br.readLine()) != null){
            Nodo readNodo = null;
            // Nodo Cons
            if (line.split(" ").length == 3) {
                readNodo = new NodoCons(line);
            }
            // Nodo Prod
            else if(line.split(" ").length == 4){
                readNodo = new NodoProd(line);
            }
            System.out.println(readNodo.makeSerial());
        }

    }

    public String P3ai(Database dbCons, Database dbProd,Boolean print) throws IOException, NoSuchFieldException, IllegalAccessException{

        P3 p = new P3();

        dbCons.bTreeIni();

        Map<String, Nodo> m = dbCons.firstOfPaths();
        for (Map.Entry<String, Nodo> entry : m.entrySet()){
            dbCons.insertBTree("ptosAc", entry.getValue(), entry.getKey());
        }

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
                    if(print) System.out.println(readNodo.makeSerial());

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
                                        if(print) System.out.println("       " + readNodoProd.makeSerial());
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
        return "Tiempo: " + deltaTime + "ms, accesos a disco: " + dbProd.getAccessDisk() + dbCons.getAccessDisk();
    }

    public String P3aii(Database dbCons, Database dbProd, Boolean print) throws IOException, NoSuchFieldException, IllegalAccessException{

        dbCons.bTreeIni();
        Map<String, Nodo> m = dbCons.firstOfPaths();
        for (Map.Entry<String, Nodo> entry : m.entrySet()){
            dbCons.insertBTree("ptosAc", entry.getValue(), entry.getKey());
        }

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
                    if(print) System.out.println(readNodo.makeSerial());

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
                                        if(print) System.out.println("       " + readNodoCons.makeSerial());
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

        return "Tiempo: " + deltaTime + "ms, accesos a disco: " + dbProd.getAccessDisk() + dbCons.getAccessDisk();
    }

    public String listConsumers(Database dbCl, Database dbPr, boolean print) throws IOException{
        List<NodoProd> listadoProds = new ArrayList<>();

        boolean read = true;

        //Abrir archivos ordenados
        BufferedReader brCl = new BufferedReader(new FileReader(dbCl.getFinalMergedPath()));
        BufferedReader brPr = new BufferedReader(new FileReader(dbPr.getFinalMergedPath()));

        String lineCons = "ini";
        String lineProd = "";
        long iniTime = System.currentTimeMillis();

        //Para cada consumidor
        while(brCl.ready()){
            if((lineCons = brCl.readLine()) == null) break;
            if(lineCons.split(" ").length == 3){
                NodoCons cons = new NodoCons(lineCons);
                //manteniendo cursor en tabla productos
                while (brPr.ready()) {
                    //Listar consumidor con todos los productos que puede llevar (sin incluir el del cursor)
                    if(print) {
                        System.out.println("ID cliente: " + cons.getId());
                        System.out.print("ID Productos: ");

                        for (NodoProd n : listadoProds) {
                            System.out.print(n.getId() + " ");
                        }
                        System.out.println();
                    }
                    if (read) {
                        if ((lineProd = brPr.readLine().trim()) == "")
                            break;
                    }
                    read = true; // puede seguir leyendo el sgte elemento
                    if (lineProd.trim() != "") {
                        NodoProd prod = new NodoProd(lineProd);
                        //Si el cliente puede canjearlo, agregarlo al listado
                        if (cons.getPtosAc() >= prod.getPtosNec()) {
                            listadoProds.add(prod);
                        } else {
                            //Si no puede canjearlo, pasar al siguiente consumidor
                            read = false; // no leer el siguiente elemento
                            break;
                        }
                    }
                }
            }

        }

        long finTime = System.currentTimeMillis();

        long deltaTime = finTime - iniTime;


        return "Tiempo: " + deltaTime + "ms, accesos a disco: " + dbCl.getAccessDisk() + dbPr.getAccessDisk();
    }


    public static void deleteFiles(String defDel){
        File dir = new File(defDel);

        for (File file: dir.listFiles()) {
            if (!file.isDirectory())
                file.delete();
        }

        dir.delete();
    }

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException{

        Boolean deleteFiles = true;

        P3 p = new P3();

        double C[] = {Math.pow(10, 1), Math.pow(10, 2), Math.pow(10, 3)};
        double P[] = {Math.pow(10, 1), Math.pow(10, 2), Math.pow(10, 3), Math.pow(10, 4)};


        System.out.println("a.i");

        List<Database> basesClai = new ArrayList<>();
        List<Database> basesPrai = new ArrayList<>();

        for(double i : C){
            Database dbClientes = p.insertClients((int)i, "clientesai" + (int)i);
            // Ordenar segun puntos Actumulados
            dbClientes.ordenar("ptosAc");
            basesClai.add(dbClientes);
        }
        for(double i : P){
            Database dbProductos = p.insertProductos((int)i, "productosai" + (int)i);
            // Ordenar segun puntos necesarios
            dbProductos.ordenar("ptosNec");
            basesPrai.add(dbProductos);
        }

        List<String> dbVsai = new ArrayList<>();
        List<String> timesAndDiskListedai = new ArrayList<>();

        //Listar clientes con sus productos
        for(Database cl : basesClai){
            for(Database pr : basesPrai){
                // colocar false, solo arroja los resultados finales (sin el print de cada consumidor con su listado)
                String timeAndDisk = p.P3ai(cl, pr, false);
                timesAndDiskListedai.add(timeAndDisk);
                dbVsai.add(cl.getPath().split("/")[0] + " v/s " + pr.getPath().split("/")[0]);
                //System.out.println(cl.getPath().split("/")[0] + " v/s " + pr.getPath().split("/")[0] + ": " + time + "ms");
            }
        }

        if(deleteFiles) {
            for (double i : C) {
                deleteFiles("clientesai" + (int) i);
            }
            for (double i : P) {
                deleteFiles("productosai" + (int) i);
            }
        }


        for(int i=0; i<dbVsai.size(); i++){
            System.out.println(dbVsai.get(i) + ": " + timesAndDiskListedai.get(i));
        }

        System.out.println("a.ii");

        List<Database> basesClaii = new ArrayList<>();
        List<Database> basesPraii = new ArrayList<>();

        for(double i : C){
            Database dbClientes = p.insertClients((int)i, "clientesaii" + (int)i);
            // Ordenar segun puntos Actumulados
            dbClientes.ordenar("ptosAc");
            basesClaii.add(dbClientes);
        }
        for(double i : P){
            Database dbProductos = p.insertProductos((int)i, "productosaii" + (int)i);
            // Ordenar segun puntos necesarios
            dbProductos.ordenar("ptosNec");
            basesPraii.add(dbProductos);
        }

        List<String> dbVsaii = new ArrayList<>();
        List<String> timesAndDiskListedaii = new ArrayList<>();

        //Listar clientes con sus productos
        for(Database cl : basesClaii){
            for(Database pr : basesPraii){
                // colocar false, solo arroja los resultados finales (sin el print de cada consumidor con su listado)
                String timeAndDisk = p.P3aii(cl, pr, false);
                timesAndDiskListedaii.add(timeAndDisk);
                dbVsaii.add(cl.getPath().split("/")[0] + " v/s " + pr.getPath().split("/")[0]);
                //System.out.println(cl.getPath().split("/")[0] + " v/s " + pr.getPath().split("/")[0] + ": " + time + "ms");
            }
        }

        if(deleteFiles) {
            for (double i : C) {
                deleteFiles("clientesaii" + (int) i);
            }
            for (double i : P) {
                deleteFiles("productosaii" + (int) i);
            }
        }

        for(int i=0; i<dbVsaii.size(); i++){
            System.out.println(dbVsaii.get(i) + ": " + timesAndDiskListedaii.get(i));
        }


        System.out.println("b");

        List<Database> basesCl = new ArrayList<>();
        List<Database> basesPr = new ArrayList<>();

        for(double i : C){
            Database dbClientes = p.insertClients((int)i, "clientesb" + (int)i);
            // Ordenar segun puntos Actumulados
            dbClientes.ordenar("ptosAc");
            basesCl.add(dbClientes);
        }
        for(double i : P){
            Database dbProductos = p.insertProductos((int)i, "productosb" + (int)i);
            // Ordenar segun puntos necesarios
            dbProductos.ordenar("ptosNec");
            basesPr.add(dbProductos);
        }

        List<String> dbVs = new ArrayList<>();
        List<String> timesListed = new ArrayList<>();

        //Listar clientes con sus productos
        for(Database cl : basesCl){
            for(Database pr : basesPr){
                // colocar false, solo arroja los resultados finales (sin el print de cada consumidor con su listado)
                String time = p.listConsumers(cl, pr, false);
                timesListed.add(time);
                dbVs.add(cl.getPath().split("/")[0] + " v/s " + pr.getPath().split("/")[0]);
                //System.out.println(cl.getPath().split("/")[0] + " v/s " + pr.getPath().split("/")[0] + ": " + time + "ms");
            }
        }


        if(deleteFiles) {
            for (double i : C) {
                deleteFiles("clientesb" + (int) i);
            }
            for (double i : P) {
                deleteFiles("productosb" + (int) i);
            }

            for (int i = 0; i < dbVs.size(); i++) {
                System.out.println(dbVs.get(i) + ": " + timesListed.get(i));
            }
        }


    }

}
