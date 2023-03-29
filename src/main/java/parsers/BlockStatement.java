package parsers;

import entities.*;

import java.util.List;
import java.util.Map;

class BlockStatement implements Statement{
    private final List<Statement> list ;

    public BlockStatement(List<Statement> list){
        this.list = list ;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) {
        for(Statement statement : list){
            Long isDone = bindings.get("done");
            if(isDone == null)
                bindings.put("done",0L);
            else if(isDone != 0)
                return;

            statement.execute(bindings, crew, territory);
        }
    }

    public boolean equals(Node node) {
        if (!this.getClass().getName().equals(node.getClass().getName()))
            return false;

        BlockStatement cmpStm = (BlockStatement) node;
        if (this.list.size() != cmpStm.list.size())
            return false;

        int size = list.size();
        for(int i = 0; i < size; i++)
            if(!list.get(i).equals(cmpStm.list.get(i)))
                return false;

        return true;
    }
}
