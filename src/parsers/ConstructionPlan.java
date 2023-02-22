package parsers;

import entities.CityCrew;
import entities.Territory;

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

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) throws SyntaxError {
        for(Statement stm : stmList){
            Long isDone = bindings.get("done");
            if(isDone == null)
                throw new SyntaxError("Missing bindings", null);
            if(isDone != 0)
                return;
            
            stm.execute(bindings, crew, territory);
        }
    }
}
