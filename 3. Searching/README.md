### 3. Searching

#### 3.1 Symbol Tables

##### API

A *symbol table* is a data structure for key-value pairs that supports two operations: *insert* (put) a new pair into
the table and *search* for (get) the value associated with a given key.

`public class ST<Key, Value>` <br>
<br>
`ST()` *create a symbol table* <br>
`void put(Key, key, Value val)` *put key-value pair into the table (remove `key` from table if value is `null`)* <br>
`Value get(Key key)` *value paired with `key` (`null` if `key` is absent)* <br>
`void delete(Key key)` *remove `key` (and its value) from table)* <br>
`boolean contains(Key key)` *is there a value paired with the key?* <br>
`boolean isEmpty()` *is the table empty?* <br>
`int size()` *number of key-value pairs in the table* <br>
`Iterable<Key> keys()` *all the keys in the table* <br>
API for a generic basic symbol table

##### Ordered symbol table

`public class ST<Key extends Comparable<Key>, Value>` <br>
<br>
`ST()` *create an ordered symbol table* <br>
`put(Key, key, Value val)` *put key-value pair into the table (remove `key` from table if value is `null`)* <br>
`Value get(Key key)` *value paired with `key` (`null` if `key` is absent)* <br>
`void delete(Key key)` *remove `key` (and its value) from table)* <br>
`boolean contains(Key key)` *is there a value paired with the key?* <br>
`boolean isEmpty()` *is the table empty?* <br>
`int size()` *number of key-value pairs in the table* <br>
`Key min()` *smallest key* <br>
`Key max()` *largest key* <br>
`Key floor(Key key)` *largest key less than or equal to `key`* <br>
`Key ceiling(Key key)` *smallest key greater than or equal to `key`* <br>
`int rank(Key key)` *number of keys less than `key`* <br>
`Key select(int k)` *key of rank `k`* <br>
`void deleteMin()` *delete smallest key* <br>
`void deleteMax()` *delete largest key* <br>
`int size(Key lo, Key hi)` *number of keys in `[lo..hi]`* <br>
`Iterable<Key> keys(Key lo, Key hi)` *keys in `[lo..hi]`, in sorted order* <br>
`Iterable<key> keys()` *all keys in the table, in sorted order* <br>
API for a generic ordered symbol table

When studying symbol-table implementations, we count *compares* (equality tests or key comparisons). In (rare) cases
where compares are not in the inner loop, we count *array accesses*.

##### Sequential search (in an unordered linked list)

```java
public class SequentialSearchST<Key, Value> {

    private int n;
    private Node first;

    private class Node {
        Key key;
        Value val;
        Node next;

        public Node(Key key, Value val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    public Value get(Key key) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                return x.val;
            }
        }
        return null;
    }

    public void put(Key key, Value val) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        first = new Node(key, val, first);
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
```
This `ST` implementation uses a private `Node` inner class to keep the keys and values in an ordered linked list. The
`get()` implementation searches the list sequentially to find whether the key is in the table (and returns the
associated value if so). The `put()` implementation also searches the list sequentially to check whether the key is in
the table. If so, it updates the associated value; if not, it creates a new node with the given key and value and
inserts it at the beginning of the list.

Search misses and insertions in and (unordered) linked-list symbol table having *n* key-value pairs both require *n* 
compares, and search hits *n* compares in the worst case. When searching for a key that is not in the list, we test
every key in the table against the search key. Because of our policy of disallowing duplicate keys, we need to do such
a search before the insertion. Insertion *n* distinct keys into an initially empty linked-list symbol table uses
~ *n*<sup>2</sup> / 2 compares.

##### Binary search (in an ordered array)

```java
public class BinarySearchST<Key extends Comparable<Key>, Value> {

    private Key[] keys;
    private Value[] vals;
    private int n;

    public BinarySearchST(int capacity) {
        keys = (Key[]) new Comparable[capacity];
        vals = (Value[]) new Object[capacity];
    }

    public int size() {
        return n;
    }

    public Value get(Key key) {
        if (isEmpty()) {
            return null;
        }
        int i = rank(key);
        if (i < n && keys[i].compareTo(key) == 0) {
            return vals[i];
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int rank(Key key) {
        int lo = 0;
        int hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp < 0) {
                hi = mid - 1;
            } else if (cmp > 0) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return lo;
    }

    public void put(Key key, Value val) {
        int i = rank(key);
        if (i < n && keys[i].compareTo(key) == 0) {
            vals[i] = val;
            return;
        }
        for (int j = n; j > i; j--) {
            keys[j] = keys[j - 1];
            vals[j] = vals[j - 1];
        }
        keys[i] = key;
        vals[i] = val;
        n++;
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (isEmpty()) return;

        int i = rank(key);

        if (i == n || keys[i].compareTo(key) != 0) {
            return;
        }

        for (int j = i; j < n - 1; j++) {
            keys[j] = keys[j + 1];
            vals[j] = vals[j + 1];
        }

        n--;
        keys[n] = null;  // to avoid loitering
        vals[n] = null;
    }
}
```
This `ST` implementation keeps the keys and values in parallel arrays. The `put()` implementation moves larger keys one
position to the right before growing the table as in the array-based stack implementation. The `rank(Key key)` method
computes the number of keys in the table that are smaller than `key`. Compare `key` with the key in the middle: if it
is equal, return its index; if it is less, look in the left half; if it is greater, look in the right half.

