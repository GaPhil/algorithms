### 1. Fundamentals

#### 1.5 Union-Find

##### Dynamic connectivity

We start with the following problem specification: The input is a sequence of pairs of integers, where each integer
represents an object of some type and we are to interpret the pair `p` `q` meaning "`p` is connected to `q`". We assume
that "is connected to" is an *equivalence* relation, which means that it is
* *Reflexive*: `p` is connected to `p`.
* *Symmetric*: If `p` is connected to `q`, then `q` is connected to `p`.
* *Transitive*: If `p` is connected to `q` and `q` is connected to `r`, then `p` is connected to `r`.

`public class UF`<br>
<br>
`UF(int n)` *initialize `n` sites with integer names (0 to `n-1`)* <br>
`void union(int p, int q)` *add connection between `p` and `q`* <br>
`int find(int p)` *component identifier for `p`(0 to `n-1`)* <br>
`boolean connected(int p, int q)` *return `true` if `p` and `q` are in the same component* <br>
`int count()` *number of components* <br>

Union-find API

Union-find cost model: When studying algorithms to implement the union-find API, we count *array accesses* (the number 
of times and array entry is accessed, for read or write).

```java
public class UF {

    private int[] id;           // access to component id (site indexed)
    private int count;          // number of components

    public UF(int n) {
        // initialize component id array.
        count = n;
        id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
    }

    public int count() {
        return count;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public int find(int p) {
        return id[p];
    }

    public void union(int p, int q) {
        // put p and q into the same component.
        int pID = find(p);
        int qID = find(q);

        // Nothing to do if p and q are already in the same component.
        if (pID == qID) {
            return;
        }

        // Change values from id[p] to id[q].
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pID) {
                id[i] = qID;
            }
        }
        count--;
    }

    public static void main(String[] args) {
        // Solve dynamic connectivity problem on StdIn.
        int n = StdIn.readInt();        // Read number of sites.
        UF uf = new UF(n);              // Initialize n components
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();    // Read pair to connect
            if (uf.connected(p, q)) {
                continue;               // Ignore if connected
            }
            uf.union(p, q);              // Combine components
            StdOut.println(uf.count() + " components");
        }
    }
}
```
Our `UF` implementations are based on this code, which maintains an array of integers `id[]`such that the `find()`
method returns the same integer for every site in each connected component. The `union()` method must maintain this 
invariant.

##### Implementations

###### Quick-find 

```java
public int find(int p) {
    return id[p];
}

public void union(int p, int q) {
    // Put p and q into the same component.
    int pID = find(p);
    int qID = find(q);
   
    // Nothing to do if p and q are already in the same component.
    if (pID == qID) {
        return;
    }
    
    // Change values from id[p] to id[q].
    for (int i = 0; i < id.length; i++) {
        if (id[i] == pID) {
            id[i] = qID;
        }
    }
    count--;
}
```
Quick-find

The quick-find algorithm uses one array access for each call to `find()`, two array accesses for each call to
`connected()`, and between *n* + 3 and 2*n* + 1 array accesses for each call to `union()` that combines two components.
Each call to `connected()` test two entries in the `id[]` array, one for each of the two calls to `find()`. Each call
to `union()` that combines two components does so by making two calls to `find()`, testing each of the *n* entries in 
the `id[]` array, and changing between 1 and *n* -1 of them.

###### Quick-union

```java
public int find(int p) {
    // Find component name.
    while(p != id[q]) {
        p = id[p];
        return p;
    }
}

public void union(int p, int q) {
    // Give p and q the same root.
    int i = find(p);
    int j = find(q);
    if(i == j) {
        return;
    }
    id[i] = j;
    count--;
}
```
Quick-union

The *size* of a tree is its number of nodes. The *depth* of a node in a tree is the number of links on the path from it
to the root. The *height* of a tree is the maximum depth among its nodes.

The number of array accesses used by `find()` in quick-union is 1 plus twice the depth of the node corresponding to the
give site. The number of array accesses used by `union()` and `connected()` is the cost of the two `find()` operations
(plus 1 for `union()` if the given sites are in different trees).

###### Weighted quick-union

```java
public class WeightedQuickUnionUF {

    private int[] id;               // parent link (site indexed)
    private int[] sz;               // size of component for roots (site indexed)
    private int count;              // number of components

    public WeightedQuickUnionUF(int n) {
        count = n;
        id = new int[n];
        for (int i = 0; i < id.length; i++) {
            id[i] = i;
        }
        sz = new int[n];
        for (int i = 0; i < id.length; i++) {
            sz[i] = 1;
        }
    }

    public int count() {
        return count;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public int find(int p) {
        // Follow links to find a root.
        while (p != id[p]) {
            p = id[p];
        }
        return p;
    }

    public void union(int p, int q) {
        int i = find(p);
        int j = find(q);
        if (i == j) {
            return;
        }

        // Make smaller root point to larger one.
        if (sz[i] < sz[j]) {
            id[i] = j;
            sz[j] += sz[j];
        } else {
            id[j] = i;
            sz[i] += sz[j];
        }
        count--;
    }
}
```
This code is best understood in terms of the forest-of-trees representation. We add a site-indexed array `sz[]` as an
instance variable so that `union()` can link the root of the smaller tree to the root of the larger tree. This addition
makes it feasible to address large problems.

The depth of any node in a forest built by weighted quick-union find for *n* sites is at most lg *n*. We prove a
stronger fact by (strong) induction: The height of every tree of size *s* in the forest is at most lg *s*. The base case
follows from the fact that the tree height is o when *k*  is 1. By the inductive hypothesis, assume that the tree height
of a tree of size *i* is at most lg *i* for all *i* < *s*. When we combine a tree of size *i* with a tree of size *j*
with *i* <= *j* and *i* + *j* = *s*, we increase the depth of each node in the smallest set by 1, but they are now in a
tree of size *i* + *j* = *s*, so the property is preserved because 1 + lg *i* = lg(*i* +* i*) <= lg(*i* + *j*) = lg *s*.

For weighted quick-union with *n* sites, the worst-case order of growth of the cost of `find()`, `connected()` and 
`union()` is log *n*. Each operation does at most a constant number of array accesses for each node on the path from a
node to a root in the forest.

algorithm                                      |constructor<sup>*</sup>|union<sup>*</sup>                         |find<sup>*</sup>  
:---------------------------------------------:|:---------------------:|:----------------------------------------:|:----------------------------------------:
*quick-find*                                   |*n*                    |*n*                                       |1
*quick-union*                                  |*n*                    |*tree height*                             |*tree height*
*weighted quick-union*                         |*n*                    |lg *n*                                    |lg *n*
*weighted quick-union with<br>path compression*|*n*                    |*very nearly, but not quite 1 (amortized)*|*very nearly, but not quite 1 (amortized)*
*impossible*                                   |*n*                    |1                                         |1

<sup>*</sup>*order of growth for N sites (worst case)*

Performance characteristics of union-find algorithms