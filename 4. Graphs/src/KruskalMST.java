import edu.princeton.cs.algorithms.MinPQ;
import edu.princeton.cs.algorithms.Queue;
import edu.princeton.cs.algorithms.UF;

public class KruskalMST {

    private Queue<Edge> mst;
    private double weight;                                  // weight of MST

    public KruskalMST(EdgeWeightedGraph G) {
        mst = new Queue<Edge>();
        MinPQ<Edge> pq = new MinPQ<Edge>();
        for (Edge e : G.edgs()) {
            pq.insert(e);
        }
        UF uf = new UF(G.V());

        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            Edge e = pq.delMin();                           // Get min weight edge on pq
            int v = e.either();                             // and its vertices.
            int w = e.other(v);
            if (uf.connected(v, w)) {
                continue;                                   // Ignore ineligible edges.
            }
            uf.union(v, w);                                 // Merge components.
            mst.enqueue(e);                                 // Add edge to mst.
        }
    }

    public Iterable<Edge> edges() {
        return mst;
    }

    public double weight() {
        return weight;
    }
}