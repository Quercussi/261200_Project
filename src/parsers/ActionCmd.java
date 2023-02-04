package parsers;

import java.util.Map;

public class ActionCmd implements Statement{

    private enum ActionCmdOperation {done,relocate}

    private final ActionCmdOperation command;

    public ActionCmd(String command) throws SyntaxError {
        try { this.command = ActionCmdOperation.valueOf(command); }
        catch (IllegalArgumentException | NullPointerException e) {
            throw new SyntaxError("Unknown command: " + command);
        }
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {
        switch (command){
            case done -> {}
            case relocate -> {}
        }
    }

}
