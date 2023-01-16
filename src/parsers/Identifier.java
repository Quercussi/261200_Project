package parsers;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Identifier implements Expression{
    private final String name;

    public Identifier(String name) throws SyntaxError {
        if(!isLegalName(name))
            throw new SyntaxError("Illegal name : " + name);

        this.name = name;
    }

    public int evaluate(Map<String, Integer> bindings) throws SyntaxError {
        if(bindings.containsKey(name))
            return bindings.get(name);
        else
            throw new SyntaxError("Identifier hasn't been assigned : " + name);
    }

    private static final Pattern pattern = Pattern.compile("[a-zA-Z_$][\\w$]*");
    private static Matcher matcher;
    public static boolean isLegalName(String name){
        matcher = pattern.matcher(name);

        boolean isEmpty = name.isEmpty();
        boolean isReserved = reservedWords.contains(name);
        boolean illegalGeneral = matcher.matches();
        return !(isEmpty || isReserved || illegalGeneral);
    }

    public static boolean isIdentifierChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_' || ch == '$';
    }

    public static Set<String> reservedWords = Set.of("collect","done","down",
            "downleft","downright","else","if","invest","move","nearby","opponent",
            "relocate","shoot","then","up","upleft","upright","while");
}
