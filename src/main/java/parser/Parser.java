package parser;

import com.ydo4ki.esast.*;
import val.Val;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Parser {
    private final File file;
    private final Iterator<LocatedExpr<? extends Expr>> iterator;

    public Parser(File file, Iterator<LocatedExpr<? extends Expr>> iterator, LocatedExpr<? extends Expr> current) {
        this.file = file;
        this.iterator = iterator;
        this.current = current;
    }

    public Parser(File file) throws IOException {
        this.file = file;
        this.iterator = new ExprOutput(new TokenOutput(file, BracketsTypes.bracketsTypes)).iterator();
    }

    private LocatedExpr<? extends Expr> current;

    private Expr next() {
        return (current = iterator.next()).getExpr();
    }

    public Map<String, Val> parseAll(Map<String, Val> declarations) throws IOException {
        next();
        while (iterator.hasNext()) {
            Expr nameExpr = current.getExpr();
            if (nameExpr.getClass() != Symbol.class)
                throw new EsastException(current.getLocation(), "Symbol expected: " + nameExpr);

            String name = ((Symbol) nameExpr).getValue();

            Expr equalsSign = next();
            if (equalsSign.getClass() != Symbol.class || !((Symbol) equalsSign).getValue().equals("="))
                throw new EsastException(current.getLocation(), "'=' expected: " + equalsSign);

            next();
            Expression expression = parseExpression(false);
//            System.out.println("$ " + expression + " : " + current.getExpr());
            Val value = expression.compile(declarations::get);
            declarations.put(name, value);
        }

        return declarations;
    }

    private Expression parseExpression(boolean requireComma) {
        LocatedExpr<? extends Expr> e = current;

//        while (e instanceof LocatedExprList
//                && ((LocatedExprList) e).size() == 1
//                && ((LocatedExprList) e).getBracketsType() == BracketsTypes.round)
//            e = ((LocatedExprList) e).get(0);

        if (e instanceof LocatedExprList) {
            e = (LocatedExpr<? extends Expr>) (Object) e.split(",", ".").iterator().next();
            LocatedExprList list = (LocatedExprList) e;
            if (list.getBracketsType() != BracketsTypes.round)
                throw new EsastException(current.getLocation(), "Unrecognized brackets type: " + list.getBracketsType());

            Iterator<LocatedExpr<? extends Expr>> iter = list.iterator();
            Parser p = new Parser(null, iter, iter.next());
            Expression r = p.parseExpression(false);
            next();
            return r;
        }

        if (!(e instanceof LocatedSymbol))
            throw new EsastException(current.getLocation(), "Name expected: " + e);

        IdentifierExpression mainValue = new IdentifierExpression((LocatedSymbol) e);

        if (!iterator.hasNext())
            return mainValue; // eof
        Expr next = next();
        if (next instanceof Symbol) {
            if (requireComma) {
                LocatedExpr<? extends Expr> expr = current;
                if (!expr.getExpr().toString().equals(",")) {
                    throw new EsastException(expr.getLocation(), "Comma expected: " + expr.getExpr());
                }
            }
            if (((Symbol) next).getValue().equals("->")) {
                next();
                Expression body = parseExpression(requireComma);

                return new LambdaExpression(mainValue, body);
            } else {
                return mainValue;
            }
        }

        LocatedExprList list = (LocatedExprList) current;
        list = ((LocatedExprList) list.split(",", ".").iterator().next());
        if (list.getBracketsType() != BracketsTypes.round)
            throw new EsastException(current.getLocation(), "Unrecognized brackets type: " + list.getBracketsType());

        List<Expression> args = parseArgs(list);
        if (iterator.hasNext()) // if not then its eof anyway
        {
            next();
            if (requireComma) {
                LocatedExpr<? extends Expr> expr = current;
                if (!expr.getExpr().toString().equals(",")) {
                    throw new EsastException(expr.getLocation(), "Comma expected: " + expr.getExpr());
                }
            }
        }
        return new CallExpression(mainValue, args.toArray(new Expression[0]));
    }

    private static List<Expression> parseArgs(LocatedExprList list) {
        List<Expression> args = new ArrayList<>();
        for (Iterator<LocatedExpr<? extends Expr>> iter = list.iterator(); iter.hasNext(); ) {
            LocatedExpr<? extends Expr> expr = iter.next();
            Parser p = new Parser(null, iter, expr);
            Expression expression = p.parseExpression(true);
            args.add(expression);
//            if (expr instanceof LocatedSymbol) {
//                expression = new IdentifierExpression(((LocatedSymbol) expr));
//            } else {
//                throw new EsastException(expr.getLocation(), "Symbol expected: " + expr);
//
//
//                // OhHhH ThEy WeRe LoW oN tImE
////                LocatedExprList lel = ((LocatedExprList) expr);
////                System.out.println("1: " + lel.getExpr());
////                lel = ((LocatedExprList)lel.split(",").iterator().next());
////                System.out.println("2: " + lel.getExpr());
////                expression = new Parser(null, lel.iterator()).parseExpression();
//            }
        }
        return args;
    }

//    private Val parseValue(Map<String, Val> declarations) {
//        Expr e = next();
//        String name;
//        while (e instanceof ExprList
//                && ((ExprList) e).size() == 1
//                && ((ExprList) e).getBracketsType() == BracketsTypes.round)
//            e = ((ExprList) e).get(0);
//
//        if (e instanceof Symbol) name = ((Symbol) e).getValue();
//        else throw new EsastException(current.getLocation(), "Name expected: " + e);
//
//        Val val = declarations.get(name);
//        if (val == null)
//            throw new EsastException(current.getLocation(), "Cannot find symbol: " + e);
//
//        if (!iterator.hasNext())
//            return val; // eof
//        e = next();
//        if (e instanceof Symbol)
//            return val; // we're done here, this already is the next declaration
//
//        Val argument = parseValue(declarations);
//
//        return val.call(argument);
//    }

    abstract class Expression {
        public abstract Val compile(Function<String, Val> declarations);
    }

    class IdentifierExpression extends Expression {
        final LocatedSymbol symbol;

        IdentifierExpression(LocatedSymbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol.getValue();
        }

        @Override
        public Val compile(Function<String, Val> declarations) {
            String name = symbol.getValue();
            if (name.charAt(0) == '\'' && name.charAt(name.length() - 1) == '\'')
                return Val.symbol(name.substring(1, name.length() - 1));

            Val val = declarations.apply(name);
            if (val == null)
                throw new EsastException(symbol.getLocation(), "Cannot find symbol: " + symbol.getValue());
            return val;
        }
    }

    class ConstExpression extends Expression {
        private final Val val;

        ConstExpression(Val val) {
            this.val = val;
        }

        @Override
        public Val compile(Function<String, Val> declarations) {
            return val;
        }
    }

    class CallExpression extends Expression {
        final Expression function;
        final Expression[] arguments;

        CallExpression(Expression function, Expression[] arguments) {
            this.function = function;
            this.arguments = arguments;
        }

        @Override
        public String toString() {
            return function + "(" + String.join(", ", Arrays.stream(arguments).map(Object::toString).toArray(String[]::new)) + ")" + arguments.length;
        }

        @Override
        public Val compile(Function<String, Val> declarations) {
            Val ret = function.compile(declarations);
            for (Expression argument : arguments) {
                ret = ret.call(argument.compile(declarations));
            }
            return ret;
        }
    }

    class LambdaExpression extends Expression {
        final IdentifierExpression parameter;
        final Expression body;

        LambdaExpression(IdentifierExpression parameter, Expression body) {
            this.parameter = parameter;
            this.body = body;
        }

        @Override
        public Val compile(Function<String, Val> declarations) {
            return toCompilableExpression(declarations).compile(declarations);
//            return compileBody(body, declarations);

//            throw new UnsupportedOperationException(expression.toString());
//            return body.compile(str -> {
//                if (str.equals(parameter.symbol.getValue()))
//                    return Val.mirror;
//
//                Val value = declarations.apply(str);
//                if (value == null)
//                    return null;
//
//                return Val.konst.call(value);
//            });
        }

        private Expression toCompilableExpression(Function<String, Val> declarations) {
            return compileBody(body, declarations);
        }

        private Expression compileBody(Expression expression, Function<String, Val> declarations) {
            if (expression instanceof IdentifierExpression) {
                if (((IdentifierExpression) expression).symbol.getValue().equals(parameter.symbol.getValue()))
                    return new ConstExpression(Val.mirror);

                return new CallExpression(new ConstExpression(Val.konst), new Expression[]{expression});
            } else if (expression instanceof ConstExpression) {
                return new ConstExpression(Val.konst.call(expression.compile(declarations)));
            } else if (expression instanceof CallExpression) {
                Expression ret = compileBody(((CallExpression) expression).function, declarations);
                for (Expression argument : ((CallExpression) expression).arguments) {
                    ret = new CallExpression(
                            new ConstExpression(Val.substitutor),
                            new Expression[]{
                                    ret,
                                    compileBody(argument, declarations)
                            }
                    );
                }
                return ret;
            } else if (expression instanceof LambdaExpression) {
                LambdaExpression l = (LambdaExpression) expression;
                return l.toCompilableExpression(declarations);
            }
            throw new UnsupportedOperationException(expression + "");
        }

    }
}
