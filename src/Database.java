import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

    /**
     * Metodo que ordena segun un atributo
     * Utiliza MergeSort
     * @param attribute atributo por el que ordena
     */
    void ordenar(String attribute){
        return;
    }

    public static void main(String[] args) throws  IOException{
        Database db = new Database("test.txt");

        db.add("Agregando el primer elemento 10");
        db.add("Segundo elemento 11");

    }
}