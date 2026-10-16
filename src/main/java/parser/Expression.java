package parser;

import val.Val;

import java.util.function.Function;

abstract class Expression {
    Expression() {}

    public abstract Val compile(Function<String, Val> declarations);
}
