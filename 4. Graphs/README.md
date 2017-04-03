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

underlying<br>data structure|space          |add edge `v`-`w`|check whether `w` is<br>adjacent to `v`|iterate through vertices<br>adjacent to `v`
:--------------------------:|:-------------:|:--------------:|:-------------------------------------:|:-----------------------------------------:
*list of edges*             |*E*            |1               |*E*                                    |*E* 
*adjacency matrix*          |*V*<sup>2</sup>|1               |1                                      |*V*
*adjacency lists*           |*E* + *v*      |1               |*degree(v)*                            |*degree(v)*
*adjacency sets*            |*E* + *v*      |log *V*         |log *V*                                |*degree(v)*

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