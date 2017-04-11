### 1. Fundamentals

#### 1.3 Bags, queues, and stacks

##### APIs

`public class Bag<Item> implements Iterabel<Item>`<br>
<br>
`Bag()` *create an empty bag* <br>
`void add(Item item)` *add and item* <br>
`boolean isEmpty()` *is the bag empty? <br>
`int size()` *number of items in the bag* <br>
Bag

`public class Queue<Item> implements Iterable<Item>`<br>
<br>
`Queue()` *create an empty queue* <br>
`void enqueue(Item item)` *add an item* <br>
`Item dequeue()` *remove the least recently added item* <br>
`boolean isEmtpy()` *is the queue empty? <br>
`int size()` *number of item in the queue <br>
Queue

`public class Stack<Item> implements Iterable<Item>`<br>
<br>
`Stack()` *create an empty stack* <br>
`void push(Item item)` *add an item* <br>
`Item pop()` *remove the most recently added item* <br>
`boolean isEmpty()` *is the stack empty?* <br>
`int size()` *number of items in the stack* <br>
Pushdown (LIFO) stack

APIs for fundamental generic iterable collections

##### Implementing collections

Pushdown (LIFO) stack (resizing array implementation)
```java
import java.util.Iterator;

public class ResizingArrayStack<Item> implements Iterable<Item> {

    private Item[] a = (Item[]) new Object[1];      // stack of items
    private int n = 0;                              // number of items

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    private void resize(int max) {
        // Move stack to a new array of size max.
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    public void push(Item item) {
        // Add item to top of stack
        if (n == a.length) {
            resize(2 * a.length);
            a[n++] = item;
        }
    }

    public Item pop() {
        // Remove item from top of stack
        Item item = a[--n];
        a[n] = null;                                // avoid loitering
        if (n > 0 && n == a.length / 4) {
            resize(a.length / 2);
        }
        return item;
    }

    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        // Support LIFO iteration.
        private int i = n - 1;

        public boolean hasNext() {
            return i >= 0;
        }

        public Item next() {
            return a[i--];
        }

        public void remove() {

        }
    }
}
```
This generic, iterable implementation of out `Stack` API is a model for collection ADTs that keep items in an array.
It resizes the array to keep the array size within a constant factor of the stack size.

##### Linked lists

A *linked list* is  a recursive data structure that is either empty (*null*) or a reference to a *node* having a generic
item and a reference to a linked list.

Pushdown stack (linked-list implementation)
```java
import java.util.Iterator;

public class Stack<Item> implements Iterable<Item> {

    private Node first;             // top of stack (most recently added node)
    private int n;                  // number of items

    private class Node {
        // nested class to define nodes
        Item item;
        Node next;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void push(Item item) {
        // Add item to the top of the stack.
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        n++;
    }

    public Item pop() {
        // Remove item from top of stack.
        Item item = first.item;
        first = first.next;
        n--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {

        }

        public Item next() {
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
```
This generic `Stack` implementation is based on linked-list data structure. It can be used to create stacks containing 
any type of data.

FIFO queue
```java
import java.util.Iterator;

public class Queue<Item> implements Iterable<Item> {

    private Node first;                 // link to least recently added node
    private Node last;                  // link to most recently added node
    private int n;                      // number of items in queue

    private class Node {
        // nested class to define nodes
        Item item;
        Node next;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void enqueue(Item item) {
        // Add item to the end of the list.
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
            first = last;
        } else {
            oldlast.next = last;
        }
        n++;
    }

    public Item dequeue() {
        // Remove item from the beginning of the list.
        Item item = first.item;
        first = first.next;
        n--;
        if (isEmpty()) {
            last = null;
        }
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {

        }

        public Item next() {
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
```
This generic `Queue` implementation is based on a linked-list data structure. It can be used to create queues containing
any type of data.

Bag
```java
import java.util.Iterator;

public class Bag<Item> implements Iterable<Item> {

    private Node first;             // first node in list

    private class Node {
        Item item;
        Node next;
    }

    public void add(Item item) {
        // same as push() in Stack
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {

        }

        public Item next() {
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
```
This `Bag` implementation maintains a linked list of the item provided in calls to `add()`. The iterator traverses the
list, maintaining the current node in `current`.

