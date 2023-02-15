package parsers;

import java.util.Map;

public class MoveCmd implements Statement{
    private final Direction dir;

    /** Create a move command
     * @param command must be "move"
     * @param dir is not null
     * @throws SyntaxError if the input command is not "move"
     */
    public MoveCmd(String command, Direction dir) throws SyntaxError {
        if(!command.equals("move"))
            throw new SyntaxError("Unknown command: " + command, null);
        this.dir = dir;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {

    }
}
