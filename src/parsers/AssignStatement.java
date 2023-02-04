package parsers;

import java.util.Map;

public class AssignStatement implements Statement{
    private final String name;
    private final Expression expr;

    public AssignStatement(String name, Expression expr) {
        this.name = name;
        this.expr = expr;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {
        bindings.put(name, expr.evaluate(bindings));
    }
}
