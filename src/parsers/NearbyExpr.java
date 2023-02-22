package parsers;

import entities.CityCrew;
import entities.Territory;
import entities.Tile;

import java.util.Map;

public class NearbyExpr implements Expression{
    private final Direction dir;

    /** Create an evaluator for nearby command
     * @param command must be "nearby"
     * @param dir is not null
     * @throws SyntaxError if the input command is not "nearby"
     */
    public NearbyExpr(String command, Direction dir) throws SyntaxError {
        if(!command.equals("nearby"))
            throw new SyntaxError("Unknown command: " + command, null);
        this.dir = dir;
    }

    public long evaluate(Map<String, Long> bindings, CityCrew crew, Territory territory) throws SyntaxError {
        Tile currTile = territory.getTileAt(crew);
        int distance = 0;
        while(currTile != null) {
            CityCrew tileOwner = currTile.getOwner();

            if (tileOwner != null && tileOwner != crew) {
                int depositDigits = (int)Math.log10(currTile.getDeposit()) + 1;
                return distance * 100L + depositDigits;
            }

            currTile = territory.getTileAt(currTile.getPositionAt(dir));
            distance++;
        }
        return 0;
    }
}
