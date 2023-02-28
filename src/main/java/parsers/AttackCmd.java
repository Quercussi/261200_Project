package parsers;

import entities.*;

import java.util.List;
import java.util.Map;

class AttackCmd implements Statement{
    private final Direction dir;
    private final Expression expr;

    /** Create an attack command
     * @param command must be "shoot".
     * @param dir is not null
     * @param expr is not null
     * @throws SyntaxError if input command is not "shoot"
     */
    AttackCmd(String command, Direction dir, Expression expr) throws SyntaxError {
        if(!command.equals("shoot"))
            throw new SyntaxError("Unknown command: " + command, null);
        this.dir = dir;
        this.expr = expr;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory, List<Alteration> alterations) {
        Tile target = territory.getTileAt(crew.getPositionAt(dir));

        long expenditure = expr.evaluate(bindings, crew, territory);
        int cost = 1;
        long budget = crew.getBudget();

        // no-op
        if(target == null || budget < expenditure + cost)
            return;

        // all correct
        crew.withdraw(expenditure + cost);
        double maximumExpenditure = target.getDeposit();
        target.withdraw(Math.min(expenditure, maximumExpenditure));

        target.updateOwnership(target.getOwner());
        alterations.add(new Alteration(crew, target, Alteration.ActionType.shoot));
    }
}
