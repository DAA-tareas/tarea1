import java.io.IOException;
import java.lang.reflect.Field;

public class bTree {
    // max children per B-tree node = M-1
    // (must be even and greater than 2)
    private static final int M = 4;

    private NodoAB root;       // root of the B-tree
    private int height;      // height of the B-tree
    private int n;           // number of key-value pairs in the B-tree

    // helper B-tree node data type
    private static final class NodoAB {
        private int m;                             // number of children
        private Entry[] children = new Entry[M];   // the array of children
        Database db;

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
        public Entry(String key, Nodo val, NodoAB next) {
            this.key  = key;
            this.val  = val;
            this.next = next;
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
    public Nodo get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return search(root, key, height);
    }

    private Nodo search(NodoAB x, String key, int ht) {
        Entry[] children = x.children;

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (eq(key, children[j].key)) return children[j].val;
            }
        }

        // internal node
        else {
            for (int j = 0; j < x.m; j++) {
                if (j+1 == x.m || less(key, children[j+1].key))
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
    public void put(String key, Nodo val) throws NoSuchFieldException, IllegalAccessException, IOException {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null");
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
        if(keyN instanceof String) u = insert(root, (String)keyN, val, height);
        else u = insert(root, Integer.toString((Integer) keyN), val, height);

        n++;
        if (u == null) return;

        // need to split root
        NodoAB t = new NodoAB(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    private NodoAB insert(NodoAB h, String key, Nodo val, int ht) throws IOException{
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
                    NodoAB u = insert(h.children[j++].next, key, val, ht-1);
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
        /*h.db = new Database(key);
        h.db.add(val);*/
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
                s.append(indent + children[j].key + " " + children[j].val.makeSerial() + "\n");
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

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }


    /**
     * Unit tests the {@code BTree} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args){
        try {
            bTree st = new bTree();

            Nodo nodo = new NodoCons(17, "19187331-1", 2001);
            Nodo nodo2 = new NodoCons(16, "19187331-2", 2002);
            Nodo nodo3 = new NodoCons(15, "19187331-3", 2003);
            Nodo nodo4 = new NodoCons(14, "19187331-4", 2004);
            Nodo nodo5 = new NodoCons(13, "19187331-5", 2005);
            Nodo nodo6 = new NodoCons(12, "19187331-6", 2006);
            Nodo nodo7 = new NodoCons(11, "19187331-7", 2007);
            Nodo nodo8 = new NodoCons(10, "19187331-8", 2008);
            Nodo nodo9 = new NodoCons(9, "19187331-9", 2009);
            Nodo nodo10 = new NodoCons(8, "19187331-10", 20010);
            Nodo nodo11 = new NodoCons(7, "19187331-11", 20011);
            Nodo nodo12 = new NodoCons(6, "19187331-12", 20012);
            Nodo nodo13 = new NodoCons(5, "19187331-13", 20013);
            Nodo nodo14 = new NodoCons(4, "19187331-14", 20014);
            Nodo nodo15 = new NodoCons(3, "19187331-15", 20015);
            Nodo nodo16 = new NodoCons(2, "19187331-16", 20016);
            Nodo nodo17 = new NodoCons(1, "19187331-17", 20017);

            st.put("id", nodo5);
            st.put("id", nodo11);
            st.put("id", nodo4);
            st.put("id", nodo14);
            st.put("id", nodo7);
            st.put("id", nodo13);
            st.put("id", nodo6);
            st.put("id", nodo12);
            st.put("id", nodo2);
            st.put("id", nodo10);
            st.put("id", nodo15);
            st.put("id", nodo16);
            st.put("id", nodo3);
            st.put("id", nodo8);
            st.put("id", nodo17);
            st.put("id", nodo);
            st.put("id", nodo9);


            System.out.println("cs.princeton.edu:  " + st.get("www.cs.princeton.edu"));
            System.out.println("hardvardsucks.com: " + st.get("www.harvardsucks.com"));
            System.out.println("simpsons.com:      " + st.get("www.simpsons.com"));
            System.out.println("apple.com:         " + st.get("www.apple.com"));
            System.out.println("ebay.com:          " + st.get("www.ebay.com"));
            System.out.println("dell.com:          " + st.get("www.dell.com"));
            System.out.println();

            System.out.println("size:    " + st.size());
            System.out.println("height:  " + st.height());
            System.out.println(st);
            System.out.println();
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