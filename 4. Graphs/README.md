### 4. Graphs

#### 4.1 Undirected graphs

A *graph* is a set of *vertices* and a collection of *edges* that each connect to a pair of vertices.

##### Glossary

A *path* in a graph is a sequence of vertices connected by edges, with no repeated edges. A *simple path* is a path with
no repeated vertices. A *cycle* is a path with at least one edge whose first and last vertices are the same. A *simple
cycle* is a cycle with no repeated vertices (except the requisite repetition of the first and last vertices). The
*length* of a path or a cycle is its number of edges.

A graph is *connected* if there is a path from every vertex to every other vertex in the graph. A graph that is *not 
connected* consists of a set of *connected components*, which are maximal connected subgraphs.

A *tree* is an acyclic connected graph. A disjoint set of trees is called a *forest*. A *spanning tree* of a connected
graph is a subgraph that contains all of that graph's vertices and is a single tree. A *spanning forest* of a graph is
the union of spanning trees of its connected components.

##### Undirected graph data type

`public class Graph`<br>
<br>
`Graph(int V)` *create a V-vertex graph with no edges*<br>
`Graph(In in)` *create a graph from input stream `in`*<br>
`int V()` *number of vertices*<br>
`int E()` *number of edges*<br>
`void addEdge(int v, int w)` *add edge `v-w` to this graph*<br>
`Iterable<Integer> adj(int v)` *vertices adjacent to vertex `v`*<br>
`String toString()` *string representation*<br>

API for an undirected graph

```java
public class GraphHelper {

    public static int degree(Graph G, int v) {
        int degree = 0;
        for (int w : G.adj(v)) {
            degree++;
        }
        return degree;
    }

    public static int maxDegree(Graph G) {
        int max = 0;
        for (int v = 0; v < G.V(); v++) {
            if (degree(G, v) > max) {
                max = degree(G, v);
            }
        }
        return max;
    }

    public static double averageDegree(Graph G) {
        return 2.0 * G.E() / G.V();
    }

    public static int numberOfSelfLoops(Graph G) {
        int count = 0;
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                if (v == w) {
                    count++;
                }
            }
        }
        return count / 2;       // each edge counted twice
    }
}
```
Typical graph-processing code

```java
import edu.princeton.cs.algorithms.Bag;
import edu.princeton.cs.introcs.In;

public class Graph {

    private final int V;        // number of vertices
    private int E = 0;          // number of edges
    private Bag<Integer>[] adj; // adjacency lists

    public Graph(int V) {
        this.V = V;
        adj = (Bag<Integer>[]) new Bag[V];  // Create array of lists
        for (int v = 0; v < V; v++) {       // Initialize all lists
            adj[v] = new Bag<Integer>();    // to empty.
        }
    }

    public Graph(In in) {
        this(in.readInt());
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            // add and edge
            int v = in.readInt();       // Read a vertex
            int w = in.readInt();       // read another vertex
            addEdge(v, w);              // and add edge connecting them.
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }
    
    public String toString() {
        String s = V + " vertices, " + E + " edges\n";
        for (int v = 0; v < V; v++) {
            s += v + ": ";
            for (int w : this.adj(v)) {
                s += w + " ";
            }
            s += "\n";
        }
        return s;
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }
}
```
this `Graph` implementation maintains a vertex-indexed array of lists of integers. Every edge appears twice: if an edge
connects `v` to `w`, then `w` appears in `v`'s list and `v` appears in `w`'s list. The second constructor reads a graph
from an input stream, in the format *V* followed by *E* followed by a list of pairs of `int` values between 0 and *V*-1.

underlying<br>data structure|space          |add edge `v-w`|check whether `w` is<br>adjacent to `v`|iterate through vertices<br>adjacent to `v`
:--------------------------:|:-------------:|:------------:|:-------------------------------------:|:-----------------------------------------:
*list of edges*             |*E*            |1             |*E*                                    |*E* 
*adjacency matrix*          |*V*<sup>2</sup>|1             |1                                      |*V*
*adjacency lists*           |*E* + *v*      |1             |*degree(v)*                            |*degree(v)*
*adjacency sets*            |*E* + *v*      |log *V*       |log *V*                                |*degree(v)*

Order-of-growth performance for typical `Graph` implementations.

##### Depth-first search

```java
public class DepthFirstSearch {

    private boolean[] marked;
    private int count;

    public DepthFirstSearch(Graph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        count++;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    public boolean marked(int w) {
        return marked[w];
    }

    public int count() {
        return count;
    }
}
```
Depth-first search

DFS marks all the vertices connected to a given source in time proportional to the sum of their degrees. First, we
prove that the algorithm marks all the vertices connected to the source `s` (and no others). Every marked vertex is
connected to `s`, since the algorithm finds vertices only by following edges. Now, suppose that some unmarked vertex
`w` is connected to `s`. since `s` itself is marked, any depth from `s` to `w` must have at least one edge from the set
of marked vertices to the set of unmarked vertices, say `v-x`. But the algorithm would have discovered `x` after marking
`v`, so no such edge can exist, a contradiction. The time bound follows because marking ensures that each vertex is
visited once (taking time proportional to its degree to check marks).

##### Finding paths

