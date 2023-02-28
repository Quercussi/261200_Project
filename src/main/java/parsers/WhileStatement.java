package parsers;

import entities.*;

import java.util.List;
import java.util.Map;

class WhileStatement implements Statement{

    private final Expression expression ;
    private final Statement statement ;
    public WhileStatement(Expression expression,Statement statement){
        this.expression = expression ;
        this.statement = statement ;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory, List<Alteration> alterations) {
        int count = 0;
        while (expression.evaluate(bindings, crew, territory) > 0){
            Long isDone = bindings.get("done");
            if(isDone == null)
                bindings.put("done",0L);
            if(isDone != 0)
                return;

            statement.execute(bindings, crew, territory, alterations);
            if(++count >= 10000)
                break;
        }
    }
}
