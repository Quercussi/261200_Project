package parsers;

import java.util.Map;

public class IfStatement implements Statement{

    private final Statement ifstatement,elsestatement ;
    private final Expression expression ;
    public IfStatement(Expression expression,Statement ifstatement,Statement elsestatement){
        this.expression = expression ;
        this.ifstatement = ifstatement ;
        this.elsestatement = elsestatement ;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {
        if(expression.evaluate(bindings) > 0){
            ifstatement.execute(bindings);
        }else{
            elsestatement.execute(bindings);
        }
    }
}
