public class QuicksortPartition {

    public static int partition(Comparable[] a, int lo, int hi) {
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