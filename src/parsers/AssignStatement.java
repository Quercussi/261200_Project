package parsers;

import java.util.Map;

public class AssignStatement implements Statement{
    private final String name;
    private final Expression expr;

    public AssignStatement(String name, Expression expr) throws SyntaxError {
        if(!Identifier.isLegalName(name))
            throw new SyntaxError("Illegal identifier name : " + name);

        this.name = name;
        this.expr = expr;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {
        if(Identifier.isSpecialVariable(name))
            return;

        bindings.put(name, expr.evaluate(bindings));
    }
}
