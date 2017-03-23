### 2. Sorting

#### 2.1 Elementary Sorts

##### Selection Sort

```java
public class SelectionSort {

    public static Comparable[] sort(Comparable[] a) {

        for (int i = 0; i < a.length; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (Helper.less(a[j], a[min])) {
                    min = j;
                }
            }
            Helper.exchange(a, i, min);
        }
        return a;
    }
}
```
For each `i`, this implementation puts the (`i`+1)st smallest item in `a[a]`. The entries to the left of position `i` 
are the `i` smallest items in the array and are not examined again.

Selection sort uses ~ *n*<sup>2</sup> / 2 compares and *n* exchanges to sort an array of length *n*.
For each *i* from 0 to *n* - 1, there is one exchange and *n* - 1 - *i* compares, so the totals 
are *n* exchanges and (*n* - 1) + (*n* - 2) + ... + 2 + 1 + 0 = *n*(*n* - 1) / 2 <br> ~ *n* <sup>2</sup> / 2 compares.

##### Insertion Sort

```java
public class InsertionSort {

    public static Comparable[] sort(Comparable[] a) {

        for (int i = 0; i < a.length; i++) {
            for (int j = i; j > 0 && Helper.less(a[j], a[j - 1]); j--) {
                Helper.exchange(a, j, j - 1);
            }
        }
        return a;
    }
}
```
For each `i` from 1 to `n`-1, exchange `a[i]` with the entries that are larger in `a[0]` through `a[i-1]`. As the index
`i` travels from left to right, the entries to its left are in sorted order in the array, so the array is fully sorted 
when `i` reaches the right end.

Insertion Sort uses  ~ *n*<sup>2</sup> / 4 compares and ~ *n*<sup>2</sup> / 4 exchanges to sort a 
randomly ordered array of length *n* with distinct keys, on average. The worst case is ~ *n*<sup>2</sup> / 2
compares and ~ *n* <sup>2</sup> / 2 exchanges and the best case is *n* - 1 compares and 0 exchanges.

##### Shellsort

```java
public class Shellsort {

    public static Comparable[] sort(Comparable[] a) {

        int n = a.length;
        int h = 1;
        while (h < n / 3) {
            h = 3 * h + 1;
        }
        while (h >= 1) {
            for (int i = h; i < n; i++) {
                for (int j = i; j >= h && Helper.less(a[j], a[j - h]); j -= h) {
                    Helper.exchange(a, j, j - h);
                }
            }
            h = h / 3;
        }
        return a;
    }
}
```
If we modify insertion sort to `h`-sort the array and add an outer loop to decrease the `h` through a sequence of 
increments starting at an increment as large as a constant fraction of the array length and ending at 1, we are led to
this compact shellsort implementation.

The number of compares used by shellsort with the increments 1, 4, 13, 40, 121, 364, ... is bounded by a small
multiple of *n* times the number of increments used Extensive experiments suggest that the average number of 
compares per increment might be *n*<sup>1/5</sup>, but it is quite difficult to discern the growth in that 
function unless *n* is huge.

#### 2.2 Mergesort

```java
public class Merge {

    public static Comparable[] merge(Comparable[] a, int lo, int mid, int hi) {

        Comparable[] aux = new Comparable[a.length];
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (Helper.less(aux[j], aux[i])) {
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
        return a;
    }
}
```
This method merges by first copying into the auxiliary array `aux[]` then merging back to `a[]`. In the merge (the 
second `for` loop), there are four conditions:left half exhausted (take from the right), right half exhausted (take from
the left), current key on right less than current key on left (take from the right), and current key on right greater 
than or equal to current key on the left(take from the left).

##### Top-down mergesort

```java
public class MergeSort {

    private static Comparable[] aux;

    public static Comparable[] sort(Comparable[] a) {

        aux = new Comparable[a.length];
        sort(a, 0, a.length - 1);
        return a;
    }

    private static Comparable[] sort(Comparable[] a, int lo, int hi) {

        if (hi <= lo) {
            return a;
        }
        int mid = lo + (hi - lo) / 2;
        sort(a, lo, mid);
        sort(a, mid + 1, hi);
        Merge.merge(a, lo, mid, hi);
        return a;
    }
}
```
To sort a subarray `a[lo..hi]` we divide it into two parts: `a[lo..mid]` and `a[mid+1..hi]`, sort them independently 
(via recursive calls), and merge the resulting ordered subarrays to produce the result.

Top-down mergesort uses between 1/2 *n* lg *n* and *n* lg *n* compares to sort an array of length *n*. Top-down uses at
most 6*n* lg *n* array accesses to sort an array of length *n*.

##### Bottom-up mergesort

```java
public class MergeSortBU {

    private static Comparable[] aux;

    public static Comparable[] sort(Comparable[] a) {
        int n = a.length;
        aux = new Comparable[n];
        for (int len = 1; len < n; len *= 2) {
            for (int lo = 0; lo < n - len; lo += len + len) {
                Merge.merge(a, lo, lo + len - 1, Math.min(lo + len + len - 1, n - 1));
            }
        }
        return a;
    }
}
```
Bottom-up mergesort consists of a sequence of passes over the whole array, doing `len`-by-`len` merges, starting with 
`len` equal to 1 and doubling `len` on each pass. The final subarray is of length `len` only when the array length is 
an even multiple of `len` (otherwise it is less than `len`).

