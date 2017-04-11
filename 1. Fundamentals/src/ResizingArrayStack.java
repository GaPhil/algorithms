import java.util.Iterator;

public class ResizingArrayStack<Item> implements Iterable<Item> {

    private Item[] a = (Item[]) new Object[1];      // stack of items
    private int n = 0;                              // number of items

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    private void resize(int max) {
        // Move stack to a new array of size max.
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    public void push(Item item) {
        // Add item to top of stack
        if (n == a.length) {
            resize(2 * a.length);
            a[n++] = item;
        }
    }

    public Item pop() {
        // Remove item from top of stack
        Item item = a[--n];
        a[n] = null;                                // avoid loitering
        if (n > 0 && n == a.length / 4) {
            resize(a.length / 2);
        }
        return item;
    }

    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        // Support LIFO iteration.
        private int i = n - 1;

        public boolean hasNext() {
            return i >= 0;
        }

        public Item next() {
            return a[i--];
        }

        public void remove() {

        }
    }
}