public class HeapSort {

    public static void sort(Comparable[] a) {
        int n = a.length;
        for (int k = n / 2; k >= 1; k--) {
            HelperHeap.sink(a, k, n);
        }
        while (n > 1) {
            HelperHeap.exch(a, n, n--);
            HelperHeap.sink(a, 1, n);
        }
    }
}