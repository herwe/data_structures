package dary_heap;

/**
 * Implements a binary heap.
 * Note that all "matching" is based on the compareTo method.
 *
 * @author Mark Allen Weiss
 */
public class DHeap<T extends Comparable<? super T>> {

    private static final int DEFAULT_CAPACITY = 10;

    private int currentSize;      // Number of elements in heap
    private T[] array; // The heap array
    private int maxNumberOfChildren;

    /**
     * Construct a binary heap.
     */
    public DHeap() {
        this(2);
    }

    /**
     * Construct the d-ary heap.
     *
     * @param d the number of maximum children per node.
     */
    public DHeap(int maxNumberOfChildren) {
        if (maxNumberOfChildren < 2) {
            throw new IllegalArgumentException();
        }
        this.maxNumberOfChildren = maxNumberOfChildren;
        currentSize = 0;
        array = (T[]) new Comparable[DEFAULT_CAPACITY + 1];
    }

    public int firstChildIndex(int i) {
        if (i < 1) {
            throw new IllegalArgumentException();
        }
        return i * maxNumberOfChildren - maxNumberOfChildren + 2;
    }

    public int parentIndex(int i) {
        if (i <= 1) {
            throw new IllegalArgumentException();
        }
        return (i + maxNumberOfChildren - 2) / maxNumberOfChildren;
    }

    /**
     * Returns the index of the parent, or zero if this is the top node.
     */
    public int parentIndexOrZero(int i) {
        return (i + maxNumberOfChildren - 2) / maxNumberOfChildren;
    }


    public int minChildIndex(int i) {
        int firstChildIndex = firstChildIndex(i);
        int minChildIndex = firstChildIndex;

        T minValue = array[firstChildIndex];

        for (int j = 1; j < maxNumberOfChildren; j++) {
            if (firstChildIndex + j <= currentSize) {
                if (array[firstChildIndex + j] != null && minValue.compareTo(array[firstChildIndex + j]) > 0) {
                    minChildIndex = firstChildIndex + j;
                    minValue = array[firstChildIndex + j];
                }
            }
        }
        return minChildIndex;
    }

    /**
     * Insert into the priority queue, maintaining heap order.
     * Duplicates are allowed.
     *
     * @param x the item to insert.
     */
    public void insert(T x) {
        if (currentSize == array.length - 1)
            enlargeArray(array.length * 2 + 1);

        // Percolate up
        int hole = ++currentSize;

        for (array[0] = x; x.compareTo(array[parentIndexOrZero(hole)]) < 0; hole = parentIndexOrZero(hole)) {
            array[hole] = array[parentIndexOrZero(hole)];
        }
        array[hole] = x;
    }


    private void enlargeArray(int newSize) {
        T[] old = array;
        array = (T[]) new Comparable[newSize];
        for (int i = 0; i < old.length; i++)
            array[i] = old[i];
    }

    /**
     * Find the smallest item in the priority queue.
     *
     * @return the smallest item, or throw an UnderflowException if empty.
     */
    public T findMin() {
        if (isEmpty())
            throw new IllegalStateException();
        return array[1];
    }

    /**
     * Remove the smallest item from the priority queue.
     *
     * @return the smallest item, or throw an UnderflowException if empty.
     */
    public T deleteMin() {
        if (isEmpty())
            throw new IllegalStateException();

        T minItem = findMin();
        array[1] = array[currentSize--];
        percolateDown(1);

        return minItem;
    }

    /**
     * Establish heap order property from an arbitrary
     * arrangement of items. Runs in linear time.
     */
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--)
            percolateDown(i);
    }

    /**
     * Test if the priority queue is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Make the priority queue logically empty.
     */
    public void makeEmpty() {
        currentSize = 0;
    }


    /**
     * Internal method to percolate down in the heap.
     *
     * @param hole the index at which the percolate begins.
     */
    private void percolateDown(int hole) {
        int child;
        T tmp = array[hole];

        while (firstChildIndex(hole) <= currentSize) {
            child = minChildIndex(hole);
            if (array[child].compareTo(tmp) >= 0)
                break;

            array[hole] = array[child];
            hole = child;
        }
        array[hole] = tmp;
    }

    T get(int index) {
        return array[index];
    }

    public int size() {
        return currentSize;
    }


}