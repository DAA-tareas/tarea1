public abstract class AbstractNodo implements Nodo{
    private int id;

    public AbstractNodo(int i){
        this.id = i;
    }

    public int getId(){
        return id;
    }

    public abstract String makeSerial();

    public abstract String getAttribute(String a);

}
