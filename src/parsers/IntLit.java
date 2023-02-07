package parsers;

import java.util.Map;

public class IntLit implements Expression {
    private int val;

    public IntLit(int val) { this.val = val; }

    public int evaluate(Map<String, Integer> bindings) { return val; }
}
