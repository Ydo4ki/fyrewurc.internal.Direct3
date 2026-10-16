package org.fw.direct3.parser;

import org.fw.direct3.val.Val;

import java.util.function.Function;

class LambdaExpression extends Expression {
    final IdentifierExpression parameter;
    final Expression body;

    LambdaExpression(IdentifierExpression parameter, Expression body) {
        this.parameter = parameter;
        this.body = body;
    }

    @Override
    public Val compile(Function<String, Val> declarations) {
        return toSKI().compile(declarations);
    }

    private Expression toSKI() {
        return abstractOver(parameter, body);
    }

    private Expression abstractOver(IdentifierExpression parameter, Expression expression) {
        if (expression instanceof IdentifierExpression || expression instanceof ConstExpression) {
            if (expression instanceof IdentifierExpression && ((IdentifierExpression) expression).symbol.getText().equals(parameter.symbol.getText())) {
                return new ConstExpression(Val.mirror);
            }

            Expression function = new ConstExpression(Val.konst);
            return new CallExpression(function, expression);
        }

        if (expression instanceof CallExpression) {
            CallExpression call = (CallExpression) expression;
            Expression a = abstractOver(parameter, call.function);
            Expression b = abstractOver(parameter, call.argument);

            Expression function = new ConstExpression(Val.substitutor);
            Expression function1 = new CallExpression(function, a);
            return new CallExpression(function1, b);
        }

        if (expression instanceof LambdaExpression) {
            LambdaExpression lambda = (LambdaExpression) expression;
            Expression inner = lambda.toSKI();

            return abstractOver(parameter, inner);
        }

        throw new UnsupportedOperationException("Unknown Expression implementation: " + expression.getClass());
    }

}