```java
import edu.princeton.cs.algorithms.Stack;

public class DepthFirstPaths {

    private boolean[] marked;   // Has dfs() been called fot this vertex?
    private int[] edgeTo;       // last vertex on known path to this vertex
    private final int s;        // source

    public DepthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }
}
```
This `Graph` client uses depth-first search to find paths to all the vertices in a graph that are connected to a given
start vertex `s`. To save known paths to each vertex, this code maintains a vertex-indexed array `edgeTo[]` such that
`edgeTo[w] = v` means that `v-w` was the edge used to access `w` for the first time. The `edgeTo[]` array is a
parent-link representation of a tree at `s` that contains all the vertices connected to `s`.

DFS allows us to provide clients with a path from a given source to any marked vertex in time proportional to its 
length. By induction on the number of vertices visited, it follows that the `edgeTo[]` array in `DepthFirstPaths` 
represents a tree rooted at the source. The `pathTo()` method builds the path in time proportional to its length.

##### Breadth-first search

```java
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
```
This `Graph` client uses breadth-first search to find path in a graph with the fewest number of edges from the source
`s` given the constructor. The `bfs()` method marks all vertices connected to `s`, so clients can use `hasPathTo()` to
determine whether a given vertex `v` is connected to `s` and `pathTo()` to get a path from `s` to `v` with the property
that no other such path from `s` to `v` has fewer edges.

For any vertex `v` reachable from `s`, BFS computes the shortest path from `s` to `v` (no path from `s` to `v` has fewer
edges). It is easy to prove by induction that the queue always consists of zer or more vertices of distance *k* from 
the source, followed by zero or more vertices of distance *k*+1 from the source, for some integer *k*, starting with *k*
equal to 0. This property implies, in particular that vertices enter and leave the queue in order of their distance
from `s`. When a vertex `v` enters the queue, no shorter path to `v` will be found before it comes off the queue, and 
no path to `v` that is discovered after it comes off the queue can be shorter than `v`'s tree path length.

BFS takes time proportional to *E* + *V* in the worst case. BFS marks all the vertices connected to `s` in time
proportional to the sum of their degrees. If the graph is connected, the sum equals the sum of the degrees of all the 
vertices, or 2*E*. Initializing the `marked[]` and `edgeTo[]` arrays takes time proportional to *V*.

##### Connected components

```java
public class CC {

    private boolean[] marked;
    private int[] id;
    private int count;

    public CC(Graph G) {
        marked = new boolean[G.V()];
        for (int s = 0; s < G.V(); s++) {
            if (!marked[s]) {
                dfs(G, s);
                count++;
            }
        }
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    public boolean connected(int v, int w) {
        return id[v] == id[w];
    }

    public int id(int v) {
        return id[v];
    }

    public int count() {
        return count;
    }
}
```
This `Graph` client provides its client with the ability to independently process a graph's connected components. The
computations is based on a vertex-indexed array `id[]` such that `id[v]` is set to `i` if `v` is in the `i`th connected
component processed. The constructor finds an unmarked vertex and calls the recursive `dfs()` to mark and identify all
the vertices connected to it, continuing until all vertices have been marked and identified. Implementations of the 
instances methods `connected[]`, `id()`, and `count()` are immediate.

DFS uses preprocessing time and space proportional to *E* + *V* to support constant-time connectivity queries in a
graph. Each adjacency-list entry is examined exactly once, and there are 2*E* such entries (two for each edge);
initializing the `marked[]` and `id[]` arrays takes time proportional to *V*. Instance methods examine or return one
two instance methods.

```java
public class Cycle {

    private boolean[] marked;
    private boolean hasCycle;

    public Cycle(Graph G) {
        marked = new boolean[G.V()];
        for (int s = 0; s < G.V(); s++) {
            if (!marked[s]) {
                dfs(G, -1, s);
            }
        }
    }

    private void dfs(Graph G, int u, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, v, w);
            } else if (w != u) {
                hasCycle = true;
            }
        }
    }

    public boolean hasCycle() {
        return hasCycle;
    }
}

public class Bipartite {

    private boolean[] marked;
    private boolean[] colour;
    private boolean isBipartite = true;

    public Bipartite(Graph G) {
        marked = new boolean[G.V()];
        colour = new boolean[G.V()];
        for (int s = 0; s < G.V(); s++) {
            if (!marked[s]) {
                dfs(G, s);
            }
        }
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                colour[w] = !colour[v];
                dfs(G, w);
            } else if (colour[w] == colour[v]) {
                isBipartite = false;
            }
        }
    }

    public boolean isBipartite() {
        return isBipartite;
    }
}
```
More examples of graph processing with DFS

##### Symbol graphs

```java
import edu.princeton.cs.algorithms.ST;
import edu.princeton.cs.introcs.In;

public class SymbolGraph {

    private ST<String, Integer> st;
    private String[] keys;
    private Graph G;

    public SymbolGraph(String stream, String sp) {
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

        G = new Graph(st.size());
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

    public Graph G() {
        return G;
    }
}
```
This `Graph` client allows clients to define the graphs with `String`vertex names instead of integer indices. It
maintains instance variables `st` (a symbol table that maps names to indices), `keys` (an array that maps indices to
names), and `G` (a graph, with integer vertex names). To build these data structures, it makes two passes through the
graph definition (each line has a string and a list of adjacent strings, separated by the delimiter `sp`).

