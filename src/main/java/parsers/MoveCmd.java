package parsers;

import entities.*;

import java.util.List;
import java.util.Map;

class MoveCmd implements Statement{
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

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) {
        long budget = crew.getBudget();
        int cost = 1;
        crew.withdraw(cost);

        // end turn
        if(budget < cost) {
            bindings.put("done", 1L);
            return;
        }

        // no-op: tile is out of bound, tile belongs to another owner
        Position newPos = crew.getPositionAt(dir);
        Tile tileOverThere = territory.getTileAt(newPos);
        if(tileOverThere == null)
            return;

        // no-op
        CityCrew tileOverThereOwner = tileOverThere.getOwner();
        if(tileOverThereOwner != crew && tileOverThereOwner != null)
            return;

        crew.moveTo(tileOverThere);
    }

    public boolean equals(Node node) {
        if (!this.getClass().getName().equals(node.getClass().getName()))
            return false;

        MoveCmd cmpCmd = (MoveCmd) node;
        return (this.dir == cmpCmd.dir);
    }
}
