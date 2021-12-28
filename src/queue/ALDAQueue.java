package queue;

import java.util.Collection;
public interface ALDAQueue<E> extends Iterable<E> {

    public void add(E element);

    public void addAll(Collection<? extends E> c);

    public E remove();

    public E peek();

    public void clear();

    public int size();

    public boolean isEmpty();

    public boolean isFull();

    /**
     * Set when creating the queue.
     */
    public int totalCapacity();

    public int currentCapacity();

    /**
     * Move all elements equal to e to the end of the queue.
     *
     * @param e
     * @throws NullPointerException if e is null.
     * @return the number of elements discriminated.
     */
    public int discriminate(E e);

}

