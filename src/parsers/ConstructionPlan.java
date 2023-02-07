package parsers;

import java.util.Collection;
import java.util.Map;

public class ConstructionPlan implements Statement{
    private final Collection<Statement> stmList;

    /** Create a construction plan statement(s)
     * @param stmList does not include null
     */
    public ConstructionPlan(Collection<Statement> stmList) {
        this.stmList = stmList;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {
        for(Statement stm : stmList){
            stm.execute(bindings);
        }
    }
}
