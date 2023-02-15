package parsers;

import java.util.Map;

public class AttackCmd implements Statement{
    private final Direction dir;
    private final Expression expr;

    /** Create an attack command
     * @param command must be "shoot".
     * @param dir is not null
     * @param expr is not null
     * @throws SyntaxError if input command is not "shoot"
     */
    AttackCmd(String command, Direction dir, Expression expr) throws SyntaxError {
        if(!command.equals("shoot"))
            throw new SyntaxError("Unknown command: " + command, null);
        this.dir = dir;
        this.expr = expr;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {

    }
}
