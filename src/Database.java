import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    //estructura subyacente: archivo.txt
    private String path;
    private List <String> secondaryPaths;
    private int B;
    private int accessDisk;


    public Database(String filepath){
        this.path = filepath;
        this.B = (int)Math.pow(10, 5);
        secondaryPaths = new ArrayList<>();
    }

    public int getAccessDisk(){
        return this.accessDisk;
    }

    void add(Nodo toAdd) throws IOException{
        ArrayList<Nodo> a = new ArrayList<Nodo>();
        a.add(toAdd);
        add(a);
    }

    void add(List<Nodo> nodoList) throws IOException{
        BufferedWriter out = null;
        try {
            String toAdd = serialize(nodoList);
            FileWriter fstream = new FileWriter(this.path, true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(toAdd + "\r\n");
        }
        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        finally {
            if(out != null) {
                out.close();
            }
        }
    }


    /**metodo que escribe la info del archivo en archivos de tamaño B
     */
    public void segmentar(String field) throws IOException{
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int nFile = 1;
        Ordenador ord = new Ordenador();
        while(br.ready()){
            // Acceso a disco
            this.accessDisk++;
            List<Nodo> lista = new ArrayList<>();
            String linea;

            for (int i = 0; i < this.B; i++){
                Nodo nodo;

                if((linea = br.readLine()) == null) break;

                String[] aNodo = linea.split(" ");
                //System.out.println("Largo lista: " + aNodo.length);
                /*
                System.out.println(aNodo[0]);
                System.out.println(aNodo[1]);
                System.out.println(aNodo[2]);
                */

                if (aNodo.length==4){
                    nodo = new NodoProd(Integer.valueOf(aNodo[0]), Integer.valueOf(aNodo[1]), Integer.valueOf(aNodo[2]), Integer.valueOf(aNodo[3]));
                    lista.add(nodo);
                }
                else if(aNodo.length == 3){
                    nodo = new NodoCons(Integer.valueOf(aNodo[0]), aNodo[1], Integer.valueOf(aNodo[2]));
                    lista.add(nodo);
                }

            }


            List<Nodo> listaOrdenada =  ord.ordenarSec(lista, field);

            Database db = new Database(nFile + ".txt");
            db.add(listaOrdenada);

            this.secondaryPaths.add(nFile + ".txt");
            nFile++;
            // Acceso a disco - escritura
            this.accessDisk++;

        }


    }

    public String serialize(List<Nodo> nList){
        StringBuffer sb = new StringBuffer();
        for(Nodo n : nList){
            sb.append(n.makeSerial());
        }
        return sb.toString();
    }

    /**
     * Metodo que ordena segun un atributo
     * Utiliza MergeSort
     * @param field atributo por el que ordena
     */
    void ordenar(String field) throws IOException{
        // Del archivo enorme (llamese A)
        // Crear k = |A|/10^5 bloques con 10^5 nodos cada uno y ordenarlos
        this.segmentar(field);
        int numIter = 1;
        // Mientras no se hayan mergeado todos los archivos
        while(this.secondaryPaths.size() != 1){
            // Agrupar de a 2 bloques, leer 10^5/2 nodos de cada bloque
            this.merger(field, numIter);
            numIter++;
        }


    }

    public void merger(String mergeAttr, int numIter) throws IOException{
        // Hacer una copia de los secondaryPaths
        List<String> copySecPaths = new ArrayList<>();
        copySecPaths.addAll(this.secondaryPaths);
        // Clear secondaryPaths
        this.secondaryPaths.clear();
        // Cada 2 archivos
        for(int i=0; i<copySecPaths.size(); i+=2){
            // Numero impar de Archivos => El ultimo archivo se escribe directamente
            if(i == copySecPaths.size() - 1 && copySecPaths.size() % 2 != 0){ //
                this.secondaryPaths.add(copySecPaths.get(i));
                break;
            }
            // Acceso a disco - 2 lecturas de archivos
            this.accessDisk += 2;
            // 2 Archivos .txt
            String file1 = copySecPaths.get(i);
            String file2 = copySecPaths.get(i+1);
            // Nombres de archivos sin .txt
            String name1 = file1.split(".txt")[0];
            String name2 = file2.split(".txt")[0];
            String fileToWriteName = "i" + numIter + "-" + name1 + ".txt";
            try {
                // Abrir un buffer para los 2 archivos
                BufferedReader br1 = new BufferedReader(new FileReader(file1));
                BufferedReader br2 = new BufferedReader(new FileReader(file2));
                FileWriter fw = new FileWriter(fileToWriteName, true);
                List<Nodo> buffer1 = new ArrayList<>();
                List<Nodo> buffer2 = new ArrayList<>();
                List<Nodo> bufferToWrite = new ArrayList<>();
                // Mientras existan elementos en algun archivo
                while(br1.ready() || br2.ready()) {
                    // Se lleno el buffer de escritura
                    if(bufferToWrite.size() >= B){
                        // Escribirlo en el archivo
                        fw.write(this.serialize(bufferToWrite));
                        // Vaciar buffer de escritura
                        bufferToWrite = new ArrayList<>();
                    }
                    // Recorrer B/2 elementos de cada archivo. Almacenar Nodos en listas
                    if(buffer1.size() == 0){
                        String el1;
                        int b = 0;
                        while((el1 = br1.readLine()) != null && b < B/2){
                            // Nodo Cons
                            if (el1.split(" ").length == 3) {
                                buffer1.add(new NodoCons(el1));
                                b++;
                            }
                            // Nodo Prod
                            else if(el1.split(" ").length == 4){
                                buffer1.add(new NodoProd(el1));
                                b++;
                            }

                        }
                    }
                    if (buffer2.size() == 0){
                        String el2;
                        int b = 0;
                        while((el2 = br2.readLine()) != null && b < B/2){
                            // Nodo Cons
                            if (el2.split(" ").length == 3) {
                                buffer2.add(new NodoCons(el2));
                                b++;
                            }
                            // Nodo Prod
                            else if(el2.split(" ").length == 4){
                                buffer2.add(new NodoProd(el2));
                                b++;
                            }
                        }
                    }
                    // Mientras el buffer a escribir tenga menos de B elementos
                    while(bufferToWrite.size() < B && buffer1.size() > 0 && buffer2.size() > 0){
                        // Comparar los nodos
                        // Nodo1 < Nodo2 => Se agrega Nodo1 en bufferToWrite
                        if(buffer1.get(0).compareBy(buffer2.get(0), mergeAttr) > 0){
                            bufferToWrite.add(buffer1.get(0));
                            //Borrar elemento
                            buffer1.remove(0);
                        }else{ // Nodo1 >= Nodo2 => Agregar Nodo2 en bufferToWrite
                            bufferToWrite.add(buffer2.get(0));
                            //Borrar elemento
                            buffer2.remove(0);
                        }
                    }
                    // Si se acaba 1 archivo completo, hay que appendiar el restante
                    // Se acabo el br1
                    if(!br1.ready()){
                        //Escribir el br2
                        while(bufferToWrite.size() < B && buffer2.size() > 0){
                            bufferToWrite.add(buffer2.get(0));
                            //Borrar elemento
                            buffer2.remove(0);
                        }
                    }
                    if(!br2.ready()){
                        //Escribir el br2
                        while(bufferToWrite.size() < B && buffer1.size() > 0){
                            bufferToWrite.add(buffer1.get(0));
                            //Borrar elemento
                            buffer1.remove(0);
                        }
                    }

                }
                // Si se acabaron los elementos y no escribio la "cola"
                if(bufferToWrite.size() > 0){
                    // Acceso a disco
                    this.accessDisk++;
                    // Escribirlo en el archivo
                    fw.write(this.serialize(bufferToWrite));
                }
                // Agregar archivo mergeado a secondaryPaths
                this.secondaryPaths.add(fileToWriteName);
                // Cerrar BufferedReaders, FileWriters
                br1.close();
                br2.close();
                fw.close();
                // Borrar archivos br1, br2?

            }
            catch (FileNotFoundException e) {
                System.err.println("Error: " + e.getMessage());
            }

        }
    }

    public List<String> A = new ArrayList<>();

    public static void main(String[] args) throws  IOException{
        Database db = new Database("test.txt");
        db.A.add("El1");
        db.A.add("El2");
        System.out.println(db.A);
        List<String> B = new ArrayList<>();
        B.addAll(db.A);

        System.out.println(B);
        db.A.clear();
        System.out.println(db.A);
        System.out.println(B);


    }
}