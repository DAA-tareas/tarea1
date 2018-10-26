public class NodoProd extends AbstractNodo {

    private int precio;
    private int ptosNec;
    private int ptosRec;

    public NodoProd(int i, int p, int n, int r) {
        super(i);
        this.precio = p;
        this.ptosNec = n;
        this.ptosRec = r;
    }

    public NodoProd(String atributos){
        super(Integer.parseInt(atributos.split(" ")[0]));
        String[] atrSpl = atributos.split(" ");
        this.precio = Integer.parseInt(atrSpl[1]);
        this.ptosNec = Integer.parseInt(atrSpl[2]);
        this.ptosRec = Integer.parseInt(atrSpl[3]);
    }

    public int getPrecio(){
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getPtosNec(){
        return ptosNec;
    }

    public void setPtosNec(int ptosNec){
        this.ptosNec = ptosNec;
    }

    public int getPtosRec(){
        return ptosRec;
    }

    public void setPtosRec(int ptosRec){
        this.ptosRec = ptosRec;
    }

    public String makeSerial() {
        String id = Integer.toString(this.getId());
        String pre = Integer.toString(precio);
        String ptsN = Integer.toString(ptosNec);
        String ptsR = Integer.toString(ptosRec);
        return id + " " + pre + " " + ptsN + " " + ptsR;
    }
}
