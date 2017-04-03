import edu.princeton.cs.algorithms.Queue;

public class BreadthFirstPaths {

    private boolean[] marked;   // Is a shortest path to this vertex known?
    private int[] edgesTo;      // last vertex on known path to this vertex
    private final int s;        // source

    public BreadthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        edgesTo = new int[G.V()];
        this.s = s;
        bfs(G, s);
    }

    private void bfs(Graph G, int s) {
        Queue<Integer> queue = new Queue<Integer>();
        marked[s] = true;       // Mark the source
        queue.enqueue(s);       // and put it onto the queue.
        while (!queue.isEmpty()) {
            int v = queue.dequeue();    // Remove next vertex from the queue.
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgesTo[w] = v;     // save last edge on a shortest path,
                    marked[w] = true;   // mark it because path is known,
                    queue.enqueue(w);   // and add it to the queue.
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

//    public Iterable<Integer> pathTo(int v)
    // Same as for DFS
}