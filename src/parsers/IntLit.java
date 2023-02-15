package parsers;

import java.util.Map;

public class IntLit implements Expression {
    private final int val;

    public IntLit(int val) { this.val = val; }

    public int evaluate(Map<String, Integer> bindings) { return val; }

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
