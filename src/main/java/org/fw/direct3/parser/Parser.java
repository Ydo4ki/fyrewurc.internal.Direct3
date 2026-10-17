package org.fw.direct3.parser;

import com.ydo4ki.esast.*;
import org.fw.direct3.val.Val;

import java.util.*;

public class Parser {
    private final TokenOutput toks;

    public Parser(TokenOutput toks) {
        this.toks = toks;
        iter = toks.iterator();
    }

    private Iterator<Token> iter;

    private Token current;
    private Token next() {
        do {
            current = iter.next();
        } while (current.getType() == TokenType.COMMENT);
        return current;
    }

    public void parseAll(Map<String, Val> initialDeclarations) {
        Token tok = next();
        while (iter.hasNext()) {
            typeCheck(tok, TokenType.IDENTIFIER);
            String name = tok.getText();
            tok = next();
            if (!tok.getText().equals("="))
                throw new UnexpectedTokenEsastException(tok, "'=' expected");

            next();
            Expression expr = parseExpression();
            initialDeclarations.put(name, Objects.requireNonNull(expr.compile(initialDeclarations::get)));
            tok = current;
        }
    }

    private Expression parseExpression() {
        if (current.getType() == TokenType.OPEN) {
            BracketsType bt = toks.getBracketsTypes().byOpen(current.getText().charAt(0));
            next();
            Expression e = parseExpression();
            if (current.getType() != TokenType.CLOSE || !(current.getText().equals(String.valueOf(bt.close()))))
                throw new UnexpectedTokenEsastException(current, bt.close() + " expected");
            next();
            return e;
        }
        IdentifierExpression identifier = new IdentifierExpression(current);
        next();
        if (current.getText().equals("->")) {
            next();
            Expression body = parseExpression();
            return new LambdaExpression(identifier, body);
        }
        Expression result = identifier;
        while (true) {
            if (current.getType() == TokenType.OPEN) {
                BracketsType bracketsType = toks.getBracketsTypes().byOpen(current.getText().charAt(0));
                next();
                List<Expression> args = parseArgs(bracketsType);
                for (Expression arg : args) {
                    result = new CallExpression(result, arg);
                }
            } else if (current.getText().equals(".")) {
                next();
                ConstExpression sym = new ConstExpression(Val.symbol(current.getText()));
                result = new CallExpression(result, sym);
                next();
            } else {
                break;
            }
        }
        return result;
    }

    private List<Expression> parseArgs(BracketsType bracketsType) {
        List<Expression> expressions = new ArrayList<>();
        if (current.getType() == TokenType.CLOSE)
            return expressions;
        while (true) {
            Expression expression = parseExpression();
            expressions.add(expression);
            if (current.getType() != TokenType.CLOSE && !current.getText().equals(","))
                throw new UnexpectedTokenEsastException(current, "',' expected");

            if (current.getType() == TokenType.CLOSE) {
                if (!current.getText().equals(String.valueOf(bracketsType.close())))
                    throw new UnexpectedTokenEsastException(current, "'" + bracketsType.close() + "' expected");
                next();
                return expressions;
            }

            next();
        }
    }

    private void typeCheck(Token tok, TokenType tokenType) {
        if (tok.getType() != tokenType)
            throw new UnexpectedTokenEsastException(tok, tokenType + " expected");
    }
}
