import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


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

    public long listConsumers(Database dbCl, Database dbPr, boolean print) throws IOException{
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

        return deltaTime;
    }

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException{
        P3 p = new P3();

        double C[] = {Math.pow(10, 1), Math.pow(10, 2), Math.pow(10, 3)};
        double P[] = {Math.pow(10, 1), Math.pow(10, 2), Math.pow(10, 3), Math.pow(10, 4)};

        List<Database> basesCl = new ArrayList<>();
        List<Database> basesPr = new ArrayList<>();

        for(double i : C){
            Database dbClientes = p.insertClients((int)i, "clientes" + (int)i);
            // Ordenar segun puntos Actumulados
            dbClientes.ordenar("ptosAc");
            basesCl.add(dbClientes);
        }
        for(double i : P){
            Database dbProductos = p.insertProductos((int)i, "productos" + (int)i);
            // Ordenar segun puntos necesarios
            dbProductos.ordenar("ptosNec");
            basesPr.add(dbProductos);
        }

        List<String> dbVs = new ArrayList<>();
        List<Long> timesListed = new ArrayList<>();

        //Listar clientes con sus productos
        for(Database cl : basesCl){
            for(Database pr : basesPr){
                // colocar false, solo arroja los resultados finales (sin el print de cada consumidor con su listado)
                long time = p.listConsumers(cl, pr, true);
                timesListed.add(time);
                dbVs.add(cl.getPath().split("/")[0] + " v/s " + pr.getPath().split("/")[0]);
                //System.out.println(cl.getPath().split("/")[0] + " v/s " + pr.getPath().split("/")[0] + ": " + time + "ms");
            }
        }

        for(int i=0; i<dbVs.size(); i++){
            System.out.println(dbVs.get(i) + ": " + timesListed.get(i) + " ms");
        }


    }

}
