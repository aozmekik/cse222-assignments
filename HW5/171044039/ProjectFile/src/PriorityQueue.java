import java.util.*;

/**
 * Represents the Priority Queue with array based heap implementation.
 * @author Ahmed Semih Özmekik
 */
public class PriorityQueue<E> extends AbstractQueue<E>
        implements Queue<E>
{
    private Comparator<E> comparator = null; // generic comparisons.
    private E[] theData; // underlying heap array.
    private int capacity = 10; // initial capacity.
    private int size = 0;
    private int index = -1;

    @SuppressWarnings("unchecked cast")
    public PriorityQueue()
    {
        theData = (E[]) new Object[capacity];

    }

    /**
     * Returns the Q with specified comparison scheme.
     * @param comparator is the comparison scheme.
     */
    public PriorityQueue(Comparator<E> comparator)
    {
        this();
        this.comparator = comparator;
    }

    /**
     * Returns the size of the queue.
     * @return the size.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Adds a new element.
     * @param e new element to be added.
     */
    @Override
    public boolean offer(E e) {
        if (size+1>capacity) // capacity is reached
            expand();

        ++index;
        ++size;
        theData[index] = e; // added.

        int child = index; // newly inserted item.
        int parent = (child-1)/2;

        // While is not root and the child is smaller then the parent.
        while(parent>=0 && compare(theData[parent], theData[child])<0){
            // Swap them.
            E temp = theData[parent];
            theData[parent] = theData[child];
            theData[child] = temp;

            child = parent; // child becomes parent.
            parent =  (child-1)/2; // get new parent.
        }
        return true;
    }

    /**
     * Gets the maximum item of the Q and removes it.
     * @return the max item of the Q.
     */
    @Override
    public E poll() {
        if (isEmpty())
            return null;

        // Save the top.
        E result = theData[0];

        if (size() == 1){ // There is only one node which is the root, in the heap.
            index = -1;
            size = 0;
            return result;
        }

        --size;
        // Remove the last item from the array and place it into the
        // first position.
        theData[0] = theData[index];
        --index;

        int parent = 0;
        while(true){
            int leftChild = 2*parent + 1;
            int rightChild = leftChild + 1;

            if(leftChild >= size())
                break;

            int minChild = leftChild;

            if (rightChild < size() &&
            compare(theData[leftChild], theData[rightChild])<0)
                minChild = rightChild;

            if (compare(theData[parent], theData[minChild]) < 0){
                // Swap them.
                E temp = theData[parent];
                theData[parent] = theData[minChild];
                theData[minChild] = temp;

                parent = minChild;
            }
            else // the heap is recovered.
                break;
        }
        return result;
    }

    /**
     * Gets the max element of the Q.
     * @return the max element of the Q.
     */
    @Override
    public E peek() {
        return theData[0];
    }

    @SuppressWarnings("unchecked cast")
    private int compare(E e1, E e2)
    {
        if(comparator != null) // comparator is defined.
            return comparator.compare(e1, e2);
        else
            return ((Comparable<E>) e1).compareTo(e2);
    }

    /**
     * Expand the capacity of array as double and maintain the array.
     */
    private void expand()
    {
        capacity*=2;
        theData = Arrays.copyOf(theData, capacity);
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
