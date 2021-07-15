package synthesizer;

import java.util.Iterator;

/**
 * Interface of a BoundedQueue.
 * @param <T>
 * @author Lingfeng Chen
 */
public interface BoundedQueue<T> extends Iterable<T> {
    /**queue's capacity.
     * @return int*/
    int capacity();
    /**queue's current number of elements.
     * @return int*/
    int fillCount();
    /**enqueue abstract method.
     * @param x */
    void enqueue(T x);
    /**dequeue abstract method.
     * @return T*/
    T dequeue();
    /**peek abstract method.
     * @return T*/
    T peek();

    /**
     * Iterator class.
     * @return Iterator<T>
     */
    Iterator<T> iterator();

    /**
     * IsEmpty method.
     * @return boolean
     */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /**
     * isFull method.
     * @return boolean
     */
    default boolean isFull() {
        return fillCount() == capacity();
    }


}
