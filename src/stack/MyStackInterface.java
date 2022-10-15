package stack;

public interface MyStackInterface<T> {
    T push(T data);
    T pop();
    T peek();
    int size();
    boolean isEmpty();
    MyStack<T> grow(int newSize);
}
