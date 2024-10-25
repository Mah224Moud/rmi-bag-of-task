package interfaces;

import java.io.Serializable;
import java.util.List;;

public class Result implements Serializable {
    private List<Integer> fiboSequence;

    public Result(List<Integer> fiboSequence) {
        this.fiboSequence = fiboSequence;
    }

    public List<Integer> getFibonacciSequence() {
        return fiboSequence;
    }

    @Override
    public String toString() {
        return "Result: " + fiboSequence;
    }
}
