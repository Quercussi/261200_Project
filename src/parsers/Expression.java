package parsers;

import java.util.Map;

public interface Expression extends Node{
    int evaluate(Map<String,Integer> bindings) throws SyntaxError;
}