import java.io.IOException;
import java.lang.reflect.Field;

public class bTree {
    // max children per B-tree node = M-1
    // (must be even and greater than 2)
    private static final int M = 10;

    private NodoAB root;       // root of the B-tree
    private int height;      // height of the B-tree
    private int n;           // number of key-value pairs in the B-tree
    private String indexedType;

    // helper B-tree node data type
    private static final class NodoAB {
        private int m;                             // number of children
        private Entry[] children = new Entry[M];   // the array of children

        // create a node with k children
        private NodoAB(int k) {
            m = k;
        }
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry {
        private String key;
        private final Nodo val;
        private NodoAB next;     // helper field to iterate over array entries
        String path;
        public Entry(String key, Nodo val, NodoAB next) {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
        public Entry(String key, Nodo val, NodoAB next, String path) {
            this.key  = key;
            this.val  = val;
            this.next = next;
            this.path = path;
        }
    }

    /**
     * Initializes an empty B-tree.
     */
    public bTree() {
        root = new NodoAB(0);
    }

    /**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Returns the height of this B-tree (for debugging).
     *
     * @return the height of this B-tree
     */
    public int height() {
        return height;
    }


    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public String get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return search(root, key, height);
    }

    private String search(NodoAB x, String key, int ht) {
        Entry[] children = x.children;

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (greater(key, children[j].key)){
                    return children[j].path;
                }
            }
        }

        // internal node
        else {
            for (int j = 0; j < x.m; j++) {
                if (j+1 == x.m || greater(key, children[j+1].key))
                    return search(children[j].next, key, ht-1);
            }
        }
        return null;
    }


    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Nodo val, String path) throws NoSuchFieldException, IllegalAccessException, IOException {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null");
        this.indexedType = key;
        Field field;
        try{
            field = val.getClass().getDeclaredField(key);
        }
        catch (NoSuchFieldException e){
            field = val.getClass().getSuperclass().getDeclaredField(key);
        }
        field.setAccessible(true);

        Object keyN = field.get(val);
        NodoAB u;
        if(keyN instanceof String) u = insert(root, (String)keyN, val, height, path);
        else u = insert(root, Integer.toString((Integer) keyN), val, height, path);

        n++;
        if (u == null) return;

        // need to split root
        NodoAB t = new NodoAB(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    private NodoAB insert(NodoAB h, String key, Nodo val, int ht, String path) throws IOException{
        int j;
        Entry t = new Entry(key, val, null);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.children[j].key)) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    NodoAB u = insert(h.children[j++].next, key, val, ht-1, path);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--)
            h.children[i] = h.children[i-1];
        h.children[j] = t;
        //h.path = path;
        t.path = path;

        h.m++;
        if (h.m < M) return null;
        else         return split(h);
    }

    // split node in half
    private NodoAB split(NodoAB h) {
        NodoAB t = new NodoAB(M/2);
        h.m = M/2;
        for (int j = 0; j < M/2; j++)
            t.children[j] = h.children[M/2+j];
        return t;
    }

    /**
     * Returns a string representation of this B-tree (for debugging).
     *
     * @return a string representation of this B-tree.
     */
    public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(NodoAB h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s.append(indent + children[j].path + " " + children[j].val.makeSerial()  + "\n");
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s.append(indent + "(" + children[j].key + ")\n");
                s.append(toString(children[j].next, ht-1, indent + "     "));
            }
        }
        return s.toString();
    }


    // comparison functions - make Comparable instead of Key to avoid casts
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean greater(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) > 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }


    public String getIndexedType(){
        return indexedType;
    }

    /**
     * Unit tests the {@code BTree} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args){
        try {
            bTree st = new bTree();

            Nodo nodo = new NodoCons(1, "19187331-1", 2001);
            Nodo nodo2 = new NodoCons(2, "19187331-2", 2002);
            Nodo nodo3 = new NodoCons(3, "19187331-3", 2003);
            Nodo nodo4 = new NodoCons(4, "19187331-4", 2004);
            Nodo nodo5 = new NodoCons(5, "19187331-5", 2005);
            Nodo nodo6 = new NodoCons(6, "19187331-6", 2006);
            Nodo nodo7 = new NodoCons(7, "19187331-7", 2007);
            Nodo nodo8 = new NodoCons(8, "19187331-8", 2008);
            Nodo nodo9 = new NodoCons(9, "19187331-9", 2009);
            Nodo nodo10 = new NodoCons(10, "19187331-10", 20010);
            Nodo nodo11 = new NodoCons(11, "19187331-11", 20011);
            Nodo nodo12 = new NodoCons(12, "19187331-12", 20012);
            Nodo nodo13 = new NodoCons(13, "19187331-13", 20013);
            Nodo nodo14 = new NodoCons(14, "19187331-14", 20014);
            Nodo nodo15 = new NodoCons(15, "19187331-15", 20015);
            Nodo nodo16 = new NodoCons(16, "19187331-16", 20016);
            Nodo nodo17 = new NodoCons(17, "19187331-17", 20017);
            Nodo nodo18 = new NodoCons(18, "19187331-17", 20017);
            Nodo nodo19 = new NodoCons(19, "19187331-17", 20017);
            Nodo nodo20 = new NodoCons(20, "19187331-17", 20017);
            Nodo nodo21 = new NodoCons(21, "19187331-17", 20017);
            Nodo nodo22 = new NodoCons(22, "19187331-17", 20017);
            Nodo nodo23 = new NodoCons(23, "19187331-17", 20017);
            Nodo nodo24 = new NodoCons(24, "19187331-17", 20017);
            Nodo nodo25 = new NodoCons(25, "19187331-17", 20017);

            st.put("id", nodo5, "5");
            st.put("id", nodo11, "11");
            st.put("id", nodo4, "4");
            st.put("id", nodo14, "14");
            st.put("id", nodo7, "7");
            st.put("id", nodo13 , "r");
            st.put("id", nodo6 , "t");
            st.put("id", nodo12 , "y");
            st.put("id", nodo2 , "u");
            st.put("id", nodo10 , "o");
            st.put("id", nodo15 , "p");
            st.put("id", nodo16 , "a");
            st.put("id", nodo3, "s" );
            st.put("id", nodo8 , "d");
            st.put("id", nodo17 , "f");
            st.put("id", nodo , "g");
            st.put("id", nodo9, "h");
            st.put("id", nodo18, "j");
            st.put("id", nodo19, "k");
            st.put("id", nodo20, "l");
            st.put("id", nodo21, "b");
            st.put("id", nodo22, "v");
            st.put("id", nodo23, "c");
            st.put("id", nodo24, "x");
            st.put("id", nodo25, "z");


            //System.out.println("cs.princeton.edu:  " + st.get("3").makeSerial());
            System.out.println("cs.princeton.edu:  " + st.get("www.cs.princeton.edu"));
            System.out.println("hardvardsucks.com: " + st.get("www.harvardsucks.com"));
            System.out.println("simpsons.com:      " + st.get("www.simpsons.com"));
            System.out.println("apple.com:         " + st.get("www.apple.com"));
            System.out.println("ebay.com:          " + st.get("www.ebay.com"));
            System.out.println("dell.com:          " + st.get("www.dell.com"));
            System.out.println();

            System.out.println("size:    " + st.size());
            System.out.println("height:  " + st.height());
            System.out.println(st.toString());

        }
        catch (NoSuchFieldException e){
            System.out.println("no such field");
        }
        catch (IllegalAccessException b){
            System.out.println("Illegal Acces");

        }
        catch (IOException e){
            System.out.println("no such field");
        }
    }

}