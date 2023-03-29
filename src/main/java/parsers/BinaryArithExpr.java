package parsers;

import entities.*;

import java.util.Map;

class BinaryArithExpr implements Expression {
    private final Expression left;
    private final Expression right;
    private final String operator;

    public BinaryArithExpr(Expression left, String operator, Expression right) throws SyntaxError {
        this.left = left;
        this.right = right;
        this.operator = operator;
        if(!(operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/") || operator.equals("%") || operator.equals("^")))
            throw new SyntaxError("Unknown operator: " + operator, null);
    }


    public long evaluate(Map<String, Long> bindings, CityCrew crew, Territory territory) {
        long left_val = left.evaluate(bindings, crew, territory);
        long right_val = right.evaluate(bindings, crew, territory);
        switch (operator) {
            case "+" -> { return left_val + right_val ;}
            case "-" -> { return left_val - right_val; }
            case "*" -> { return left_val * right_val; }
            case "/" -> {
                try { return left_val / right_val; }
                catch (ArithmeticException e) { bindings.put("done",1L); return Long.MAX_VALUE; }
            }
            case "%" -> { return left_val % right_val; }
            case "^" -> { return (int) Math.pow(left_val,right_val);}
            default -> {
                bindings.put("done",1L);
                return 0;
            }
        }
    }

    public boolean equals(Node node) {
        if (!this.getClass().getName().equals(node.getClass().getName()))
            return false;

        BinaryArithExpr cmpExpr = (BinaryArithExpr) node;
        return this.left.equals(cmpExpr.left) && this.right.equals(cmpExpr.right) && this.operator.equals(cmpExpr.operator);
    }
}
