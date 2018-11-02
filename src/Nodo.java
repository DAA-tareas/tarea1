
public interface Nodo {
    public int getId();
    public String makeSerial();
    public int compareBy(Nodo p, String f);
    public int compareToNodoCons(NodoCons p , String f);
    public int compareToNodoProd(NodoProd p, String f);
    public String getAttribute(String a);
}
