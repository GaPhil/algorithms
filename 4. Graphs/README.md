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