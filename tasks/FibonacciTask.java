package tasks;

import interfaces.Task;
import java.util.List;
import interfaces.Result;
import java.util.ArrayList;

public class FibonacciTask implements Task {
    private int n;
    private String description;

    public FibonacciTask(int n) {
        this.n = n;
        this.description = "Calcul des " + n + " premiers nombres de Fibonacci";
    }

    /**
     * Executes the task and returns the result.
     * 
     * This method computes the Fibonacci sequence for the parameter 'n'
     * and returns the result as a list of integers.
     * 
     * @return a list of integers representing the Fibonacci sequence
     */
    @Override
    public Result execute() {
        List<Integer> fiboSequence = calculate(n);
        return new Result(fiboSequence);
    }

    /**
     * Computes the Fibonacci sequence up to the nth number.
     * 
     * This method takes an integer 'n' as parameter and returns
     * a list of integers representing the Fibonacci sequence
     * up to the nth number. If 'n' is less than or equal to 0,
     * the method returns an empty list.
     * 
     * @param n the number of elements in the Fibonacci sequence
     * @return a list of integers representing the Fibonacci sequence
     */
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

    /**
     * Retrieves the parameter associated with the task.
     * 
     * This method returns the parameter 'n' associated with the Fibonacci
     * task, which is the number of elements in the Fibonacci sequence to
     * be computed.
     * 
     * @return the parameter associated with the task
     */
    public int getParam() {
        return this.n;
    }

    /**
     * Retrieves the description associated with the task.
     * 
     * This method returns the description associated with the Fibonacci
     * task, which is a string that describes the task.
     * 
     * @return the description associated with the task
     */
    public String getDescription() {
        return this.description;
    }
}
