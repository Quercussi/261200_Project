package parsers;

import entities.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

class BlockStatement implements Statement{
    private final Collection<Statement> list ;

    public BlockStatement(Collection<Statement> list){
        this.list = list ;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory, List<Alteration> alterations) {
        for(Statement statement : list){
            Long isDone = bindings.get("done");
            if(isDone == null)
                bindings.put("done",0L);
            if(isDone != 0)
                return;

            statement.execute(bindings, crew, territory, alterations);
        }
    }

}
