package parsers;

import entities.CityCrew;
import entities.Territory;

import java.util.Collection;
import java.util.Map;

public class BlockStatement implements Statement{
    private final Collection<Statement> list ;

    public BlockStatement(Collection<Statement> list){
        this.list = list ;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) throws SyntaxError {
        for(Statement statement : list){
            Long isDone = bindings.get("done");
            if(isDone == null)
                throw new SyntaxError("Missing bindings", null);
            if(isDone != 0)
                return;

            statement.execute(bindings, crew, territory);
        }
    }

}