##### Summary

problem                           |solution
:--------------------------------:|:------------------:
*single-source connectivity*      |`DepthFirstSearch`
*single-source paths*             |`DepthFirstSearch`
*single-source shortest paths*    |`BreadthFirstSearch`
*connectivity*                    |`CC`
*cycle detection*                 |`Cycle`
*two-colorability (bipartiteness)*|`TwoColor`

(Undirected) graph-processing problems addressed in this section

#### 4.2 Directed graphs

##### Glossary

A *directed graph* (or *digraph*) is a set of *vertices* and a collection of *directed edges*. Each directed edge
connects an ordered pair of vertices. 

A *directed path* in a digraph is a sequence of vertices in which there is a (directed) edge point from each vertex in 
the sequence to its successor in the sequence, and with no repeated edges. A *simple directed path* is a directed path
with no repeated vertices. A *directed cycle* is a directed path with at least one edge whose first and last vertices
are the same. A *simple directed cycle* is a directed cycle with no repeated vertices (except the requisite repetition
of the first and last vertices). The *length* of a directed path or cycle is its number of edges.

##### Digraph data type

```java
import edu.princeton.cs.algorithms.Bag;

public class Digraph {

    private final int V;
    private int E;
    private Bag<Integer>[] adj;

    public Digraph(int V) {
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        E++;
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    public Digraph reverse() {
        Digraph R = new Digraph(V);
        for (int v = 0; v < V; v++) {
            for (int w : adj(v)) {
                R.addEdge(w, v);
            }
        }
        return R;
    }
}
```
This `Digraph` data type is identical to `Graph` except that `addEdge` only calls `add()` once, and it has an instance
method `reverese()` that returns a copy with all its edges reversed.

##### Reachability in digraphs

DFS marks all the vertices in a digraph reachable from a given set of sources in time proportional to the sum of the
outdegrees of the vertices marked.

```java
import edu.princeton.cs.algorithms.Bag;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.StdOut;

public class DirectedDFS {

    private boolean[] marked;

    public DirectedDFS(Digraph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    public DirectedDFS(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        for (int s : sources) {
            if (!marked[s]) {
                dfs(G, s);
            }
        }
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    public boolean marked(int v) {
        return marked[v];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        Bag<Integer> sources = new Bag<Integer>();
        for (int i = 1; i < args.length; i++) {
            sources.add(Integer.parseInt(args[1]));
        }

        DirectedDFS reachable = new DirectedDFS(G, sources);

        for (int v = 0; v < G.V(); v++) {
            if (reachable.marked(v)) {
                StdOut.print(v + " ");
            }
        }
        StdOut.println();
    }
}
```
This implementation of depth-first search provides clients the ability to test which vertices are reachable from a
given vertex or a given set of vertices.

##### Cycles and DAGs

A *directed acyclic graph* (DAG) is a digraph with no directed cycles.

```java
import edu.princeton.cs.algorithms.Stack;

public class DirectedCycle {

    private boolean[] marked;
    private int[] edgeTo;
    private Stack<Integer> cycle;   // vertices on a cycle (if one exists)
    private boolean[] onStack;

    public DirectedCycle(Digraph G) {
        onStack = new boolean[G.V()];
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
    }

    private void dfs(Digraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (this.hasCycle()) {
                return;
            } else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            } else if (onStack[w]) {
                cycle = new Stack<Integer>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }
}
```
This class adds to our standard recursive `dfs()` a boolean array `onStack[]` to keep track of the vertices for which
the recursive call has not completed. when it finds an edge `v->w` to a vertex `w` that is on the stack, it has
discovered a directed cycle, which it can recover by following `edgeTo[]` links.

A digraph has a topological order if and only if it is a DAG. If the the digraph has a directed cycle, it has no
topological order. Conversely, the algorithm that we are about to examine computes a topological order for any given
DAG.

```java
import edu.princeton.cs.algorithms.Queue;
import edu.princeton.cs.algorithms.Stack;

public class DepthFirstOrder {

    private boolean[] marked;

    private Queue<Integer> pre;
    private Queue<Integer> post;
    private Stack<Integer> reversePost;

    public DepthFirstOrder(Digraph G) {
        pre = new Queue<Integer>();
        post = new Queue<Integer>();
        reversePost = new Stack<Integer>();
        marked = new boolean[G.V()];

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
    }

    private void dfs(Digraph G, int v) {
        pre.enqueue(v);

        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        post.enqueue(v);
        reversePost.push(v);
    }

    public Iterable<Integer> preorder() {
        return pre;
    }

    public Iterable<Integer> postorder() {
        return post;
    }

    public Iterable<Integer> reversePostorder() {
        return reversePost;
    }
}
```
this class enables clients to iterate through the vertices in various orders defined by depth-first search. This
ability is very useful in the development of advanced digraph-processing algorithms, because the recursive nature of the
search enables us to prove properties of the computation.

