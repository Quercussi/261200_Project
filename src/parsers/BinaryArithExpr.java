package parsers;

import java.util.Map;

public class BinaryArithExpr implements Expression {
    private final Expression left;
    private final Expression right;
    private final String operator;

    public BinaryArithExpr(Expression left, String operator, Expression right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }


    public int evaluate(Map<String, Integer> bindings) throws SyntaxError{
        int left_val = left.evaluate(bindings);
        int right_val = right.evaluate(bindings);
        switch (operator) {
            case "+" -> { return left_val + right_val ;}
            case "-" -> { return left_val - right_val; }
            case "*" -> { return left_val * right_val; }
            case "/" -> { return left_val / right_val; }
            case "%" -> { return left_val % right_val; }
            case "^" -> { return (int) Math.pow(left_val,right_val);}
            default -> throw new SyntaxError("unknown operator: " + operator);
        }
    }
}
