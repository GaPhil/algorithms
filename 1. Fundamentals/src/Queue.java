public class Queue<Item> implements Iterable<Item> {

    private Node first;                 // link to least recently added node
    private Node last;                  // link to most recently added node
    private int n;                      // number of items in queue

    private class Node {
        // nested class to define nodes
        Item item;
        Node next;
    }




}