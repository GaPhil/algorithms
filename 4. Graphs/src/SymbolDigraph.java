import edu.princeton.cs.algorithms.ST;
import edu.princeton.cs.introcs.In;

public class SymbolDigraph {

    private ST<String, Integer> st;
    private String[] keys;
    private Digraph G;

    public SymbolDigraph(String stream, String sp) {
        st = new ST<String, Integer>();
        In in = new In(stream);                     // First pass
        while (in.hasNextLine()) {                  // builds the index
            String[] a = in.readLine().split(sp);   // by reading strings
            for (int i = 0; i < a.length; i++) {    // to associate each
                if (!st.contains(a[i])) {           // distinct string
                    st.put(a[i], st.size());        // with an index
                }
            }
        }
        keys = new String[st.size()];               // Inverted index
        for (String name : st.keys()) {             // to get string keys
            keys[st.get(name)] = name;              // is an array.
        }

        G = new Digraph(st.size());
        in = new In(stream);                        // Second pass
        while (in.hasNextLine()) {                  // builds the graph
            String[] a = in.readLine().split(sp);   // by connecting the
            int v = st.get(a[0]);                   // first vertex
            for (int i = 1; i < a.length; i++) {    // on each line
                G.addEdge(v, st.get(a[i]));         // to all the others.
            }
        }
    }

    public boolean contains(String s) {
        return st.contains(s);
    }

    public int indexOf(String s) {
        return st.get(s);
    }

    public String nameOf(int v) {
        return keys[v];
    }

    public Digraph G() {
        return G;
    }

    public Digraph digraph() {
        return G;
    }
}