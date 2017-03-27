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