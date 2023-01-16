package parsers;

import java.util.Map;

public class StatementParser {
    Tokenizer tkn;

    public StatementParser(String src) throws SyntaxError {
        tkn = new Tokenizer(src);
    }

    public void compute(Map<String,Integer> bindings) throws SyntaxError {
        Statement stm = parseStatement();
        stm.execute(bindings);
    }

    private Statement parseStatement() throws SyntaxError {
        Statement stm;
        String token = tkn.consume();
        if(token.isEmpty() || !Character.isLetter(token.charAt(0)))
            throw new SyntaxError("Unknown command: " + token);

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
        Direction dir = parseDirection();
        return new InfoExpression(command, dir);
    }

    private Direction parseDirection() throws SyntaxError {
        // This method will check for exception automatically.
        String strDir = tkn.consume();
        return Direction.getDir(strDir);
    }
}
