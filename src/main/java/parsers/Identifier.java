package parsers;

import entities.*;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Identifier implements Expression{
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

    public long evaluate(Map<String, Long> bindings, CityCrew crew, Territory territory) {
        if (!bindings.containsKey(name) && !isSpecialVariable(name))
            bindings.put(name, 0L);

        switch (name) {
            case "rows" -> { return territory.getRows(); }
            case "cols" -> { return territory.getCols(); }
            case "currow" -> { return crew.getRow(); }
            case "curcol" -> { return crew.getCol(); }
            case "budget" -> { return crew.getBudget(); }
            case "deposit" -> {
                Tile tile = territory.getTileAt(crew);
                return (long) (tile.getOwner() == null ? -tile.getDeposit() : tile.getDeposit());
            }
            case "int" -> {
                Tile tile = territory.getTileAt(crew);
                return (long) territory.getInterestRate(tile);
            }
            case "maxdeposit" -> { return (long) territory.getMaxDeposit(); }
            case "random" -> { return rand.nextInt(1,1000); }
            default -> { return bindings.get(name); }
        }
    }

        private static final Pattern pattern = Pattern.compile("[a-zA-Z_$][\\w$]*");

    /** Check if the input name is legal for an identifier
         * @param name is the name of an identifier
         * @return true if the input name can be an identifier name
         */
        public static boolean isLegalName(String name){
            Matcher matcher = pattern.matcher(name);

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

        private static final Random rand = new Random();
    }