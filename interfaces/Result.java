package interfaces;

import java.io.Serializable;

public class Result implements Serializable {
    private int result;

    public Result(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Result: " + result;
    }
}
