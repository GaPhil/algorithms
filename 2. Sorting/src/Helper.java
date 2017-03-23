import java.util.Random;

public class Helper {

    public static boolean less(Comparable x, Comparable y) {
        return x.compareTo(y) < 0;
    }

    public static void exchange(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    static void print(Comparable[] a) {
        for (Comparable i : a) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    static boolean isSorted(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            if (less(a[i], a[i - 1]))
                return false;
        }
        return true;
    }

    private static Random random;    // pseudo-random number generator

    // static initializer
    static {
        long seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    private static int uniform(int n) {
        if (n <= 0) throw new IllegalArgumentException("argument must be positive");
        return random.nextInt(n);
    }

    static void shuffle(Object[] a) {
        if (a == null) {
            throw new IllegalArgumentException("argument array is null");
        }
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int r = i + uniform(n - i);      // between i and n-1
            Object temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }
}