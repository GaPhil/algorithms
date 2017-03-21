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
Selection sort uses ~ *n* <sup>2</sup> / 2 compares and *n* exchanges to sort an array of length *n*.
For each *i* from 0 to *n* - 1, there is one exchange and *n* - 1 - *i* compares, so the totals 
are *n* exchanges and (*n* - 1) + (*n* - 2) + ... + 2 + 1 + 0 = *n*(*n* - 1) / 2 ~ *n* <sup>2</sup> / 2 compares.

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
Insertion Sort uses  ~ *n* <sup>2</sup> / 4 compares and ~ *n* <sup>2</sup> / 4 exchanges to sort a 
randomly ordered array of length *n* with distinct keys, on average. The worst case is ~ *n* <sup>2</sup> / 2
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
The number of compares used by shellsort with the increments 1, 4, 13, 40, 121, 364, ... is bounded by a small
multiple of *n* times the number of increments used Extensive experiments suggest that the average number of 
compares per increment might be *n* <sup>1/5</sup>, but it is quite difficult to discern the growth in that 
function unless *n* is huge.

#### Summary

The table below summarizes the number of compares for a variety of sorting algorithms, as implemented 
in the textbook. It includes leading constants but ignores lower-order terms.

| ALGORITHM      | IN PLACE | STABLE | BEST                        | AVERAGE              | WORST                | REMARKS |
| -------------- | :------: | :----: | :-------------------------: | : -----------------: | :------------------: | : ----: |
| selection sort | x        |        | 1/2 *n*<sup>2</sup>         | 1/2 *n* <sup>2</sup> | 1/2 *n* <sup>2</sup> |         |
| insertion sort | x        | x      | *n*                         | 1/4 *n* <sup>2</sup> | 1/2 *n* <sup>2</sup> |         |
| bubble sort    | x        | x      | *n*                         | 1/2 *n* <sup>2</sup> | 1/2 *n* <sup>2</sup> |         |
| shellsort      | x        |        | 1/2 *n* log<sub>3</sub> *n* | unknown              | c *n* <sup>3/2</sup> |         |
| mergesort      |          | x      | 1/2 *n* lg *n*              | *n* lg *n*           | *n* lg *n*           |         |
| quicksort      | x        |        | *n* lg *n*                  | 2 *n* lg *n*         | 1/2 *n* <sup>2</sup> |         |
| heapsort       | x        |        | *n* lg *n* <sup>*</sup>     | 2 *n* lg *n*         | 2 *n* lg *n*         |         |

<sup>*</sup> *n* lg *n* if all keys are distinct