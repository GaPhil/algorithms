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