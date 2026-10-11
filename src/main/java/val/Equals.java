package val;

import java.util.Objects;

final class Equals implements Value {

    static final Equals eq = new Equals();

    static final Value T = K.k;
    static final Value F = S.s.call(K.k);
    
    private Equals() {}
    
    @Override
    public Value call(Value x) {
        return new EqualsX(x);
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
        return "eq";
    }

    static class EqualsX implements Value {
        private final Value x;

        EqualsX(Value x) {
            this.x = x;
        }

        @Override
        public Value call(Value y) {
            if (x.equals(y))
                return T;

            if (x.isInNormalForm() && y.isInNormalForm())
                return F;

            return new LazyValueApplication(this, y);
        }

        @Override
        public boolean isInNormalForm() {
            return x.isInNormalForm();
        }

        @Override
        public Value reduce() {
            return new EqualsX(x.reduce());
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            EqualsX equalsX = (EqualsX) o;
            return Objects.equals(x, equalsX.x);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(x);
        }
    }
}
