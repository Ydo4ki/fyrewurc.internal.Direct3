package parser;

import val.Val;

import java.util.function.Function;

class ConstExpression extends Expression {
    private final Val val;

    ConstExpression(Val val) {
        this.val = val;
    }

    @Override
    public Val compile(Function<String, Val> declarations) {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
