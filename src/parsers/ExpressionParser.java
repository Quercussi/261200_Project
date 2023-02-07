package parsers;

import java.util.Map;

public class ExpressionParser {
    private Expression expr ;
    private  Tokenizer tkz ;

    private boolean isCalculated = false;
    public ExpressionParser(String src) throws  SyntaxError{
        this.tkz = new Tokenizer(src) ;
    }

    public int eval(Map<String,Integer> bindings) throws SyntaxError {
        if(!isCalculated)
            calculate();
        if(expr.evaluate(bindings) < 0) throw new SyntaxError("Value must be Nonnegative Number");

        return expr.evaluate(bindings);
    }
    private boolean isNumber(String s){
        try{
            Integer.parseInt(s)  ;
            return true ;
        }catch (NumberFormatException e){
            return false ;
        }
    }

    private void calculate() throws SyntaxError {
        expr = parseExpression() ;
        if(tkz.hasNextToken()){
            throw new SyntaxError("leftover token") ;
        }
        isCalculated = true;
    }

    private Expression parseExpression() throws SyntaxError{
        Expression e =  parseTerm() ;
        while (tkz.peek("+") || tkz.peek("-")){
            String operator = tkz.consume() ;
            switch (operator){
                case "+","-" -> e = new BinaryArithExpr(e,operator,parseTerm()) ;
            }
        }

        return e ;
    }

    private Expression parseTerm() throws SyntaxError{
        Expression e = parseFactor() ;
        while (tkz.peek("*") || tkz.peek("/") || tkz.peek("%")){
            String operator = tkz.consume() ;
            switch (operator){
                case "*","/","%" -> e = new BinaryArithExpr(e,operator,parseFactor()) ;
            }
        }
        return e ;
    }

    private Expression parseFactor() throws SyntaxError{
        Expression e = parsePower() ;
        while (tkz.peek("^")){
            String operator = tkz.consume();
            e = new BinaryArithExpr(e,operator,parsePower()) ;
        }
        return  e ;
    }

    private Expression parsePower() throws SyntaxError{
        String nextToken = tkz.consume() ;
        if(isNumber(nextToken)){
            return new IntLit(Integer.parseInt(nextToken));
        }else if(Identifier.isLegalName(nextToken)){
            return new Identifier(nextToken) ;
        }else if(nextToken.equals("(")) {
            Expression e = parseExpression() ;
            tkz.consume(")") ;
            return e;
        }
        else throw new SyntaxError("Malformed expression: " + nextToken);

    }


}
