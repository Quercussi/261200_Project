package parsers;

import entities.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

class ConstructionPlan implements Statement{
    private final Collection<Statement> stmList;

    /** Create a construction plan statement(s)
     * @param stmList does not include null
     */
    public ConstructionPlan(Collection<Statement> stmList) {
        this.stmList = stmList;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory, List<Alteration> alterations) {
        for(Statement stm : stmList){
            Long isDone = bindings.get("done");
            if(isDone == null)
                bindings.put("done",0L);
            if(isDone != 0)
                return;
            
            stm.execute(bindings, crew, territory, alterations);
        }
    }
}
