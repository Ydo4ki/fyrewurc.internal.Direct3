package org.fw.direct3.parser;

import org.fw.direct3.val.Val;

import java.util.function.Function;

abstract class Expression {
    Expression() {}

    public abstract Val compile(Function<String, Val> declarations);

    protected abstract Expression simplify();
}
