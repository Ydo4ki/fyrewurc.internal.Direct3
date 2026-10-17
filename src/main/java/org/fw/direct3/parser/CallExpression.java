package org.fw.direct3.parser;

import org.fw.direct3.val.Val;

import java.util.Objects;
import java.util.function.Function;

class CallExpression extends Expression {
    final Expression function;
    final Expression argument;

    CallExpression(Expression function, Expression argument) {
        this.function = function.simplify();
        this.argument = argument.simplify();
    }

    @Override
    public String toString() {
        return function + "(" + argument + ")";
    }

    @Override
    public Val compile(Function<String, Val> declarations) {
        Val ret = function.compile(declarations);
        ret = ret.call(argument.compile(declarations));
        return ret;
    }

    @Override
    protected Expression simplify() {
//        if (argument instanceof ConstExpression && ((ConstExpression) argument).val == Val.mirror) {
//            if (function instanceof CallExpression) {
//                CallExpression call = (CallExpression) function;
//                if (call.function instanceof ConstExpression && ((ConstExpression) call.function).val == Val.substitutor) {
//                    if (call.argument instanceof CallExpression
//                            && ((CallExpression) call.argument).function instanceof ConstExpression
//                            && ((ConstExpression) ((CallExpression) call.argument).function).val == Val.konst)
//                        return ((CallExpression) call.argument).argument;
//                }
//            }
//        } else
        if (argument instanceof CallExpression) {
            if (function instanceof CallExpression) {
                if (((CallExpression) function).function.equals(new ConstExpression(Val.substitutor))) {
                    if (((CallExpression) function).argument instanceof CallExpression && ((CallExpression) ((CallExpression) function).argument).function.equals(new ConstExpression(Val.konst))) {
                        Expression x = ((CallExpression) ((CallExpression) function).argument).argument;
                        if (((CallExpression) argument).function.equals(new ConstExpression(Val.konst))) {
                            Expression y = ((CallExpression) argument).argument;
                            return new CallExpression(
                                    new ConstExpression(Val.konst),
                                    new CallExpression(x, y)
                            );
                        }
                    }
                }
            }
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CallExpression that = (CallExpression) o;
        return Objects.equals(function, that.function) && Objects.equals(argument, that.argument);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(function);
        result = 31 * result + Objects.hashCode(argument);
        return result;
    }
}