##### Ordered symbol-table operations for binary search

```java
 public Key min() {
        return keys[0];
    }

    public Key max() {
        return keys[n - 1];
    }

    public Key select(int k) {
        return keys[k];
    }

    public Key ceiling(Key key) {
        int i = rank(key);
        return keys[i];
    }

    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        int i = rank(key);
        if (i < n && key.compareTo(keys[i]) == 0) {
            return keys[i];
        }
        if (i == 0) {
            return null;
        } else {
            return keys[i - 1];
        }
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<Key>();
        for (int i = rank(lo); i < rank(hi); i++) {
            queue.enqueu(keys[i]);
        }
        if (contains(hi)) {
            queue.enqueue(keys[rank(hi)]);
        }
        return queue;
    }
```
These methods complete the implementation of our (ordered) symbol-table API using binary search in an ordered array.
The `min()`, `max()`, and `select()` methods are trivial, just amounting to returning the appropriate key from its
known position in the array. The `rank()` and `delete()` implementations are more complicated, but still straight
forward.

##### Analysis of binary search

Binary search in an ordered array with *n* keys uses no more than lg *n* + 1 compares for a search (successful or 
unsuccessful). With binary search, we achieve a logarithmic-time search guarantee. Inserting a new key into an ordered 
array of size *n* uses ~ 2*n* array access in the worst case, so inserting *n* keys into an initially empty table uses
~ *n*<sup>2</sup> array accesses in the worst case.

underlying data structure           |implementation                                   |pros                                            |cons                  
:----------------------------------:|:-----------------------------------------------:|:-----------------------------------------------|:---
*linked list<br>(sequential search)*|`SequentialSearchST`                             |best for tiny STs                               |slow for large STs
*ordered array<br>(binary search)*  |`BinarySearchST`                                 |optimal search<br>and space,<br>order-based ops |slow insert
*binary<br>search tree*             |`BST`                                            |easy to<br>implement,<br>order-based ops        |no guarantees<br>space for links
*balanced<br>BST*                   |`RedBlackBST`                                    |optimal search<br>and insert,<br>order-based ops|space for links
*hash table*                        |`SeparateChainingHashST`<br>`LinearProbingHashST`|fast search/insert<br>for common types of data  |need for each type<br>no order-based ops<br> space for links/empty

Pros and cons of symbol-table implementations

#### 3.2 Binary Search Trees

A *binary search tree* (BST) is a binary tree where each node has a `Comparable` key (and associated value) and
satisfies the restriction that the key in any node is larger than the keys in all nodes in that node's left subtree and
smaller than the keys in all nodes in that node's right subtree.
```java
public class BST<Key extends Comparable<Key>, Value> {

    private Node root;

    private class Node {
        private Key key;
        private Value val;
        private Node left;
        private Node right;
        private int n;

        public Node(Key key, Value val, int n) {
            this.key = key;
            this.val = val = val;
            this.n = n;
        }
    }

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        } else {
            return x.n;
        }
    }
}
```
This implementation of the ordered symbol-table API uses a binary search tree built from `Node` objects that each
contain a key, associated value, two links, and a node count `n`. Each `Node` is the root of a subtree containing `n`
nodes, with its left link pointing to a `Node` that is the root of a subtree with smaller keys and its right link
pointing to a `Node` at the root of the BST (which has all the keys and associated values in the symbol table). 

```java
    public Value get(Key key) {
        return get(root, key);
    }

    public Value get(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.val;
        }
    }

    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) {
            return new Node(key, val, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, val);
        } else if (cmp > 0) {
            x.right = put(x.right, key, val);
        } else {
            x.val = val;
        }
        x.n = size(x.left) + size(x.right) + 1;
        return x;
    }
```
These implementations of `get()` and `put()` for the symbol-table API are characteristic recursive BST methods that
also serve as models for several other implementations that we consider later.

