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
        return id + " " + rut + " " + p + "\r\n";
    }

    public int compareBy(Nodo p, String f){
        return p.compareToNodoCons(this, f);
    }

    public int compareToNodoCons(NodoCons p , String f){
        if (f.equals("id")) {
            return this.getId() - p.getId();
        } else if (f.equals("rut")){
            return this.getRut().compareTo(p.getRut());
        } else if (f.equals("ptosAc")){
            return this.getPtosAc() - p.getPtosAc();
        } else {
            return 0;
        }
    }

    public int compareToNodoProd(NodoProd p, String f){
        return this.getPtosAc() - p.getPtosNec();
    }

    public String getAttribute(String a){
        if(a.equals("id"))
            return Integer.toString(this.getId());
        else if( a.equals("rut"))
            return this.getRut();
        else
            return Integer.toString(this.getPtosAc());
    }
}
