package parsers;

import entities.*;

import java.util.Map;

public interface Expression extends Node{
    /**
     * Evaluate the expression
     *
     * @param bindings is a map from identifiers to integers.
     * @param crew is the CityCrew which execute the construction plan.
     * @param territory is the Territory which hosts the game.
     * @throws SyntaxError if the statement cannot be executed.
     */
    long evaluate(Map<String,Long> bindings, CityCrew crew, Territory territory);
}