package parsers;

import entities.CityCrew;
import entities.Territory;

import java.util.Map;

public class AssignStatement implements Statement{
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

    public void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) throws SyntaxError {
        if(Identifier.isSpecialVariable(name) || Identifier.reservedWords.contains(name))
            return;

        bindings.put(name, expr.evaluate(bindings, crew, territory));
    }
}
