package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
    }

//    @Test
//    public void testPeek() {
//        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<>(10);
//        arb.enqueue(10.0);
////        for (int i = 0; i < 10; i++) {
////            arb.enqueue(i * 1.0);
////        }
////        for (int i = 0; i < 10; i++) {
////            assertEquals((arb.peek() * 1.0), (i * 1.0));
////        }
//        assertEquals(arb.peek(), Double.valueOf(10.0));
//    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
