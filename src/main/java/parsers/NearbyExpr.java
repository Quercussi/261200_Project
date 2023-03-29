package parsers;

import entities.*;

import java.util.Map;

class NearbyExpr implements Expression{
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

    public long evaluate(Map<String, Long> bindings, CityCrew crew, Territory territory) {
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

    public boolean equals(Node node) {
        if (!this.getClass().getName().equals(node.getClass().getName()))
            return false;

        NearbyExpr cmpExpr = (NearbyExpr) node;
        return this.dir == cmpExpr.dir;
    }
}
