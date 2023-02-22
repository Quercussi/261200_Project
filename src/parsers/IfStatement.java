package parsers;

import entities.CityCrew;
import entities.Territory;

import java.util.Map;

public class IfStatement implements Statement{

    private final Statement ifstatement,elsestatement ;
    private final Expression expression ;
    public IfStatement(Expression expression,Statement ifstatement,Statement elsestatement){
        this.expression = expression ;
        this.ifstatement = ifstatement ;
        this.elsestatement = elsestatement ;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) throws SyntaxError {
        if(expression.evaluate(bindings, crew, territory) > 0){
            ifstatement.execute(bindings, crew, territory);
        }else{
            elsestatement.execute(bindings, crew, territory);
        }
    }
}
