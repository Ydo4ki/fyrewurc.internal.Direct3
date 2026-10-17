package org.fw.direct3.val;

final class Type implements Value {

    public static final Type type = new Type();

    private Type() {}

    @Override
    public Value call(Value value) {
        return value.getType();
    }

    @Override
    public boolean isInNormalForm() {
        return true;
    }

    @Override
    public Value reduce() {
        return this;
    }

    @Override
    public Value getType() {
        return Telephonist.telephonist;
    }

    @Override
    public String toString() {
        return "type";
    }
}
