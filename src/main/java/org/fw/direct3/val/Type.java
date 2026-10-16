package org.fw.direct3.val;

class Type implements Value {

    public static final Type type = new Type();

    private Type() {}

    @Override
    public Value call(Value value) {
        if (value instanceof Telephonist)
            return new Telephonist((Telephonist) value);

        return Telephonist.telephonist;
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
    public String toString() {
        return "type";
    }
}
