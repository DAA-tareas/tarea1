public class NodoCons extends AbstractNodo {

    private String rut;
    private int ptosAc;

    public NodoCons(int i, String s, int p){
        super(i);
        this.rut = s;
        this.ptosAc = p;
    }

    public NodoCons(String atributos){
        super(Integer.parseInt(atributos.split(" ")[0]));
        String[] atrSpl = atributos.split(" ");
        this.rut = atrSpl[1];
        this.ptosAc = Integer.parseInt(atrSpl[2]);
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
        return id + " " + rut + " " + p;
    }
}
