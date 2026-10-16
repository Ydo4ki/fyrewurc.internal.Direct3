package org.fw.direct3.parser;

import org.fw.direct3.val.Val;

import java.util.function.Function;

class CallExpression extends Expression {
    final Expression function;
    final Expression argument;

    CallExpression(Expression function, Expression argument) {
        this.function = function;
        this.argument = argument;
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
}
