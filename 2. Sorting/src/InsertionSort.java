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