```java
import edu.princeton.cs.introcs.StdOut;

public class Topological {

    private Iterable<Integer> order;    // topological order

    public Topological(Digraph G) {

        DirectedCycle cyclefinder = new DirectedCycle(G);
        if (!cyclefinder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePostorder();
        }
    }

    public Iterable<Integer> order() {
        return order;
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
}
```
This `DepthFirstOrder` and `DirectedCycle` client return a topological order for a DAG. The test client solves the
precedence-constrained scheduling problem for a `SymbolDigraph`. The instance method `order()` returns `null` if the
given digraph is not a DAG and an iterator giving the vertices in topological order otherwise. 

Reverse postorder in a DAG is a topological sort. Consider any edge `v->w`. One of the following three cases must hold
when `dfs(v)` is called:
* `dfs(w)` has already been called and has returned (`w` is marked).
* `difs(w)` has not yet been called (`w` is unmarked), so `v->w` will cause `dfs(w) to be called (and return), either
directly or indirectly, before `dfs(v)` returns.
* `dfs(w)` has been called and has not yet returned when `dfs(v)` is called. The keys to the proof is that this case is
impossible in a DAG, because the recursive call chain implies a path from `w` to `v` and `v->w` would complete a
directed cycle.

In the two possible cases, `dfs(w)` is done before `dfs(v)`, so `w` appears *before* `v` in postorder and *after* `v`
in reverse postoreder. Thus, each edge `v->w` points from a vertex earlier in the order to a vertex later in the order,
as desired.

With DFS, we can topologically sort a DAG in time proportional to *E* + *V*. The code uses depth-first search to ensure
that the graph has no directed cycles, and another to do the reverse postorder ordering. Both involve examining all the
edges and all the vertices, and thus take time proportional to *E* + *V*.

##### Strong connectivity in digraphs

Two vertices `v` and `w` are *strongly connected* if they are mutually reachable: that is, if there exists a direct path
from `v` to `w` and a directed path from `w` to `v`. A digraph is *strongly connected* if all its vertices are strongly
connected to one another.

```java
public class KosarajuSharirSCC {

    private boolean[] marked;       // reachable vertices
    private int[] id;               // component identifiers
    private int count;              // number of strong components

    public KosarajuSharirSCC(Digraph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        DepthFirstOrder order = new DepthFirstOrder(G.reverse());
        for (int s : order.reversePostorder()) {
            if (!marked[s]) {
                dfs(G, s);
                count++;
            }
        }
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    public boolean stronglyConnected(int v, int w) {
        return id[v] == id[w];
    }

    public int id(int v) {
        return id[v];
    }

    public int count() {
        return count;
    }
}
```
This implementation differs from `CC` only in few parts. To find strong components, it does a depth-first search in the
reverse digraph to produce a vertex order (reverse postorder of that search) for use in a depth-first search of the 
given digraph.

Let *C* be a strong component in a digraph *G* and let `v` be any vertex nto in *C*. If there is an edge *e* in *G*
pointing from any vertex in *C* to `v`, then vertex `v` appears before *every* vertex in *C* in the reverse postorder
of *G* <sup>*R*</sup>.

The Kosaraju-Sharir algorithm identifies the strong components of a digraph *G*.<br>By induction on the number of
strong components identified in the DFS of *G*. After the algorithm has identified the first *i* components, we assume
(by our inductive hypothesis) that the vertices in the first *i* components are marked and the vertices in the
remaining components are unmarked. Let `s` be the unmarked vertex that appears first in the reverse postorder of *G* 
<sup>*R*</sup>. Then, the constructor call `dfs(G, s)` will visit every vertex in the strong component containing `s`
(which we refer to as component *i*+1) and only those vertices because:
* Vertices in the first *i* components will not be visited (because they are allready marked).
* Vertices in component *i*+1 are not yet marked are reachable from `s` using only other vertices in component *i*+1 (so
will be visited and marked).
* Vertices in components after *i*+1 will not be visited (or marked): Consider (for the sake of contradiction) the first
such vertex `v` that is visited. Let *e* be and edge that goes from a vertex in component *i*+1 to `v`. By the postorder
lemma, `v` appears in the reverse postorder before every vertex in component *i*+1 (including `s`). This contradicts
the definition of `s`.

The Kosaraju-Sharir algorithm uses preprocessing time and space proportional to *E* + *V* to support constant-time
strong connectivity queries in a digraph. The algorithm computes the reverse of the digraph and does two depth-first 
searches. Each of these three steps takes time proportional to *E* + *V*. The reverse copy of the digraph uses space
proportional to *E* + *V*.

The  *transitive closure* of a digraph *G* is another digraph with the same set of vertices, but with an edge from `v`
to `w` in the transitive closure if and only if `w` is reachable from *G*.

```java
public class TransitiveClosure {

    private DirectedDFS[] all;

    TransitiveClosure(Digraph G) {
        all = new DirectedDFS[G.V()];
        for (int v = 0; v < G.V(); v++) {
            all[v] = new DirectedDFS(G, v);
        }
    }

