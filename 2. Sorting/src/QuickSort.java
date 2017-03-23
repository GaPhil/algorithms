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