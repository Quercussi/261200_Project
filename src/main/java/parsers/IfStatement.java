package parsers;

import entities.*;

import java.util.List;
import java.util.Map;

class IfStatement implements Statement{

    private final Statement ifstatement,elsestatement ;
    private final Expression expression ;
    public IfStatement(Expression expression,Statement ifstatement,Statement elsestatement){
        this.expression = expression ;
        this.ifstatement = ifstatement ;
        this.elsestatement = elsestatement ;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory, List<Alteration> alterations) {
        if(expression.evaluate(bindings, crew, territory) > 0){
            ifstatement.execute(bindings, crew, territory, alterations);
        }else{
            elsestatement.execute(bindings, crew, territory, alterations);
        }
    }
}
