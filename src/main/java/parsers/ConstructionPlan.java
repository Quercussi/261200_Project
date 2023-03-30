package parsers;

import entities.*;

import java.util.List;
import java.util.Map;

class ConstructionPlan implements Statement{
    private final List<Statement> stmList;

    /** Create a construction plan statement(s)
     * @param stmList does not include null
     */
    public ConstructionPlan(List<Statement> stmList) {
        this.stmList = stmList;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) {
        bindings.put("done",0L);
        for(Statement stm : stmList){
            Long isDone = bindings.get("done");
            if(isDone != 0)
                return;
            
            stm.execute(bindings, crew, territory);
        }
        bindings.remove("done");
    }

    public boolean equals(Node node) {
        if (!this.getClass().getName().equals(node.getClass().getName()))
            return false;

        ConstructionPlan cmpStm = (ConstructionPlan) node;
        if (this.stmList.size() != cmpStm.stmList.size())
            return false;

        int size = stmList.size();
        for(int i = 0; i < size; i++)
            if(!stmList.get(i).equals(cmpStm.stmList.get(i)))
                return false;

        return true;
    }
}
