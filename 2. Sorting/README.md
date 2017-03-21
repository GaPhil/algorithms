##### 2. Sorting

###### Selection Sort

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

###### Insertion Sort

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