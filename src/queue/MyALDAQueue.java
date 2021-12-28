package queue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyALDAQueue<E> implements ALDAQueue<E> {

    private Node<E> first;
    private Node<E> last;
    private int totalCapacity;
    private int currentCapacity;

    private static class Node<E> {
        private E data;
        private Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    public MyALDAQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Error: The size of MyALDAQueue must be greater than 0!");
        }
        totalCapacity = capacity;
        currentCapacity = totalCapacity;
        first = new Node<E>(null); //Sentinel node

    }

    @Override
    public void add(E element) {
        if (isFull()) {
            throw new IllegalStateException("Error: MyALDAQueue is full!");
        }

        if (element == null) {
            throw new NullPointerException();
        }

        if (isEmpty()) {
            first.next = new Node<E>(element);
            last = first.next;
        } else {
            last.next = new Node<E>(element);
            last = last.next;
        }
        currentCapacity--;
    }

    @Override
    public void addAll(Collection<? extends E> c) {
        for (E element : c) {
            add(element);
        }
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Error: MyALDAQueue is empty!");
        }

        E data = first.next.data;
        first.next = first.next.next;
        currentCapacity++;
        return data;
    }

    @Override
    public E peek() {
        if (first.next == null) {
            return null;
        }
        return first.next.data;
    }

    @Override
    public void clear() {
        first.next = null;
        last = null;
        currentCapacity = totalCapacity;
    }

    @Override
    public int size() {
        return totalCapacity - currentCapacity;
    }

    @Override
    public boolean isEmpty() {
        return totalCapacity == currentCapacity;
    }

    @Override
    public boolean isFull() {
        return currentCapacity == 0;
    }

    @Override
    public int totalCapacity() {
        return totalCapacity;
    }

    @Override
    public int currentCapacity() {
        return currentCapacity;
    }

    @Override
    public int discriminate(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        Iterator<E> iterator = iterator();
        MyALDAQueue<E> toBeAppended = new MyALDAQueue<>(totalCapacity);
        int n = 0;

        while (iterator.hasNext()) {
            E current = iterator.next();
            if (current.equals(e)) {
                iterator.remove();
                toBeAppended.add(current);
                n++;
            }
        }
        for (E element : toBeAppended) {
            this.add(element);
        }
        return n;
    }


    private class MyQueueIterator implements Iterator {

        private Node<E> prev;
        private Node<E> current;
        private boolean okToRemove;

        MyQueueIterator() {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current.next != null;
        }

        @Override
        public Object next() {
            if (current == null) {
                throw new NoSuchElementException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            prev = current;
            current = current.next;
            okToRemove = true;
            return current.data;
        }

        @Override
        public void remove() {
            if (okToRemove) {
                prev.next = current.next;
                current = prev;
                okToRemove = false;
                currentCapacity++;

                if (current.next == null){
                    last = current;
                }
            }
        }
    }



    @Override
    public Iterator<E> iterator() {
        return new MyQueueIterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Node<E> temp = first.next; temp != null; temp = temp.next) {
            sb.append(temp.data);
            if (temp.next != null) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}