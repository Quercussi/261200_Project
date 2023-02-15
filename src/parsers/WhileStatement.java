package parsers;

import java.util.Map;

public class WhileStatement implements Statement{

    private final Expression expression ;
    private final Statement statement ;
    public WhileStatement(Expression expression,Statement statement){
        this.expression = expression ;
        this.statement = statement ;
    }

    public void execute(Map<String,Integer> bindings) throws SyntaxError{
        int count = 0;
        while (expression.evaluate(bindings) > 0){
            statement.execute(bindings);
            if(++count >= 10000) throw new RuntimeException();
        }
    }
}
