import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Database {
    //estructura subyacente: archivo.txt
    private String path;

    /**
     *
     * @param file
     * @throws IOException
     */
    public Database(String file){
        this.path = file;
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


    public String serialize(ArrayList<Nodo> nList){
        StringBuffer sb = new StringBuffer();
        for(Nodo n : nList){
            sb.append(n.makeSerial() + "\r\n");
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