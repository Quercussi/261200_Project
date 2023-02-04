package parsers;

import java.util.Map;

public interface Expression extends Node{
    /** Evaluate the expression
     * @param bindings is a map from identifiers to integers.
     * @throws SyntaxError if the expression cannot be evaluated.
     */
    int evaluate(Map<String,Integer> bindings) throws SyntaxError;
}