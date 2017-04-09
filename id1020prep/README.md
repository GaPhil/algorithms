### ID1020 Exam preparation 

#### 2016-01-18

#### Q 1: Order of growth for functions

* from where to where does the function go?
* how big are the steps? (i++ or i * 2)?
* check for division  / multiplication / addition

#### Q 2: Stacks and Queues order of growth

###### Linked list implementation of stack (according to Jim's answers)

Operation|Beginning |End
:-------:|:--------:|:---:
push     |~1        |~1
pop      |~1        |~N

###### Linked list implementation of queue

Operation|Beginning |End
:-------:|:--------:|:---:
enqueue  |~1        |~1
dequeue  |~N        |~1

###### Array implementation of stack

Operation|Beginning |End
:-------:|:--------:|:---:
push     |~N        |~1
pop      |~N        |~1

###### Array implementation of queue

Operation|Beginning |End
:-------:|:--------:|:---:
enqueue  |~N        |~1
dequeue  |~1        |~N

#### Q 3: Analysis of search algorithms

* **in-place** sorting algorithm: Memory complexity is (at most) logarithmic (~lg N)
* **in-place** algorithm examples: quicksort (logarithmic memory) - all of them apart from mergesort
* algorithm with significantly better memory complexity than in-place: heapsort or shell sort (constant memory)
* algorithm with significantly worse memory complexity than in-place: merge
* can a recursive divide-and-conquer algorithm be in-place? : yes if the problem is (approximately) halved then the
virtual machine stack depth will be logarithmic (as in quicksort)

#### Q 4: Analysis of search algorithms (continued)

* **stable sorting** algorithm: entries with duplicate keys are in the same order after sorting as they were before.
* the following algorithms are stable: insertion sort and mergesort (bubble sort)
* mergesort always divides into equal subsets. Quicksort; the division depends on the partitioning element.
* for quicksort all elements in one subset are greater than all the elements in the other subset
* how are two subsets merged into one: they need to be merged by repeatedly taking the smallest first remaining element
of the two subset until the subsets are exhausted.

#### Q 5: Trees

* binary search tree: smaller elements on the left , larger on the right - everywhere!
* worst case time complexity for search in red-black binary tree: ~2 log N (path that is alternating red and black lines
has this depth)
* binary tree worst case for search: ~N (having to traverse the whole tree if it is not balanced)
* how to represent a matrix with loads of 0s (instead of array of arrays): using a single array (representing rows) with
symbol table entries. Only the non-zero entries are in the symbol table with column number as the key. A search miss
is interpreted to mean that the entry is zero.

#### Q 6: Array resizing and hash tables

* standard arrays in hash tables are kept between 0.5 and 0.125 full 
* hash function hashes to multiples of 32, standard resizing, linear probing, array access for hit and miss: array is
between kept between 4 and 16, average is (4+16)/2 = 10. The array will have clusters of size 10. Linear probing will
on average use 5.5 access for a hit and 11 for a miss.
* same hash function as above but uses separate chaining, array access for hit and miss: the number of array access for
hit and miss will be one. The list will have to be traversed but that does not involve array accesses.
* hash tables vs. red-black BST for symbol table, pros and cons: red-black BSTs are useful if you need to iterate the
elements and they have a better worse case (logarithmic) which may be necessary for mission-critical applications or
open systems where malicious users supply keys that have been designed for maximum collision. Hash tables have linear
worst case. Hash tables are generally better, since time complexity is most likely constant rather than logarithmic.

#### Q 7: Trees

* binary heap tree: has largest element at root, smaller elements are below. Can be reversed for binary min heap 
(smallest element at top).
* binary search tree: right has larger elements, left has small elements
* red-black binary search tree: max depth difference is 1, root is (almost always) black, red nodes have two black
children.
* implicit tree of quick-union: can't have duplicate keys
* minimum-oriented priority queue: smallest item at root

#### Q 8: Graph DFS

* DRAW GRAPHS -> USE COLOURS
* assume we perform DFS, gives a search tree *T*, *u* and *v* are two vertices, does *T* contain a path containing *s*,
 *u*, *v* in that order: NO, if you go back on yourself in the graph it doesn't work.
 
 #### Q 9: Kruskal's algorithm
 
 * suggest a modification to Kruskal's algorithm (a greedy algorithm that finds a minimum spanning tree for connected
 weighted graph) that exploits negative and positive weights and give a better running time than the normal algorithm:
 *e* edges *a* are -1 weight and *b* have 1 weight. so *a*+*b*=*e*. To reduce complexity overall we reduce complexity
 of one subpart. First perform algorithm on *a* edges, if it completes then you have reduced the overall time. If not
 then you add *b* edges too.
 
 #### Q 10: Graph Theory
 
 * a directed graph is called an *oriented graph* IF no 2 vertices are linked by two symmetric edges.
 * *acyclic graph* has no graph cycles 
 * an algorithm that finds an orientation such that the new directed graph is acyclic: start at node *s* and run
 modified DFS that marks vertices as 'half done' if they have been visited but still need to be processed.
 * COMPLETE GRAPH: number of edges *e* = *v* (*v* - 1) / 2 
 * a complete undirected graph with 6 vertices, how many orientations are there to make it acyclic: 6(6-1)/2=15 edges
 * a Digraph with 6 vertices can be [1,2,3,4,5,6] or [1,2,3,4,6,5] or [1,2,3,6,5,4] etc there are 6! combinations.
 there are n! orientations that make a graph acyclic.
 
 #### General info for part B
 
 * nullifying is used to avoid loitering (i.e. leaving memory that the garbage collection will be unable to reclaim)
 * an invariant is kept true throughout the execution of the program
 * the invariant of **maximum-oriented priority queues**: the value in all nodes is great than its two children
 * a directed graph is said to be *semi-connected* if there exists some path from vertex *a* to *b*, or *b* to *a* or
 in both directions.