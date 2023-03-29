package parsers;

import entities.*;

import java.util.List;
import java.util.Map;

class ActionCmd implements Statement{

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

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) {
        switch (command){
            case relocate -> {
                bindings.put("done", 1L);
                Tile oldCityCenter = crew.getCityCenter();
                Tile newCityCenter = territory.getTileAt(crew);

                int distance = newCityCenter.distanceTo(oldCityCenter);
                long cost = 5L * distance + 10;
                long budget = crew.getBudget();

                // no-op
                if (newCityCenter.getOwner() != crew || budget < cost)
                    return;

                crew.setCityCenter(newCityCenter);
                crew.withdraw(cost);
            }

            case done -> bindings.put("done",1L);
        }
    }

    public boolean equals(Node node) {
        if (!this.getClass().getName().equals(node.getClass().getName()))
            return false;

        ActionCmd cmpCmd = (ActionCmd) node;
        return (this.command == cmpCmd.command);
    }
}
