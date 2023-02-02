package parsers;

import java.util.Map;

public class AttackCmd implements Statement{
    private final Direction dir;
    private final Expression expr;

    AttackCmd(String command, Direction dir, Expression expr) throws SyntaxError {
        if(!command.equals("shoot"))
            throw new SyntaxError("Unknown command: " + command);
        this.dir = dir;
        this.expr = expr;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {

    }
}