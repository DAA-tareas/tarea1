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

    void add(Nodo toAdd) throws IOException{
        ArrayList<Nodo> a = new ArrayList<Nodo>();
        a.add(toAdd);
        add(a);
    }

    void add(ArrayList<Nodo> nodoList) throws IOException{
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


    public String serialize(ArrayList<Nodo> nList){
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
        Nodo n1 = new NodoProd(1, 20, 100, 3);
        Nodo n2 = new NodoProd(2, 30, 150, 8);
        Nodo n3 = new NodoProd(3, 5, 10, 1);
        ArrayList<Nodo> l = new ArrayList<Nodo>();
        l.add(n2);
        l.add(n3);
        db.add(n1);
        db.add(l);

    }
}