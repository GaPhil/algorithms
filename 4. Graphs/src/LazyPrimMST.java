import edu.princeton.cs.algorithms.MinPQ;
import edu.princeton.cs.algorithms.Queue;

public class LazyPrimMST {

    private boolean[] marked;           // MST vertices
    private double weight;              // total weight of MST
    private Queue<Edge> mst;            // MST edges
    private MinPQ<Edge> pq;             // crossing (and ineligible) edges

    public LazyPrimMST(EdgeWeightedGraph G) {
        pq = new MinPQ<Edge>();
        marked = new boolean[G.V()];
        mst = new Queue<Edge>();

        visit(G, 0);                    // assumes G is connected
        while (!pq.isEmpty()) {
            Edge e = pq.delMin();       // Get lowest-weight
            int v = e.either();         // edge from pq.
            int w = e.other(v);
            if (marked[v] && marked[w]) {
                continue;               // skip if ineligible
            }
            mst.enqueue(e);             // Add edge to tree.
            if (!marked[v]) {
                visit(G, v);            // Add vertex to tree
            }
            if (!marked[w]) {
                visit(G, w);            // (either v or w).
            }
        }
    }

    private void visit(EdgeWeightedGraph G, int v) {
        // Mark v and add to pq all edges from v to unmarked vertices.
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            if (!marked[e.other(v)]) {
                pq.insert(e);
            }
        }
    }

    public Iterable<Edge> edges() {
        return mst;
    }

    public double weight() {
        return weight;
    }
}