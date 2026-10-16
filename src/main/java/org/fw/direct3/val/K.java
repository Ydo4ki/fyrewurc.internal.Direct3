package org.fw.direct3.val;

import java.util.Objects;

final class K implements Value {

    public static final K k = new K();

    private K() {}

    @Override
    public Value call(Value x) {
        return new Kx(x);
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
        return "const";
    }

    static class Kx implements Value {
        private final Value x;

        Kx(Value x) {
            this.x = x;
        }

        @Override
        public Value call(Value value) {
            return x;
        }

        @Override
        public boolean isInNormalForm() {
            return x.isInNormalForm();
        }

        @Override
        public Value reduce() {
            return new Kx(x.reduce());
        }

        @Override
        public String toString() {
            return "const(" + x + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            Kx kx = (Kx) o;
            return Objects.equals(x, kx.x);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(x);
        }
    }
}
