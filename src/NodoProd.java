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
}
