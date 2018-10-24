public abstract class AbstractNodo {
    private int id;

    public AbstractNodo(int i){
        this.id = i;
    }

    int getId(){
        return id;
    }

    public abstract String makeSerial();

}
