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