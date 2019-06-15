import java.util.Arrays;
import java.util.EmptyStackException;

/***
 * Represents the Stack implementation.
 * This structure will be used regarding to the matrix problem and
 * in the infix calculation.
 * @param <E> is the Object which will be stored.
 * @author Ahmed Semih Özmekik
 */


public class MyStack<E>
{

    private E[] buffer; // Underlying data field.
    private int size = 0; // Size of the stack.
    private int index = -1; // Index in data buffer, top of the stack.
    private int capacity = 10; // Initial capacity is 10.


    /**
     * Creates an empty stack.
     */
    @SuppressWarnings("unchecked cast")
    public MyStack()
    {
        buffer = (E[]) new Object[capacity];
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Looks at the element at the top of this stack without
     * removing it from stack.
     * @return The element at the top of the stack.
     */
    public E peek()
    {
        if (isEmpty())
            throw new EmptyStackException();
        return buffer[index];
    }

    /**
     * Removes the element at the top of this stack and
     * returns it.
     * @return The element at the top of the stack.
     */
    public E pop()
    {
        if (isEmpty())
            throw new EmptyStackException();
        --size;
        return buffer[index--];
    }

    /**
     * Pushes an item onto top of this stack.
     * @param item element to push onto top.
     */
    public void push(E item)
    {
        if (size == capacity)
            allocate();
        ++size;
        buffer[++index] = item;
    }

    /**
     * Allocates space if the capacity is getting full.
     */
    private void allocate()
    {
        capacity*=2; // double the capacity.

        buffer = Arrays.copyOf(buffer, capacity);
    }

     public void print()
     {
        for (int i=index;i>-1;--i)
            System.out.println(buffer[i].toString());
     }
}