Search hits in a BST built from *n* random keys require ~ 2 ln *n* compares, on the average. The number of compares
used for a search hit ending at a given node is 1 plus the depth. Adding the depths of all nodes, we get a quantity
know as the *internal path length* of the tree. Insertions and search misses in a BST built from *n* random keys require
~ 2 ln *n* compares, on the average. Insertion and search misses take one more compare, on the average, than search
hits.

```java
    public Key min() {
        if (isEmpty()) {
            throw new NoSuchElementException("called min() with empty symbol table");
        }
        Node x = min(root);
        return x.key;
    }

    private Node min(Node x) {
        if (x.left == null) {
            return x;
        }
        return min(x.left);
    }

    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            return x;
        }
        if (cmp < 0) {
            return floor(x.left, key);
        }
        Node t = floor(x.right, key);
        if (t != null) {
            return t;
        } else {
            return x;
        }
    }
```
Each client method calls a corresponding private method that takes an additional link (to a `Node`) as argument and 
returns `null` or a `Node` containing the desired `Key` via the recursive procedure. The `max()` and `ceiling()`
methods are the same as `min()` and `floor()` (respectively) with right and left (and < and >) interchanged.

```java
    public Key select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException();
        }
        Node x = select(root, k);
        return x.key;
    }

    private Node select(Node x, int k) {
        if (x == null) {
            return null;
        }
        int t = size(x.left);
        if (t > k) {
            return select(x.left, k);
        } else if (t < k) {
            return select(x.right, k - t - 1);
        } else {
            return x;
        }
    }

    public int rank(Key key) {
        return rank(key, root);
    }

    private int rank(Key key, Node x) {
        if (x == null) {
            return 0;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return rank(key, x.left);
        } else if (cmp > 0) {
            return 1 + size(x.left) + rank(key, x.right);
        } else {
            return size(x.left);
        }
    }
```
This code uses the same recursive scheme that has been used previously inorder to implement `select()` and `rank()`
methods. It depends on using the private `size()` method given further up that returns the number of nodes in the
subtree rooted at a node.

```java
    public void deleteMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        root = deleteMin(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) {
            return x.right;
        }
        x.left = deleteMin(x.left);
        x.n = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = delete(x.left, key);
        } else if (cmp > 0) {
            x.right = delete(x.right, key);
        } else {
            if (x.right == null) {
                return x.left;
            }
            if (x.left == null) {
                return x.right;
            }
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.n = size(x.left) + size(x.right) + 1;
        return x;
    }
```
These methods implement eager Hibbard deletion in BSTs. The `delete()` code is compact, but tricky. This method is 
typically effective, but performance in large-scale applications can become a bit problematic. The `deleteMax()` method
is the same as `deleteMin()` with right and left interchanged.

In a BST, all operations take time proportional to the height of the tree, in the worst case. All of these methods go
down one or two paths in the tree. The length of any path is no more than the height, by definition.

```java
    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) {
            return;
        }
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) {
            keys(x.left, queue, lo, hi);
        }
        if (cmplo <= 0 && cmphi >= 0) {
            queue.enqueue(x.key);
        }
        if (cmphi > 0) {
            keys(x.right, queue, lo, hi);
        }
    }
```
To enqueue all the keys from the tree rooted at a given node that fall in a given range onto a queue, we (recursively)
enqueue all the keys from the left subtree (if any of them could fall in the range), then enqueue the node at the root
(if it falls in the range), then (recursively) enqueue all the keys from the right subtree (if any of them could fall
in the range).

algorithm<br>(data structure)|

 
algorithm<br>(data structure)                 |search<sup>1</sup>|insert<sup>1</sup>|search hit<sup>2</sup>|insert<sup>2</sup>|efficiently<br>support ordered<br>operations?
:--------------------------------------------:|:----------------:|:----------------:|:---------------------:|:-----------------:|:--------------------------------------------:
*sequential search<br>(unordered linked list)*|*n*               |*n*               |*n*/2                  |*n*                |no
*binary search<br>(ordered array)*            |lg *n*            |*n*               |lg *n*                 |*n*/2              |yes
*binary tree search<br>(BST)*                 |*n*               |*n*               |1.39 lg *n*            |1.39 lg *n*        |yes


<sup>1</sup>worst-case cost (after n inserts)<br><sup>2</sup>average-case cost (after n random inserts)

Cost summary for basic symbol-table implementations (updated) 