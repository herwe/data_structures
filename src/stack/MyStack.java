package stack;

public class MyStack<T> implements MyStackInterface<T> {
    private T[] arr;
    private int next;

    public MyStack(int size) {
        arr = (T[]) new Object[size];
        next = 0;
    }

    @Override
    public T push(T data) {
        if (data == null) throw new NullPointerException("Value must not be null");
        if (arr[arr.length-1] != null) throw new IllegalStateException("Stack is full");
        arr[next] = data;
        next++;
        return data;
    }

    @Override
    public T pop() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        next--;
        T removed = arr[next];
        arr[next] = null;
        return removed;
    }

    @Override
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        return arr[--next];
    }

    @Override
    public int size() {
        return next;
    }

    @Override
    public boolean isEmpty() {
        return next == 0;
    }
    
    @Override
    public MyStack<T> grow(int newSize) {
        if (arr.length >= newSize) throw new IllegalStateException("Stack must grow larger than size of old stack");
        MyStack<T> copy = new MyStack<>(newSize);
        System.arraycopy(arr, 0, copy.arr, 0, arr.length);
        copy.next = next;
        arr = copy.arr;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = arr.length-1; i >= 0; i--) {
            if (arr[i] != null) {
                stringBuilder.append(arr[i]).append(",");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1).append("]");
        return stringBuilder.toString();
    }
}
