package parsers;

import java.util.LinkedList;
import java.util.Map;

public class BlockStatement implements Statement{
    private LinkedList<Statement> list ;

    public BlockStatement(LinkedList<Statement> list){
        this.list = list ;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {

    }

}
