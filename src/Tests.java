import java.lang.StringBuffer;
import java.io.IOException;

public class Tests {

    public int[] N;

    public Tests(){
        N = new int[7];
        for(int i=0; i<N.length; i++){
            this.N[i] = (int) Math.pow(10, i+1);
        }
    }

    public long[] P1Inserciones() throws IOException{
        Database db = new Database("testP1Inserciones.txt");
        long[] times = new long[N.length];
        StringBuffer buf = new StringBuffer();
        int[] diskAccess = new int[7];

        for(int i=0; i<N.length; i++){
            long iniTime = System.currentTimeMillis();
            for(int n=0; n<this.N[i]; n++){
                buf.append("id" + n + " rut" + n + " ptosRec" + n + "\r\n");
                //Realizar inserciones
                if (n%Math.pow(10,5) == 0) {
                    db.add(buf.toString());
                    buf = new StringBuffer();
                    diskAccess[i]++;
                }
            }
            if (buf.length() > 0) {
                db.add(buf.toString());
                diskAccess[i]++;
            }

            long finTime = System.currentTimeMillis();

            times[i] = finTime - iniTime;
        }
        System.out.println("Accesos totales a discos");
        for (int i = 0; i<diskAccess.length; i++){
            int j = i + 1;
            System.out.print(diskAccess[i]);
        }
        return times;
    }

    int[] getN(){
        return this.N;
    }

    public static void main(String[] args) throws IOException{
        Tests t = new Tests();
        long[] p1 = t.P1Inserciones();
        System.out.println();
        System.out.println("Timepos en milisegundos");
        for(long l : p1){
            System.out.print(l + " ");
        }
        /*
        for(int i : t.getN()){
            System.out.println(i);
        }
        */

    }

}
