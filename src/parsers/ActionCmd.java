package parsers;

import java.util.Map;

public class ActionCmd implements Statement{

    private enum ActionCmdOperation {done,relocate}

    private final ActionCmdOperation command;

    /** Create an action command
     * @param command is either "done" or "relocate"
     * @throws SyntaxError if command is neither "done" nor "relocate"
     */
    public ActionCmd(String command) throws SyntaxError {
        try { this.command = ActionCmdOperation.valueOf(command); }
        catch (IllegalArgumentException | NullPointerException e) {
            throw new SyntaxError("Unknown command: " + command, null);
        }
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {
        switch (command){
            case done -> {}
            case relocate -> {}
        }
    }

}
