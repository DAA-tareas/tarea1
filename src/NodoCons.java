public class NodoCons extends AbstractNodo {

    private String rut;
    private int ptosAc;

    public NodoCons(int i, String s, int p){
        super(i);
        this.rut = s;
        this.ptosAc = p;
    }

    public String getRut(){
        return rut;
    }

    public int getPtosAc(){
        return ptosAc;
    }

    public void setPtosAc(int ptosAc){
        this.ptosAc = ptosAc;
    }

    public String makeSerial() {
        String id = Integer.toString(this.getId());
        String p = Integer.toString(ptosAc);
        return id + " " + rut + " " + p + "\r\n";
    }
}