    boolean reachable(int v, int w) {
        return all[v].marked(w);
    }
}
```
All-pairs reachability

##### Summary

problem                                   |solution
:----------------------------------------:|:-------------------------:
*single- and multiple-source reachability*|`DirectedDFS`
*single-source directed paths*            |`DepthFirstDirectedPaths`
*single-source shortest directed paths*   |`BreadthFirstDirectedPaths`
*directed cycle detection*                |`DirectedCycle`
*depth-first vertex orders*               |`DepthFirstOrder`
*precedence-constrained scheduling*       |`Topological`
*topological sort*                        |`Topological`
*strong connectivity*                     |`KosarajuSharirSCC`
*all-pairs reachability*                  |`TransitiveClosure`

Digraph-processing problems addressed in this section

#### 4.3 Minimum Spanning Trees

Recall that a *spanning tree* of a graph is a connected subgraph with no cycles that includes all the vertices. A 
*minimum spanning tree* (MST) of an edge weighted graph is a spanning tree whose weight (the sum of the weights of its
edges) is no larger than the weight of any other spanning tree.

application            |vertex     |edge
:---------------------:|:---------:|:---:
*circuit*              |component  |wire
*airline*              |airport    |flight route
*power<br>distribution*|power plant|transmission<br>lines
*image<br>analysis*    |feature    |proximity<br>relationship

Typical MST applications

##### Underlying principles

Recall the two defining properties of a tree:
* Adding an edge that connects two vertices in a tree creates a unique cycle.
* Removing an edge from a tree breaks it into two separate subtrees.

A *cut* of a graph is a partition of its vertices into two nonempty disjoint sets. A *crossing edge* of a cut is an
edge that connects a vertex in one set with a vertex in the other.

Given any cut in an edge-weighted graph, the crossing edge of minimum weight is in the MST of the graph. Let *e* be the
crossing edge of minimum weight and let *T* be the MST. The proof is by contradiction: suppose that *T* does not contain
*e*. Now consider the graph formed by adding *e* to *T*. This graph has a cycle that contains *e*, and that cycle must
contain at least one other crossing edge - say, *f*, which has higher weight than *e* (since *e* is minimal and all edge
weights are different). We can get a spanning tree of strictly lower weight by deleting *f* and adding *e*,
contradicting the assumed minimality of *T*.

(Greedy MST algorithm) The following method colours black edges in the MST of any connected edge-weighted graph with *V*
vertices: starting with all edges colored gray, find a cut with no black crossing edges, colour its minimum-weight
crossing edge black, and continue until *V* - 1 edges have been coloured black. For simplicity, we assume in the
discussion that the edge weights are all different, though the proposition is still true when that is not the case. By
the cut property, any edge that is coloured balck is in the MST. If fewer than *V* - 1 edges are black, a cut with no
black crossing edges exists (recall that we assume the graph to be connected). Once *V* - 1 edges are black, the black
edges form a spanning tree.

##### Edge-weighted graph data type

`public class Edge implements Comparable<Edge>` <br>
<br>
`Edge(int v, int w, double weight)` *initializing constructor* <br>
`double weight()` *weight of this edge* <br>
`int either()` *either of this edge's vertices* <br>
`int other(int v)` *the other vertex*
`int compareTo(Edge that)` *compare `this` edge to `that`* <br>
`String toString()` *string representation* <br>
API for a weighted edge

<br>

`public class EdgeWeightedGraph` <br>
<br>
`EdgeWeightedGraph(int V)` *create a `V`-vertex graph with no edges* <br>
`EdgeWeightedGraph(In in)` *create a graph from input stream `in`* <br>
`int V()` *number of vertices* <br>
`int E()` *number of edges* <br>
`void addEdge(Edge e)` *add edge `e` to this graph* <br>
`Iterable<Edge> adj(int v)` *all edges incident to vertex `v`* <br>
`Iterable<Edge> edges()` *all edges in this graph* <br>
`String toString()` *string representation* <br>
API for an edge-weighted graph

```java
public class Edge implements Comparable<Edge> {

    private final int v;                // one vertex
    private final int w;                // the other vertex
    private final double weight;        // edge weight

    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if (vertex == v) {
            return w;
        } else if (vertex == w) {
            return v;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int compareTo(Edge that) {
        if (this.weight() < that.weight()) {
            return -1;
        } else if (this.weight() > that.weight()) {
            return +1;
        } else return 0;
    }

    public String toString() {
        return String.format("%d-%d %.5f", v, w, weight);
    }
}
```
This data type provides the methods `either()` and `other()` so that a client can use `other(v)` to find the other
vertex when it know `v`. When neither vertex is known, a client can use the idiomatic code `int v = e.either(),
w = e.other(v);` to access and `Edge e`'s two vertices.

```java
import edu.princeton.cs.algorithms.Bag;

public class EdgeWeightedGraph {

    private final int V;            // number of vertices
    private int E;                  // number of edges
    private Bag<Edge>[] adj;        // adjacency list

