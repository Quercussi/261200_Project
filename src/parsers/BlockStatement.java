package parsers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class BlockStatement implements Statement{
    private Collection<Statement> list ;

    public BlockStatement(Collection<Statement> list){
        this.list = list ;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {
        for(Statement statement : list){
            statement.execute(bindings);
        }
    }

}
