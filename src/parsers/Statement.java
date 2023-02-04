package parsers;

import java.util.Map;

public interface Statement extends Node {
    /** Executes the statement
     * @param bindings is a map from identifiers to integers.
     * @throws SyntaxError if the statement is illegal.
     */
    void execute(Map<String,Integer> bindings) throws SyntaxError;
}
