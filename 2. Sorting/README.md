#####2. Sorting

######Selection Sort

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