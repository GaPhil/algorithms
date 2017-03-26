public class MaxPQ<Key extends Comparable<Key>> {

    private Key[] pq;
    private int n = 0;

    public MaxPQ(int maxN) {
        pq = (Key[]) new Comparable[maxN + 1];
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void insert(Key v) {
        pq[++n] = v;
        HelperHeap.swim(n);
    }

    public Key delMax() {
        Key max = pq[1];
        HelperHeap.exch(1, n--);
        pq[n + 1] = null;
        HelperHeap.sink(1);
        return max;
    }
}