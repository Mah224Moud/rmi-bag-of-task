package interfaces;

import java.io.Serializable;
import java.util.List;;

public class Result implements Serializable {
    private List<Integer> fiboSequence;

    public Result(List<Integer> fiboSequence) {
        this.fiboSequence = fiboSequence;
    }

    /**
     * Retrieves the Fibonacci sequence stored in this result.
     * 
     * @return a list of integers representing the Fibonacci sequence
     */
    public List<Integer> getFibonacciSequence() {
        return fiboSequence;
    }

    /**
     * Converts the result to a string representation.
     * 
     * @return a string representing the result, of the form "Result: [0, 1, 1, 2,
     *         ...]"
     */
    @Override
    public String toString() {
        return "Result: " + fiboSequence;
    }
}