Bottom-up mergesrot uses between 1/2 *n* lg *n* and *n* lg *n* compares and at most 6*n* lg *n* array accesses to sort
an array of length *n*.

Mergesort is an asymptotically optimal compare-base sorting algorithm. That means *both the number of compares used by 
mergesort in the worst case and the minimum number of compares that any compare-based sorting algorithm can guarantee 
are ~ *n* lg *n*.*

#### 2.3 QuickSort

##### Quicksort partitioning
```java
public class QuickSortPartition {

    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];
        while (true) {
            while (Helper.less(a[i++], v)) {
                if (i == hi) {
                    break;
                }
            }
            while (Helper.less(v, a[--j])) {
                if (j == lo) {
                    break;
                }
            }
            if (i >= j) {
                break;
            }
        }
        Helper.exchange(a, i, j);
        return j;
    }
}
```
This code partitions on the item `v` in `a[lo]`. The main loop exists when the indices `i` and `j` cross. Within the 
loop, we increment `i` while `a[i]` is less than `v` and decrement `j` while `a[j]` is greater than `v`, then do and 
exchange to maintain the invariant property that no entries to the left of `i` are greater than `v` and no entries to
the right of `j` are smaller than `v`. Once the indices meet, we complete the partitioning by exchanging `a[lo]` with 
`a[j]` (thus leaving the partitioning value in `a[j]`).

##### Quicksort

```java
public class QuickSort {

    static Comparable[] sort(Comparable[] a) {

        Helper.shuffle(a);
        // for 2-way partition
        // sort(a, 0, a.length - 1);
        QuickSortPartition3Way.sort(a, 0, a.length - 1);
        return a;
    }

    private static Comparable[] sort(Comparable[] a, int lo, int hi) {

        if (hi <= lo) {
            return a;
        }
        int j = QuickSortPartition.partition(a, lo, hi);
        sort(a, lo, j - 1);
        sort(a, j + 1, hi);
        return a;
    }
}
```
Quicksort is a recursive program that sorts a subarray `a[lo..hi]` by using a `partition()` method that puts `a[j]` 
into position and arranges the rest of the entries such that the recursive calls finish the sort.

Quicksort uses ~ 2 *n* ln *n* compares (and one-sixth that many exchanges) on the average to sort and array of length
*n* with distinct keys.

##### Quicksort with 3-way partitioning 

```java
public class QuickSortPartition3Way {

    public static Comparable[] sort(Comparable[] a, int lo, int hi) {

        if (hi <= lo) {
            return a;
        }
        int lt = lo;
        int i = lo + 1;
        int gt = hi;
        Comparable v = a[lo];
        while (i <= gt) {
            int cmp = a[i].compareTo(v);
            if (cmp < 0) {
                Helper.exchange(a, lt++, i++);
            } else if (cmp > 0) {
                Helper.exchange(a, i, gt--);
            } else i++;
        }
        sort(a, lo, lt - 1);
        sort(a, gt + 1, hi);
        return a;
    }
}
```
This sort code partitions to put keys equal to the partitioning element in place and thus does not have to include
those keys in the subarrays for the recursive calls. It is far more efficient than the standard quicksort
implementation for arrays with large numbers of duplicate keys.

Quicksort with 3-way partitioning uses ~ (2 ln 2) *n H*  compares to sort *N* items, where *H* is the Shannon entropy,
defined from the frequencies of key values. Note that *H* = lg *n* when the keys are all distinct.

#### Summary

The table below summarizes the number of compares for a variety of sorting algorithms, as implemented 
in the textbook. It includes leading constants but ignores lower-order terms.

|ALGORITHM     |IN PLACE|STABLE|BEST                       |AVERAGE            |WORST              |REMARKS|
|--------------|:------:|:----:|:-------------------------:|:-----------------:|:-----------------:|:-----:|
|selection sort|x       |      |1/2 *n*<sup>2</sup>        |1/2 *n*<sup>2</sup>|1/2 *n*<sup>2</sup>|       |
|insertion sort|x       |x     |*n*                        |1/4 *n*<sup>2</sup>|1/2 *n*<sup>2</sup>|       |
|bubble sort   |x       |x     |*n*                        |1/2 *n*<sup>2</sup>|1/2 *n*<sup>2</sup>|       |
|shellsort     |x       |      |1/2 *n* log<sub>3</sub> *n*|unknown            |c *n*<sup>3/2</sup>|       |
|mergesort     |        |x     |1/2 *n* lg *n*             |*n* lg *n*         |*n* lg *n*         |       |
|quicksort     |x       |      |*n* lg *n*                 |2 *n* lg *n*       |1/2 *n*<sup>2</sup>|       |
|heapsort      |x       |      |*n* lg *n* <sup>*</sup>    |2 *n* lg *n*       |2 *n* lg *n*       |       |

<sup>*</sup> *n* lg *n* if all keys are distinct