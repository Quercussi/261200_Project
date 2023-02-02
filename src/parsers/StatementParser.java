package parsers;

import java.util.Map;

public class StatementParser {
    private final Tokenizer tkn;
    private final Statement stm;

    /** Parses statement from the input String
     * @param src is the input grammar
     * @throws SyntaxError if the first token is malformed.
     * */
    public StatementParser(String src) throws SyntaxError {
        tkn = new Tokenizer(src);
        stm = parseStatement();
    }

    /** Executes the statement
     * @param bindings is a map from identifiers to integers.
     * @throws SyntaxError if the statement is illegal.
     */
    public void execute(Map<String,Integer> bindings) throws SyntaxError {
        stm.execute(bindings);
    }

    private Statement parseStatement() throws SyntaxError {
        Statement stm;
        String token = tkn.consume();
        if(token.isEmpty() || !Character.isLetter(token.charAt(0)))
            throw new SyntaxError("Unknown command: " + token + " at line " + tkn.getLine());

        switch (token) {
            case "done","relocate" -> stm = new ActionCmd(token);
            case "move" -> stm = parseMoveCmd(token);
            case "invest","collect" -> stm = parseRegionCmd(token);
            case "shoot" -> stm = parseAttackCmd(token);

            default -> stm = parseAssignStatement(token);
        }

        return stm;
    }

    private Statement parseMoveCmd(String command) throws SyntaxError {
        // MoveCommand
        Direction dir = parseDirection();
        return new MoveCmd(command, dir);
    }

    private Statement parseRegionCmd(String command) throws SyntaxError {
        Expression expr = parseExpression();
        return new RegionCmd(command, expr);
    }

    private Statement parseAttackCmd(String command) throws SyntaxError {
        Direction dir = parseDirection();
        Expression expr = parseExpression();
        return new AttackCmd(command, dir, expr);
    }

    private Statement parseAssignStatement(String identifier) throws SyntaxError {
        tkn.consume("=");
        Expression expr = parseExpression();
        return new AssignStatement(identifier, expr);
    }

    private Expression parseExpression() {
        // TIS YOUR WORK, FRIEND.
        return null;
    }

    private Expression parseInfoExpression(String command) throws SyntaxError {
        Expression expr;
        switch (command) {
            case "opponent" -> expr = new OpponentExpr(command);
            case "nearby" -> expr = new NearbyExpr(command, parseDirection());
            default -> throw new SyntaxError("Unknown command " + command + " at line " + tkn.getLine());
        }
        return expr;
    }

    private Direction parseDirection() throws SyntaxError {
        // This method will check for invalid direction automatically.
        String strDir = tkn.consume();
        return Direction.getDir(strDir);
    }
}
