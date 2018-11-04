import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class P3 {

    public Database insertClients(int cantidad) throws IOException{
        Database dbClientes = new Database("clientes/clientes.txt");
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
                    dbClientes.addToFolder(nodoList, "clientes");
                    nodoList = new ArrayList<>();
                }
            }
        }

        if (nodoList.size() > 0) {
            dbClientes.addToFolder(nodoList, "clientes");
        }

        System.out.println("Insertado: " + cantidad + " clientes.");

        return dbClientes;
    }


    public Database insertProductos(int cantidad) throws IOException{
        Database dbProductos = new Database("productos/productos.txt");
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
                    dbProductos.addToFolder(nodoList, "productos");
                    nodoList = new ArrayList<>();
                }
            }
        }

        if (nodoList.size() > 0) {
            dbProductos.addToFolder(nodoList, "productos");
        }

        System.out.println("Insertado: " + cantidad + " productos.");

        return dbProductos;
    }

    public void listConsumers(Database dbCl, Database dbPr) throws IOException{
        List<NodoProd> listadoProds = new ArrayList<>();

        boolean read = true;

        //Abrir archivos ordenados
        BufferedReader brCl = new BufferedReader(new FileReader(dbCl.getFinalMergedPath()));
        BufferedReader brPr = new BufferedReader(new FileReader(dbPr.getFinalMergedPath()));

        String lineCons = "ini";
        String lineProd = "";

        //Para cada consumidor
        while(brCl.ready()){
            if((lineCons = brCl.readLine().trim()) != ""){
                System.out.println(lineCons == "");
                System.out.println("que es esto" + lineCons + "esto");
                NodoCons cons = new NodoCons(lineCons);
                //manteniendo cursor en tabla productos
                while (brPr.ready()) {
                    //Listar consumidor con todos los productos que puede llevar (sin incluir el del cursor)
                    System.out.println("ID cliente: " + cons.getId());
                    System.out.print("ID Productos: ");
                    for (NodoProd n : listadoProds) {
                        System.out.print(n.getId() + " ");
                    }
                    System.out.println();
                    if (read) {
                        if ((lineProd = brPr.readLine().trim()) == "")
                            break;
                    }
                    read = true; // puede seguir leyendo el sgte elemento
                    if (lineProd.trim() != "") {
                        NodoProd prod = new NodoProd(lineProd);
                        //Si el cliente puede canjearlo, agregarlo al listado
                        System.out.println(cons.getPtosAc() + " " + prod.getPtosNec());
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

    }

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException{
        P3 p = new P3();
        Database dbClientes = p.insertClients(10);
        Database dbProductos = p.insertProductos(10);

        // Ordenar segun puntos Actumulados
        dbClientes.ordenar("ptosAc");
        // Ordenar segun puntos necesarios
        dbProductos.ordenar("ptosNec");

        //Listar clientes con sus productos
        p.listConsumers(dbClientes, dbProductos);

    }

}
