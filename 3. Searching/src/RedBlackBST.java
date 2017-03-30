public class RedBlackBST<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;

    private class Node {
        Key key;
        Value val;
        Node left;
        Node right;
        int n;
        boolean colour;

        Node(Key key, Value val, int n, boolean colour) {
            this.key = key;
            this.val = val;
            this.n = n;
            this.colour = colour;
        }
    }

    private boolean isRed(Node x) {
        if (x == null) {
            return false;
        }
        return x.colour == RED;
    }

    Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.colour = h.colour;
        h.colour = RED;
        x.n = h.n;
        h.n = 1 + size(h.left) + size(h.right);
        return x;
    }

    Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.colour = h.colour;
        h.colour = RED;
        x.n = h.n;
        h.n = 1 + size(h.left) + size(h.right);
        return x;
    }

    void flipColours(Node h) {
        h.colour = RED;
        h.left.colour = BLACK;
        h.right.colour = BLACK;
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        } else {
            return x.n;
        }
    }

    public int size() {
        return size(root);
    }

    public void put(Key key, Value val) {
        root = put(root, key, val);
        root.colour = BLACK;
    }

    private Node put(Node h, Key key, Value val) {
        if (h == null) {
            return new Node(key, val, 1, RED);
        }
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = put(h.left, key, val);
        } else if (cmp > 0) {
            h.right = put(h.right, key, val);
        } else {
            h.val = val;
        }

        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColours(h);
        }

        h.n = size(h.left) + size(h.right) + 1;
        return h;
    }
}