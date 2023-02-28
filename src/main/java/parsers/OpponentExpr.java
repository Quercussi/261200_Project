package parsers;

import entities.Tile;

import java.util.Arrays;
import java.util.Map;

import entities.*;
import static parsers.Direction.*;

class OpponentExpr implements Expression {

    /** Create an evaluator for opponent command
     * @param command must be "opponent"
     * @throws SyntaxError if the input command is not "opponent"
     */
    public OpponentExpr(String command) throws SyntaxError {
        if(!command.equals("opponent"))
            throw new SyntaxError("Unknown command: " + command, null);
    }
    public long evaluate(Map<String, Long> bindings, CityCrew crew, Territory territory) {
        Tile[] currTile = new Tile[6];
        Arrays.fill(currTile, territory.getTileAt(crew));

        long maxIteration = Math.max(territory.getRows(),territory.getCols());
        int distance = 0;
        for(int k = 0; k < maxIteration; k++) {
            distance++;
            for(int i = 0; i < directions.length; i++) {
                if(currTile[i] == null) continue;

                currTile[i] = territory.getTileAt(currTile[i].getPositionAt(directions[i]));
                if(currTile[i] != null && currTile[i].getOwner() != null && currTile[i].getOwner() != crew)
                    return distance * 10L + i+1;
            }
        }
        return 0;
    }

    static private final Direction[] directions = {up, upleft, downleft, down, downright, upright};
}
