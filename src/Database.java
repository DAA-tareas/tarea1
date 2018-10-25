import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    //estructura subyacente: archivo.txt
    private String path;
    private List <String> secondaryPaths;

    /**
     *
     * @param file
     * @throws IOException
     */
    public Database(String file){
        this.path = file;
        this.secondaryPaths = new ArrayList<>();
    }

    void add(String toAdd) throws IOException{
        BufferedWriter out = null;

        try {
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
     * @param B el tamaño de los segmentos
     */
    void segmentar(int B, String field) throws IOException{
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int nFile = 1;
        boolean EOF = false;
        while(!EOF){

            List<Nodo> lista = new ArrayList<>();
            String linea;

            for (int i = 0; i < B; i++){
                Nodo nodo;

                if((linea = br.readLine())==null) {
                    EOF = true;
                    break;
                }
                String[] aNodo = linea.split(" ");

                if (aNodo.length==4){
                    nodo = new NodoProd(Integer.valueOf(aNodo[0]), Integer.valueOf(aNodo[1]), Integer.valueOf(aNodo[2]), Integer.valueOf(aNodo[3]));
                }
                else{
                    nodo = new NodoCons(Integer.valueOf(aNodo[0]), aNodo[1], Integer.valueOf(aNodo[2]));
                }
                lista.add(nodo);
            }

            Ordenador ord = new Ordenador();
            lista =  ord.ordenarSec(lista, field);
            String aArchivo = serialize(lista);


            File fileAux = new File(Integer.toString(nFile) + ".txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileAux));
            bw.write(aArchivo);

            secondaryPaths.add(nFile + ".txt");
            nFile++;

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
     * @param attribute atributo por el que ordena
     */
    void ordenar(String attribute){
        // Del archivo enorme (llamese A)
        // Crear k = |A|/10^5 bloques con 10^5 nodos cada uno
        // Ordenar cada uno de los k bloques particularmente
        // Recursivamente || Caso base: k = 1
        // Agrupar de a 2 bloques, leer 10^5/2 nodos de cada bloque
        // Crear un archivo ordenado, agregando 10^5 nodos hasta acabar elementos de ambos archivos


        return;
    }

    public static void main(String[] args) throws  IOException{
        Database db = new Database("test.txt");

        db.add("Agregando el primer elemento 10");
        db.add("Segundo elemento 11");

    }
}