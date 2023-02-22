package parsers;

import entities.CityCrew;
import entities.Territory;

import java.util.Map;

public interface Statement extends Node {
    /**
     * Executes the statement
     *
     * @param bindings is a map from identifiers to integers.
     * @param crew is the CityCrew which execute the construction plan.
     * @param territory is the Territory which hosts the game.
     * @throws SyntaxError if the statement cannot be executed.
     */
    void execute(Map<String, Long> bindings, CityCrew crew, Territory territory) throws SyntaxError;
}