    public EdgeWeightedGraph(int V) {
        this.V = V;
        this.E = E;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Edge>();
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public Iterable<Edge> adj(int v) {
        return adj[v];
    }

    public Iterable<Edge> edges() {
        Bag<Edge> bag = new Bag<Edge>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adj[v]) {
                if (e.other(v) > v) {
                    bag.add(e);
                }
            }
        }
        return bag;
    }
}
```
This implementation maintains a vertex-indexed array of lists of edges. As with `Graph`, every edge appears twice; if
an edge connects `v` and `w`, it appears in both `v`'s list and in `w`'s list. The `edges()` method puts all the edges
in a `Bag`.

##### Prim's algorithm

Prim's algorithm computes the MST of any connected edge-weighted graph. The growing tree defines a cut with no black
edges; the algorithm takes the crossing edge of minimal weight, so it is successively colouring edges black in
accordance with the greedy algorithm. 

The lazy version of Prim's algorithm uses space proporional to *E* and time proportional to *E* log *E* (in the worst
case) to compute the MST of a connected edge-weighted graph with *E* edges and *V* vertices. The bottleneck in the
algorithm is the number of edge-weight comparisons in the priority-queue methods `insert()` and `delMin()`. The number
of edges on cost of an insertion is ~lg *E* and the cost to delete the minimum is ~2 lg *E*. Since at most *E* edges
are inserted and at most *E* are deleted, the time bound follows.

```java
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
```
This implementation of Prim's algorithm uses a priority queue to hold crossing edges, a vertex-indexed array to mark
tree vertices, and a queue to hold MST edges. This implementation is a lazy approach where we leave ineligible edges in
the priority queue.

##### Eager version of Prim's algorithm

```java
import edu.princeton.cs.algorithms.IndexMinPQ;
import edu.princeton.cs.algorithms.Queue;

public class PrimMST {

    private Edge[] edgeTo;          // shortest edge from tree vertex
    private double[] distTo;        // distTo[w] = edgeTo[w].weight()
    private boolean[] marked;       // true if v on tree
    private IndexMinPQ<Double> pq;  // eligible crossing edges

    public PrimMST(EdgeWeightedGraph G) {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        pq = new IndexMinPQ<Double>(G.V());

        distTo[0] = 0.0;
        pq.insert(0, 0.0);  // Initialize pq with 0, weight 0.
        while (!pq.isEmpty()) {
            visit(G, pq.delMin());  // Add closest vertex to tree
        }
    }

    private void visit(EdgeWeightedGraph G, int v) {
        // Add v to tree; update data structures.
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) {
                continue;           // v-w is ineligible.
            }
            if (e.weight() < distTo[w]) {
                // Edge e is new best connection from tree to w.
                edgeTo[w] = e;
                distTo[w] = e.weight();
                if (pq.contains(w)) {
                    pq.changeKey(w, distTo[w]);
                } else {
                    pq.insert(w, distTo[w]);
                }
            }
        }
    }

    public Iterable<Edge> edges() {
        Queue<Edge> mst = new Queue<Edge>();
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                mst.enqueue(e);
            }
        }
        return mst;
    }

    public double weight() {
        double weight = 0.0;
        for (Edge e : edges())
            weight += e.weight();
        return weight;
    }
}
```
This implementation of Prim's algorithm keeps eligible crossing edges on an index priority queue.

The eager version of Prim's algorithm uses extra space proportional to *V* and time proportional to to *E* log *V* (in
the worst case) to compute the MST of connected edge-weighted graph with *E* edges and *V* vertices. The number of
vertices on the priority queue is at most *V*, and there are three vertex-indexed arrays, which implies the space bound.
The algorithm uses *V insert* operations, *V delete the minimum* operations, and (in the worst case) *E change priority*
operations. These counts, coupled with the fact that our heap-based implementation of the index priority queue
implements all these operations in time proportional to log *V*, imply the time bound.

##### Kruskal's algorithm

Kruskal's algorithm computes the MST of any connected edge-weighted graph. If the next edge to be considered does not
form a cycle with black edges, it crosses a cut defined by the set of vertices connected to one of the edge's vertices
by black edges (and its complement). Since the edge does not create a cycle, it is the only crossing edge seen so far,
and since we consider the edges in sorted order, it is a crossing edge of minimum weight. Thus, the algorithm is
successively taking a minimal-weight crossing edge, in accordance with the greedy algorithm.

Kruskal's algorithm uses space proportional to *E* and time proportional to *E* log *E* (in the worst case) to compute
the MST of an edge-weighted connected graph with *E* edges and *V* vertices. The implementation uses the priority-queue
constructor that initializes the priority queue with all the edges, at a cost of at most *E* compares. After the
priority queue is built, the argument is the same as for Prim's algorithm. The number of edges on the priority queue is
at most *E*, which gives the space bound, and the cost per operation is at most 2 lg *E* compares, which gives the time
bound. Kruskal's algorithm also performs up to *E* `connected()` and *V* `union()` operations, but that cost does not
contribute to the *E* log *E* order of growth of the total running time.

```java
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
```
This implementation of Kruskal's algorithm uses a queue to hold MST edges, a priority queue to hold edges not yet
examined, and a union-find data structure for identifying ineligible edges. The MST edges are returned to the client in
increasing order of their weights. 

##### Perspective 

algorithm       |space<sup>*</sup>|time<sup>*</sup>
:--------------:|:---------------:|:----:
*lazy prim*     |*E*              |*E* log *E*
*eager prim*    |*V*              |*E* log *V*
*Kruskal*       |*E*              |*E* log *E*
*Fredman-Tarjan*|*V*              |*E* + *V* log *V*
*Chazelle*      |*V*              |*very, very nearly,<br>but not quite E*
*impossible?*   |*V*              |*E*?

<sup>*</sup> worst-case order of growth for *V* vertices and *E* edges

Performance characteristics of MST algorithms

#### 4.4 Shortest paths

application|vertex      |edge
:---------:|:----------:|:-------------------:
*map*      |intersection|road
*network*  |router      |connection
*schedule* |job         |precedence constraint
*arbitrage*|currency    |exchange rate

Typical shortest-paths applications

A *shortest path* fro vertex `s` to vertex `t` in an edge-weighted digraph is a directed path from `s` to `t` with the
property that no other such path has a lower weight.

##### Properties of shortest paths

* *Paths are directed*
* *The weights are not necessarily distances*
* *There may be unreachable vertices*
* *Paths can repeat vertices and edges*
* *Shortest paths are (normally) simple*
* *Shortest paths are not necessarily unique*
* *Parallel edges and self-loops may be present*
* *Negative weights introduce complications*

Given an edge-weighted digraph and a designated source vertex `s`, a *shortest-paths tree* for vertex `s` is a subgraph
containing `s` and all the vertices reachable from `s` that forms a directed tree rooted at `s` such that every tree
path is a shortest path in the digraph.

##### Edge-weighted digraph data types

`public class DirectedEdge`<br>
<br>
`DirectedEdge(int v, int w, double weight)`<br>
`double weight()` *weight of this edge*<br>
`int from()` *vertex this edge points from*<br>
`int to()` *vertex this edge points to*<br>
`String toString()` *string representation*<br>

Weighted directed-edge API<br>

`public class EdgeWeightedDigraph`<br>
<br>
`EdgeWeightedDigraph(int V)` *create a `V`-vertex digraph with 0 edges*<br>
`EdgeWeightedDigraph(In in)` *create a digraph from input stream `in`*<br>
`int V()` *number of vertices*<br>
`int E()` *number of edges*<br>
`void addEdge(DirectedEdge e)` *add edge `e` to the digraph*<br>
`Iterable<DirectedEdge> adj(int v)` *all edges leaving vertex `v`*<br>
`Iterable<DirectedEdge> edges()` *all edges in this digraph*<br>
`String toString()` *string representation*<br>

Edge-weighted digraph API

```java
public class DirectedEdge {

