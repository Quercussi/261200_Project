package parsers;

import entities.CityCrew;
import entities.Territory;

import java.util.Map;

public class WhileStatement implements Statement{

    private final Expression expression ;
    private final Statement statement ;
    public WhileStatement(Expression expression,Statement statement){
        this.expression = expression ;
        this.statement = statement ;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) throws SyntaxError{
        int count = 0;
        while (expression.evaluate(bindings, crew, territory) > 0){
            Long isDone = bindings.get("done");
            if(isDone == null)
                throw new SyntaxError("Missing bindings", null);
            if(isDone != 0)
                return;

            statement.execute(bindings, crew, territory);
            if(++count >= 10000)
                break;
        }
    }
}