##### Overview

data structure |advantage                                        |disadvantage
:-------------:|:-----------------------------------------------:|:-------------------------------------:
*array*        |index provides<br>immediate access<br>to any item|need to know size<br>on initialization
*linked list*  |uses space<br>proportional to size               |need reference to<br>access an item

Fundamental data structures

#### 1.4 Analysis of algorithms

##### Scientific method

The very same approach that scientists use to understand the natural world is effective for studying the running time
of programs:
* *Observe* some feature of the natural world, generally with precise instruments.
* *Hypothesize* a model that is consistent with the observations.
* *Predict* events using the hypothesis.
* *Verify* the predictions by making further observations.
* *Validate* by repeating until the hypothesis and observations agree.

##### Mathematical models

We write ~*f*(*n*) to represent any function that, when divided by *f*(*n*), approaches 1 as *n* grows, and we write
*g*(*n*) ~ *f*(*n*) to indicate that *g*(*n*)/*f*(*n*) approaches 1 as *n* grows.

description |function
:----------:|:---:
constant    |1
logarithmic |log *n*
linear      |*n*
linearithmic|*n* log *n*
quadratic   |*n* <sup>2</sup>
cubic       |*n* <sup>3</sup>
exponential |2 <sup>*n*</sup>

Commonly encountered order-of-growth functions

##### Order-of-growth classification

![Typical_orders_of_growth](https://github.com/GaPhil/algorithms/blob/master/1.%20Fundamentals/Typical_orders_of_growth.jpg)

Typical orders of growth

description   |function        |2x<br>factor    |10x<br> factor   |predicted time for 10*N*|predicted time for 10*n*<br>on a 10x faster computer
:------------:|:--------------:|:--------------:|:---------------:|:----------------------:|:---------------------------------------------------:
*linear*      |*n*             |2               |10               |a day                   |a few hours
*linearithmic*|*n* log *n*     |2               |10               |a day                   |a few hours
*quadratic*   |*n* <sup>2</sup>|4               |100              |a few weeks             |a day
*cubic*       |*n* <sup>3</sup>|8               |1,000            |several months          |a few weeks
*exponential* |2 <sup>*n*</sup>|2 <sup>*n*</sup>|2 <sup>9*n*</sup>|never                   |never

Predictions on the basis of order of-of-growth functions

##### Coping with dependence on inputs

In the linked-list implementation of `Bag`, `Stack`, and `Queue`, all operations take constant time in the worst case.
The number of instructions executed for each operation is bounded by a small constant. *Caveat*: This argument depends
upon the assumption that the Java system creates a new `Node` in constant time.

In the resizing array implementation of `Stack`, the average number of array accesses for any sequence of push and pop
operations starting from an empty data structure is constant in the worst case. For each push operation that causes the
array to grow (say from size *n* to size 2*n*), consider the *n*/2 - 1 push operations that most recently caused the 
stack size to grow to *k*, for *k* from *n*/2 + 2 to *n*. Averaging the 4*n* array accesses to grow the array to size
2*n* (2*n* array accesses to initialize an array of size 2*n* an 2*n* array accesses to copy the *n* items) with 
*n*/2 - 1 array accesses (one for each push), we get and average cost of 9 array accesses for each of these *n*/2 - 1
push operations. Establishing this proposition for any sequence of push and pop operations is more intricate.

##### Memory

type     |bytes
:-------:|:----:
`boolean`|1
`byte`   |1
`char`   |2
`int`    |4
`float`  |4
`long`   |8
`double` |8

Typical memory requirements for primitive types

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

*find examines `id[5]` and `id[9]`*
```
p q   0 1 2 3 4 5 6 7 8 9
-------------------------
5 9   1 1 1 8 8 1 1 1 8 8
```

*union has to change all 1s to 8s*
```
p q   0 1 2 3 4 5 6 7 8 9
-------------------------
5 9   1 1 1 8 8 1 1 1 8 8
      8 8 8 8 8 8 8 8 8 8
```

Quick-find overview

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