// Herman Westerman hewe1050

// Samarbete gjordes med Fredrik Engström fren2861


import java.util.*;

public class SkipList<T extends Comparable<T>> {

    private ArrayList<Node<T>> first;
    private ArrayList<Node<T>> last;
    private Random rng = new Random();
    private int maxHeight;
    private int size;

    public SkipList() {
        first = new ArrayList<>();
        last = new ArrayList<>();

        first.add(new Node<>(null));
        last.add(new Node<>(null));

        //Sentinel nodes
        first(0).next.set(0, last(0));
        last(0).previous.set(0, first(0));
    }

    private static class Node<T> {
        T data;
        int level;
        ArrayList<Node<T>> previous;
        ArrayList<Node<T>> next;


        public Node(T item) {
            this(item, 0);
        }

        public Node(T item, int level) {
            data = item;
            this.level = level;

            next = new ArrayList<>(level + 1);
            previous = new ArrayList<>(level + 1);

            for (int i = 0; i < level + 1; i++) {
                next.add(null);
                previous.add(null);
            }

        }

        boolean hasNext(int index) {
            if (index >= next.size())
                return false;
            if (next.get(index) == null)
                return false;
            return true;
        }

        Node<T> next(int level) {
            return next.get(level);
        }

        Node<T> previous(int level) {
            return previous.get(level);
        }
    }

    private Node<T> first(int level) {
        return first.get(level);
    }

    private Node<T> last(int level) {
        return last.get(level);
    }

    /**
     * Adds an item to the skiplist. If item is null, an IllegalArgumentException is thrown.
     *
     * @param item The item to be added. Cannot be null.
     */
    public void add(T item) {

        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }

        size++;
        int level = 0;
        while (rng.nextBoolean()) {
            level++;
        }
        if (level > maxHeight) {
            maxHeight = level;
            for (int i = first.size(); i <= maxHeight; i++) {
                first.add(new Node<>(null, level));
                last.add(new Node<>(null, level));

                //Sentinel nodes
                first(i).next.set(i, last(i));
                last(i).previous.set(i, first(i));
            }
        }
        Node<T> newNode = new Node<>(item, level);
        LinkedList<Node<T>> path = findPath(item);

        for (int i = 0; i <= level; i++) {
            Node<T> here = path.removeLast();
            if (here == null) {
                //Add last
                last(i).previous(i).next.set(i, newNode);
                newNode.previous.set(i, last(i).previous(i));
                newNode.next.set(i, last(i));
                last(i).previous.set(i, newNode);

            } else {
                //Add before here
                newNode.next.set(i, here);
                newNode.previous.set(i, here.previous(i));
                if (here.previous(i) != null) {
                    here.previous.set(i, newNode);
                    newNode.previous(i).next.set(i, newNode);
                }
            }
        }

    }

    /**
     * Compares two data. If the left reference a is null, it is smaller than all items. If the right reference b is null, it's larger than all items.
     *
     * @param a
     * @param b
     * @return
     */
    private int compareData(T a, T b) {
        if (a == null || b == null) {
            return -1;
        }
        return a.compareTo(b);
    }

    private LinkedList<Node<T>> findPath(T item) {
        int level = maxHeight;

        LinkedList<Node<T>> path = new LinkedList<>();
        Node<T> currNode = first(level).next(level);

        while (true) {
            if (level < 0) {
                return path;
            }
            if (compareData(item, currNode.data) >= 0) {
                if (compareData(item, currNode.next(level).data) >= 0) {
                    currNode = currNode.next(level);
                } else {
                    path.add(currNode.next(level));
                    level--;
                }
            } else {
                path.add(currNode);
                level--;
                if (level < 0) {
                    return path;
                }
                currNode = first(level).next(level);
            }
        }
    }

    private Node<T> find(T item) {
        Node<T> found = findPath(item).getLast().previous(0);
        if (item.equals(found.data)) {
            return found;
        }
        return null;
    }

    public boolean remove(T item) {
        Node<T> found = find(item);

        if (found == null) {
            return false;
        }
        size--;
        for (int i = found.level; i >= 0; i--) {
            found.previous(i).next.set(i, found.next(i));
            found.next(i).previous.set(i, found.previous(i));
        }
        return true;
    }

    public boolean contains(T item) {
        Node<T> found = find(item);
        return found != null;
    }

    public int size() {
        return size;
    }

    /**
     * Denna metod är enbart till för testning. Den ska returnera antalet nivåer för
     * nod nr i. Skyddsnivån är avsiktlig.
     */
    int levelOfNode(int i) {
        Node<T> currNode = first(0).next(0);
        int n = 0;
        while (currNode.data != null) {
            if (n == i) {
                return currNode.level + 1;
            }
            currNode = currNode.next(0);
            n++;
        }
        return -1;
    }

    @Override
    public String toString() {
        ArrayList<StringBuilder> stringBuilders = new ArrayList<>();
        for (int i = 0; i <= maxHeight; i++) {
            stringBuilders.add(new StringBuilder());

        }
        Node<T> currNode = first(0).next(0);

        int width = last(0).previous(0).data.toString().length();
        while (currNode.hasNext(0)) {
            for (int i = 0; i < stringBuilders.size(); i++) {
                if (currNode.level >= i) {
                    int spacing = width - currNode.data.toString().length();
                    for (int j = 0; j < spacing; j++) {
                        stringBuilders.get(i).append(" ");
                    }

                    stringBuilders.get(i).append(currNode.data);
                } else {
                    for (int j = 0; j < width; j++) {
                        stringBuilders.get(i).append(" ");
                    }
                }
                stringBuilders.get(i).append(" ");
            }
            currNode = currNode.next(0);
        }
        StringBuilder result = new StringBuilder();
        for (int i = maxHeight; i >= 0; i--) {
            result.append(stringBuilders.get(i).toString()).append("\n");
        }

        return result.toString();
    }

    public String toString(int index) {
        StringBuilder sb = new StringBuilder();
        Node<T> currNode;
        try {
            currNode = first(index);
        } catch (IndexOutOfBoundsException ex) {
            return "[]";
        }

        sb.append('[');
        sb.append(currNode.data);
        while (currNode.hasNext(index)) {
            currNode = currNode.next.get(index);
            sb.append(", ");
            sb.append(currNode.data);
        }
        sb.append(']');
        return sb.toString();
    }
}