    private final int v;                // edge tail
    private final int w;                // edge head
    private final double weight;        // edge weight

    public DirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public String toString() {
        return String.format("%d->%d %.2f", v, w, weight);
    }
}
```
This `DirectedEdge` implementation is simpler than the undirected weighted `Edge` implementation because the two
vertices are distinguished. Our clients use the idiomatic code `int v = e.to(), w = e.from();` to access a
`DirectedEdge e`'s two vertices.

```java
import edu.princeton.cs.algorithms.Bag;
import edu.princeton.cs.introcs.In;

public class EdgeWeightedDigraph {

    private final int V;                // number of vertices
    private int E;                      // number of edges
    private Bag<DirectedEdge>[] adj;     // adjacency list

    public EdgeWeightedDigraph(int V) {
        this.V = V;
        this.E = 0;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<DirectedEdge>();
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(DirectedEdge e) {
        adj[e.from()].add(e);
        E++;
    }

    public Iterable<DirectedEdge> adj(int v) {
        return adj[v];
    }

    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> bag = new Bag<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj[v]) {
                bag.add(e);
            }
        }
        return bag;
    }
}
```
This `EdgeWeightedDigraph` implementation is an amalgam of `EdgeWeightedGraph` and `Digraph` that maintains a
vertex-indexed array of bags of `DirectedEdge` objects. As with `Digraph`, every edge appears just once: if an edge
connects `v` to `w`, it appears in `v`'s adjacency list. Self-loops and parallel edges are allowed.


`public class SP`<br>
<br>
`SP(EdgeWeightedDigraph G, int s)` *constructor*<br>
`double distTo(int v)` *distance from `s` to `v`, ∞ if no path*<br>
`boolean hasPathTo(int v)` *path from `s` to `v`?*<br>
`Iterable<DirectedEdge> pathTo(int v)` *path from `s` to `v`, `null` if none*<br>

API for shortest-paths implementations

##### Theoretical basis for shortest-paths algorithms

Let *G* be an edge-weighted digraph, with `s` a source vertex in *G* and `distTo[]` a vertex-indexed array of path
lengths in *G* such that, for all `v` reachable from `s`, the value of `distTo[v]` is the lenght of *some* path from `s`
to `v` with `distTo[v]` equal to infinity for all `v` not reachable from `s`. These values are the lenghts of *shortest*
paths if and only if they satisfy `distTo[s] = 0` and `distTo[w] <= distTo[v] + e.weight()` for each edge `e` from `v`
to `w` (or, in other words, no edge is eligible).

Generic shortest-paths algorithm: Initialize `distTo[s]` to 0 and all other `distTo[]` values to infinity, and proceed
as follows: *Relax any edge in *G*, continuing until no edge is eligible.* For all vertices `w` reachable from `s`, the
value of `distTo[w]` after this computation is the length of a shrotest path from `s` to `w` (and `edgeTo[w]` is the
last edge on such a path).

Relaxing and edge `v->w` always sets `distTo[w]` to the length of some path from `s` (and `edgeTo[w]` to the last edge
on that path). For any vertex `w` reachable from `s`, some edge on the shortest path to `w` is eligible as long as
`distTo[w]` remains infinite, so the algorithm continues until the `distTo[]` value of each vertex reachable from `s` is
the length of some path to that vertex. For any vertex `v` for which the shortest path is well-defined, throughout the
algorithm `distTo[v]` is the length of some simple path from `s` to `v` and is strictly monotonically decreasing. Thus,
it can decrease at most a finite number of times (once for each simple path from `s` to `v`).

##### Dijkstra's algorithm

Dijkstra's algorithm solves the single-source shortest-paths problem in edge-weighted digraphs with nonnegative weights.
If `v` is reachable from the source, every edge `v->w` is relaxed exactly once, when `v` is relaxed, leaving `distTo[w]
<= distTo[v] +e.weight()`. This inequality holds until the algorithm completes, since `distTo[w]` can only decrease (any
relaxation can only decrease a `distTo[]` value) and `distTo[v]` never changes (because edge weights are nonnegative and
we choose the lowest `distTo[]` value at each step, no subsequent relaxation can set any `distTo[]` entry to a lower
value than `distTo[v]`). Thus, after all vertices reachable from `s` have been added to the tree, the shortest-paths 
optimality conditions hold.

Dijkstra's algorithm uses extra space proportional to *V* and time proportional to *E* log *V* (in the worst case) to
solve the single-source shortest paths problem in an edge-weighted digraph with *E* edges and *V* vertices. The number
of vertices on the priority queue is at most *V*, and there are three vertex-indexed arrays, which implies the space
bound. The algorithm uses *V insert* operations, *V delete the minimum* operations, and (in the worst case) *E change
priority* operations. These counts, coupled with the fact that our heap-based implementation of the index priority 
queue implements all the operations in time proportional to log *V*, imply the time bound.

```java
import edu.princeton.cs.algorithms.IndexMinPQ;
import edu.princeton.cs.algorithms.Stack;

