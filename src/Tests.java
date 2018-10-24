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

        for(int i=0; i<this.N.length; i++){
            long iniTime = System.currentTimeMillis();
            for(int n=0; n<this.N[i]; n++){
                //Realizar inserciones
                db.add("id" + n + " rut" + n + "ptosRec" + n);
            }

            long finTime = System.currentTimeMillis();

            times[i] = finTime - iniTime;
        }

        return times;
    }

    int[] getN(){
        return this.N;
    }

    public static void main(String[] args) throws IOException{
        Tests t = new Tests();
        long[] p1 = t.P1Inserciones();
        for(long l : p1){
            System.out.println(l);
        }
        /*
        for(int i : t.getN()){
            System.out.println(i);
        }
        */

    }

}
