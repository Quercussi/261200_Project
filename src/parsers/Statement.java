package parsers;

import java.util.Map;

public interface Statement extends Node {
    void execute(Map<String,Integer> bindings) throws SyntaxError;
}
