package parsers;

import entities.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
        stm = parseConstructionPlan();
    }

    /** Executes the statement
     * @param crew is the CityCrew which execute the construction plan.
     * @param territory is Territory which hosts the game.
     * @throws SyntaxError if the statement is illegal.
     */
    public void execute(Map<String,Long> bindings, CityCrew crew, Territory territory, List<Alteration> alterations) {
        if(bindings == null)
            bindings = new HashMap<>();

        bindings.put("done",0L);
        stm.execute(bindings, crew, territory, alterations);
    }

    private Statement parseConstructionPlan() throws SyntaxError {
        LinkedList<Statement> stmList = new LinkedList<>();
        do stmList.add(parseStatement());
        while (tkn.hasNextToken());

        return new ConstructionPlan(stmList);
    }

    private Statement parseStatement() throws SyntaxError {
        Statement stm;
        String token = tkn.consume();
        char beginChar = token.charAt(0);
        if(!(Character.isLetter(beginChar) || Tokenizer.isOperator(beginChar) || beginChar == '_' || beginChar == '$'))
            throw new SyntaxError("Unknown command: " + token, tkn.getLine());

        switch (token) {
            case "done","relocate" -> stm = new ActionCmd(token);
            case "move" -> stm = parseMoveCmd(token);
            case "invest","collect" -> stm = parseRegionCmd(token);
            case "shoot" -> stm = parseAttackCmd(token);
            case "{" -> stm = parseBlockStatement() ;
            case "if" -> stm = parseIfStatement() ;
            case "while" -> stm = parseWhileStatement() ;

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
        try { return new AssignStatement(identifier, expr); }
        catch (SyntaxError e) {
            throw new SyntaxError(e.getMessage(), tkn.getLine());
        }
    }

    private Expression parseExpression() throws SyntaxError{
        Expression e =  parseTerm() ;
        while (tkn.peek("+") || tkn.peek("-")){
            String operator = tkn.consume() ;
            switch (operator){
                case "+","-" -> e = new BinaryArithExpr(e,operator,parseTerm()) ;
            }
        }

        return e ;
    }

    private Expression parseTerm() throws SyntaxError{
        Expression e = parseFactor() ;
        while (tkn.peek("*") || tkn.peek("/") || tkn.peek("%")){
            String operator = tkn.consume() ;
            switch (operator){
                case "*","/","%" -> e = new BinaryArithExpr(e,operator,parseFactor()) ;
            }
        }
        return e ;
    }

    private Expression parseFactor() throws SyntaxError{
        Expression e = parsePower() ;
        if (tkn.peek("^")){
            String operator = tkn.consume();
            e = new BinaryArithExpr(e,operator,parseFactor()) ;
        }

        return  e ;
    }

    private Expression parsePower() throws SyntaxError{
        String nextToken = tkn.consume() ;
        if(IntLit.isNumber(nextToken)){
            return new IntLit(Integer.parseInt(nextToken));
        }else if(Identifier.isLegalName(nextToken)){
            return new Identifier(nextToken) ;
        }else if(nextToken.equals("(")) {
            Expression e = parseExpression() ;
            tkn.consume(")") ;
            return e;
        }else if(nextToken.equals("opponent") || nextToken.equals("nearby")){
            return parseInfoExpression(nextToken) ;
        }
        else throw new SyntaxError("Malformed expression: " + nextToken, tkn.getLine());

    }

    private Expression parseInfoExpression(String command) throws SyntaxError {
        Expression expr;
        switch (command) {
            case "opponent" -> expr = new OpponentExpr(command);
            case "nearby" -> expr = new NearbyExpr(command, parseDirection());
            default -> throw new SyntaxError("Unknown command: " + command, tkn.getLine());
        }
        return expr;
    }

    private Direction parseDirection() throws SyntaxError {
        // This method will check for invalid direction automatically.
        String strDir = tkn.consume();
        return Direction.getDir(strDir);
    }

    private BlockStatement parseBlockStatement() throws SyntaxError {
        // token character is now {
        LinkedList<Statement> l = new LinkedList<>() ;
        while(!tkn.peek("}")){
            l.add(parseStatement()) ;
        }
        tkn.consume("}") ;
        return new BlockStatement(l) ;
    }

    private IfStatement parseIfStatement() throws SyntaxError{
        // token character is now if
        tkn.consume("(") ;
        Expression expr = parseExpression() ;
        tkn.consume(")") ;
        tkn.consume("then") ;
        Statement ifstatement = parseStatement() ;
        tkn.consume("else");
        Statement elsestatement = parseStatement() ;

        return new IfStatement(expr,ifstatement,elsestatement) ;
    }
    private WhileStatement parseWhileStatement() throws SyntaxError{
        // token character is now while
        Expression expr = parseExpression() ;
        Statement statement = parseStatement() ;

        return new WhileStatement(expr,statement) ;
    }

    public String getPlan() { return tkn.getPlan(); }
}