public class DijkstraSP {

    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;

    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        pq = new IndexMinPQ<Double>(G.V());

        for (int v = 0; v < G.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;

        pq.insert(s, 0.0);
        while (!pq.isEmpty()) {
            relax(G, pq.delMin());
        }
    }

    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) {
                    pq.changeKey(w, distTo[w]);
                } else {
                    pq.insert(w, distTo[w]);
                }
            }
        }
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }
}
```
This implementation of Dijkstra's algorithm grows the SPT by adding an edge at a time, always choosing the edge from a
tree vertex to a non-tree vertex whose destination `w` is closest to `s`.

```java
public class DijkstraAllPairsSP {

    private DijkstraSP[] all;

    DijkstraAllPairsSP(EdgeWeightedDigraph G) {
        all = new DijkstraSP[G.V()];
        for (int v = 0; v < G.V(); v++) {
            all[v] = new DijkstraSP(G, v);
        }
    }

    Iterable<DirectedEdge> path(int s, int t) {
        return all[s].pathTo(t);
    }

    double dist(int s, int t) {
        return all[s].distTo(t);
    }
}
```
All-pairs shortest paths

##### Acyclic edge-weighted digraphs

By relaxing vertices in topological order, we can solve the singe source shortest-paths problem for edge-weighted DAGs
in time proportional to *E* + *V*. Every edge `v->w` is realxed exactly once, when `v` is relaxed, leaving `distTo[w] <=
distTo[v] + e.weight()`. This inequality holds until the algorithm completes, since `distTo[v]` never changes (because
of the topological order, no edge entering `v` will be processed after `v` is relaxed) and `distTo[w]` can only decrease
(any relaxation can only decrease a `distTo[]` value). Thus, after all vertices reachable from `s` have been added to
the tree, the shortest-paths optimality conditions hold. The time bound is immediate: the topological sort takes time
proportional to *E* + *V*, and the second relaxation pass completes the job by relaxing each edge once, again in time 
proportional to *E* + *V*.

```java
import edu.princeton.cs.algorithms.Stack;

public class AcyclicSP {

    public DirectedEdge[] edgeTo;
    private double[] distTo;

    private AcyclicSP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];

        for (int v = 0; v < G.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;

        Topological top = new Topological(G);

        for (int v : top.order()) {
            relax(G, v);
        }
    }

    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
            }
        }
    }

    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }
}
```
This shortest-paths implementation for edge-weighted DAGs uses a topological sort (adapted tu use `EdgeWeightedDigraph`
and `DirectedEdge`) to enable it to relax the vertices in topological order which is all that is needed to compute
shortest paths.

We solve the longest-paths problem in edge-weighted DAGs in time proportional to *E* + *V*. Given a longest-paths
problem, create a copy of the given edge-weighted DAG that is identical to the original, except that all edge weights
are negated. Then the *shortest* path in this copy is the *longest* path in the original. To transform the solution of
the shortest-paths problem to a solution of the longest-paths problem, negate the weights in the solution. The running
time is proportional to *E* + *V*.

The *critical path method* for parallel scheduling is to proceed as follows: Create an edge-weighted DAG with a source
`s`, a sink `t`, and two vertices for each job (a *start* vertex and an *end* vertex). For each job, add an edge from
its start vertex to its end vertex with weight equal to its duration. For each precedence constraint `v->w`, add a
zero-weight edge from the end vertex corresponding to `v` to the beginning vertex corresponding to `w`. Also add
zero-weight edges from the source to each job's start vertex and from each job's end vertex to the sink. Now, schedule
each job at the time given by the length of its longest path from the source.