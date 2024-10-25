package tasks;

import interfaces.Task;

import java.io.Serializable;

import interfaces.Result;

public class FibonacciTask implements Task {
    private int n;

    public FibonacciTask(int n) {
        this.n = n;
    }

    @Override
    public Result execute() {
        int fibo = calculate(n);
        return new Result(fibo);
    }

    private int calculate(int n) {
        if (n <= 1)
            return n;
        return calculate(n - 1) + calculate(n - 2);
    }
}
