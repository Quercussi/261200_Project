package parsers;

import entities.*;

import java.util.Map;

class IntLit implements Expression {
    private final long val;

    public IntLit(long val) { this.val = val; }

    public long evaluate(Map<String, Long> bindings, CityCrew crew, Territory territory) { return val; }

    /** Return true if s is an integer
     *
     * @param s is a String
     * @return true if input s can be parsed to integer
     */
    public static boolean isNumber(String s){
        try{
            Integer.parseInt(s)  ;
            return true ;
        }catch (NumberFormatException e){
            return false ;
        }
    }
}
