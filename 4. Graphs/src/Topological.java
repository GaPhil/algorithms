import edu.princeton.cs.algorithms.EdgeWeightedDirectedCycle;
import edu.princeton.cs.introcs.StdOut;

public class Topological {

    private Iterable<Integer> order;    // topological order

    public Topological(Digraph G) {

        DirectedCycle cyclefinder = new DirectedCycle(G);
        if (!cyclefinder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();
        }
    }

    public Iterable<Integer> order() {
        return order;
    }

    public Topological(EdgeWeightedDigraph G) {
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
        if (!finder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();
        }
    }

    public boolean isDAG() {
        return order != null;
    }

    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        SymbolDigraph sg = new SymbolDigraph(filename, delimiter);

        Topological top = new Topological(sg.digraph());

        for (int v : top.order()) {
            StdOut.println(sg.nameOf(v));
        }
    }

    public boolean hasOrder() {
        return order != null;
    }
}