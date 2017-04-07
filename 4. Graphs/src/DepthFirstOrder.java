import edu.princeton.cs.algorithms.Queue;
import edu.princeton.cs.algorithms.Stack;

public class DepthFirstOrder {

    private boolean[] marked;

    private Queue<Integer> preorder;
    private Queue<Integer> postorder;
    private int[] pre;
    private int[] post;
    private Stack<Integer> reversePost;

    public DepthFirstOrder(Digraph G) {
        preorder = new Queue<Integer>();
        postorder = new Queue<Integer>();
        reversePost = new Stack<Integer>();
        marked = new boolean[G.V()];

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
    }

    public DepthFirstOrder(EdgeWeightedDigraph G) {
        pre = new int[G.V()];
        post = new int[G.V()];
        postorder = new Queue<Integer>();
        preorder = new Queue<Integer>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    private void dfs(Digraph G, int v) {
        preorder.enqueue(v);

        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        postorder.enqueue(v);
        reversePost.push(v);
    }

    private void dfs(EdgeWeightedDigraph G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preorder.enqueue(v);
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        postorder.enqueue(v);
        post[v] = postCounter++;
    }

    public Iterable<Integer> preorder() {
        return preorder;
    }

    public Iterable<Integer> postorder() {
        return postorder;
    }

    public Iterable<Integer> reversePost() {
        return reversePost;
    }
}