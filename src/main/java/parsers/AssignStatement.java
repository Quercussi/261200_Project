package parsers;

import entities.*;

import java.util.List;
import java.util.Map;

class AssignStatement implements Statement{
    private final String name;
    private final Expression expr;

    /** Create an assign statement
     * @param name is the name of an identifier
     * @param expr is not null
     * @throws SyntaxError if input name is an illegal identifier name
     */
    public AssignStatement(String name, Expression expr) throws SyntaxError {
        if(!Identifier.isLegalName(name))
            throw new SyntaxError("Illegal identifier name : " + name, null);

        this.name = name;
        this.expr = expr;
    }

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) {
        if(Identifier.isSpecialVariable(name) || Identifier.reservedWords.contains(name))
            return;

        bindings.put(name, expr.evaluate(bindings, crew, territory));
    }

    public boolean equals(Node node) {
        if (!this.getClass().getName().equals(node.getClass().getName()))
            return false;

        AssignStatement cmpCmd = (AssignStatement) node;
        return (this.name.equals(cmpCmd.name) && this.expr.equals(cmpCmd.expr));
    }
}
