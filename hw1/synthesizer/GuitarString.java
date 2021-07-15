package synthesizer;

import java.util.HashSet;
import java.util.Set;
//Make sure this class is public
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor
//    private static final double DECAY = 1.; // no decay
    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(capacity);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        //       Make sure that your random numbers are different from each other.
        for (int i = 0; i < buffer.fillCount(); i++) {
            buffer.dequeue();
        }
        Set<Double> occurred = new HashSet<>();
        while (!buffer.isFull()) {
            double r = Math.random() - 0.5;
            if (!occurred.contains(r)) {
                buffer.enqueue(r);
                occurred.add(r);
            }
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double avgNote = (buffer.dequeue() + buffer.peek()) / 2 * DECAY;
        if (Math.random() > 0.5) {
            buffer.enqueue(-avgNote);
        }
        else {
            buffer.enqueue(avgNote);
        }
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }

}
