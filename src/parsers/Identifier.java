package parsers;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Identifier implements Expression{
    private final String name;

    /** Create an identifier evaluator
     * @param name is name of identifier
     * @throws SyntaxError if input name is illegal identifier name
     */
    public Identifier(String name) throws SyntaxError {
        if(!isLegalName(name))
            throw new SyntaxError("Illegal identifier name : " + name, null);

        this.name = name;
    }

    public int evaluate(Map<String, Integer> bindings) throws SyntaxError {
        if(bindings.containsKey(name)) {
            if (bindings.get(name) < 0) {
                throw new SyntaxError("Value must be Non-negative Number : " + name, null);
            }
            return bindings.get(name);
        } else
            throw new SyntaxError("Identifier hasn't been assigned: " + name, null);
        }

        private static final Pattern pattern = Pattern.compile("[a-zA-Z_$][\\w$]*");
        private static Matcher matcher;

        /** Check if the input name is legal for an identifier
         * @param name is the name of an identifier
         * @return true if the input name can be an identifier name
         */
        public static boolean isLegalName(String name){
            matcher = pattern.matcher(name);

            boolean isEmpty = name.isEmpty();
            boolean isReserved = reservedWords.contains(name);
            boolean illegalGeneral = !matcher.matches();
            return !(isEmpty || isReserved || illegalGeneral);
        }

        /** Check if the input character can be used in an identifier name
         * @param ch is checking character
         * @return true if the input character can be used in an identifier name
         */
        public static boolean isIdentifierChar(char ch) {
            return Character.isLetterOrDigit(ch) || ch == '_' || ch == '$';
        }
        // We allow underscore ('_') and dollar sign ('$') for naming identifier.

        /** Check if the input name is the name of a Special Variable
         * @param name is the name of an identifier
         * @return true if the input name is the name of a Special Variable
         */
        public static boolean isSpecialVariable(String name){
            return name.equals("rows") || name.equals("cols") ||
                    name.equals("currow") || name.equals("curcol") ||
                    name.equals("budget") || name.equals("deposit") ||
                    name.equals("int") || name.equals("maxdeposit") ||
                    name.equals("random");
        }

        public static Set<String> reservedWords = Set.of("collect","done","down",
                "downleft","downright","else","if","invest","move","nearby","opponent",
                "relocate","shoot","then","up","upleft","upright","while");
    }