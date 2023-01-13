package parsers;

import java.util.Map;

public interface Expression extends Node{
    public int evaluate(Map<String,Integer> bindings);
}