package parsers;

import java.util.Map;

public class MoveCmd implements Statement{
    private final Direction dir;

    public MoveCmd(String command, Direction dir) throws SyntaxError {
        if(!command.equals("move"))
            throw new SyntaxError("Unknown command: " + command);
        this.dir = dir;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {

    }
}
