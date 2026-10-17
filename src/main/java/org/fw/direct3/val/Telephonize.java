package org.fw.direct3.val;

final class Telephonize implements Value {
    static final Telephonize telephonize = new Telephonize();

    private Telephonize() {
    }

    @Override
    public Value call(Value x) {
        return new TelephonizeX(x);
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
        return "telephonize";
    }

    static class TelephonizeX implements Value {
        private final Value x;

        TelephonizeX(Value x) {
            this.x = x;
        }

        @Override
        public Value call(Value arg) {
            return x.call(new BoxValue.Call(this, arg));
        }

        @Override
        public boolean isInNormalForm() {
            return x.isInNormalForm();
        }

        @Override
        public Value reduce() {
            return new TelephonizeX(x.reduce());
        }

        @Override
        public Value getType() {
            return Telephonist.telephonist;
        }

        @Override
        public String toString() {
            return telephonize + "(" + x + ")";
        }
    }
}
