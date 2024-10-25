package tasks;

import interfaces.Task;
import java.util.List;
import interfaces.Result;
import java.util.ArrayList;

public class FibonacciTask implements Task {
    private int n;

    public FibonacciTask(int n) {
        this.n = n;
    }

    @Override
    public Result execute() {
        List<Integer> fiboSequence = calculate(n);
        return new Result(fiboSequence);
    }

    private List<Integer> calculate(int n) {
        List<Integer> sequence = new ArrayList<>();

        if (n <= 0)
            return sequence;

        sequence.add(0);
        if (n > 1)
            sequence.add(1);

        for (int i = 2; i < n; i++) {
            int nextFibo = sequence.get(i - 1) + sequence.get(i - 2);
            sequence.add(nextFibo);
        }

        return sequence;
    }

    public int getParam() {
        return this.n;
    }
}
