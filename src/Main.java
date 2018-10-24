import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        NodoCons nodo = new NodoCons(1, "Max", 10);
        System.out.println(nodo.makeSerial());

        Map<String, Boolean> d = new HashMap<String, Boolean>();
        for(int i=0; i<10000000; i++){
            d.put("19605689-"+i, true);
        }

        System.out.println(d.size());

    }
}
