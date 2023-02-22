package parsers;

import entities.CityCrew;
import entities.Territory;
import entities.Tile;

import java.util.Map;

public class RegionCmd implements Statement{
    private enum RegionCmdOperation {invest,collect}

    private final Expression expr;
    private final RegionCmdOperation command;

    /** Create a region command
     * @param command is either "invest" or "collect"
     * @param expr is not null
     * @throws SyntaxError if command is neither "invest" nor "collect".
     */
    public RegionCmd(String command, Expression expr) throws SyntaxError {
        try { this.command = RegionCmdOperation.valueOf(command); }
        catch (IllegalArgumentException | NullPointerException e) {
            throw new SyntaxError("Unknown command: " + command, null);
        }
        this.expr = expr;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) throws SyntaxError {
        long budget = crew.getBudget();
        long expenditure = expr.evaluate(bindings, crew, territory);
        Tile tile = territory.getTileAt(crew);
        long tileDeposit = (long)tile.getDeposit();
        int cost = 1;

        switch (command) {
            case collect -> {
                // end turn
                if(budget < cost) {
                    bindings.put("done",1L);
                    return;
                }

                crew.withdraw(cost);

                // no-op
                if(expenditure > tileDeposit || tile.getOwner() != crew)
                    return;

                // all correct
                tile.withdraw(expenditure);
                crew.deposit(expenditure);
            }

            case invest -> {
                crew.withdraw(cost);

                // no-op
                if(budget < cost + expenditure || !(tile.getOwner() == crew || hasOwnedAdjacentTile(crew,territory)) )
                    return;

                // all correct
                crew.withdraw(expenditure);
                double maxDeposit = territory.getMaxDeposit() - expenditure;
                tile.deposit(Math.min(expenditure, maxDeposit));
            }
        }

        tile.updateOwnership(crew);
    }

    private boolean hasOwnedAdjacentTile(CityCrew crew, Territory territory) {
        for(Direction dir : Direction.values()) {
            Tile adjacentTile = territory.getTileAt(crew.getPositionAt(dir));
            if(adjacentTile != null && adjacentTile.getOwner() == crew)
                return true;
        }

        return false;
    }
}
