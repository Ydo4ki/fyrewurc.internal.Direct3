package org.fw.direct3.parser;

import org.fw.direct3.val.Val;

import java.util.Objects;
import java.util.function.Function;

class ConstExpression extends Expression {
    final Val val;

    ConstExpression(Val val) {
        this.val = val;
    }

    @Override
    public Val compile(Function<String, Val> declarations) {
        return val;
    }

    @Override
    protected Expression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ConstExpression that = (ConstExpression) o;
        return Objects.equals(val, that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(val);
    }
}
