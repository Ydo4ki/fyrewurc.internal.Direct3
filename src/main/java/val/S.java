package val;

import java.util.Objects;

final class S implements Value {
    
    public static final S s = new S();
    
    private S() {}
    
    @Override
    public Value call(Value x) {
        return new Sx(x);
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
        return "substitutor";
    }

    static class Sx implements Value {
        private final Value x;

        Sx(Value x) {
            this.x = x;
        }

        @Override
        public Value call(Value y) {
            return new Sxy(x, y);
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
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            Sx sx = (Sx) o;
            return Objects.equals(x, sx.x);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(x);
        }

        @Override
        public String toString() {
            return "substitutor(" + x + ")";
        }
    }
    
    static class Sxy implements Value {
        final Value x, y;

        Sxy(Value x, Value y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Value call(Value z) {
            return x.call(z).call(new LazyValueApplication(y, z));
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
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            Sxy sxy = (Sxy) o;
            return Objects.equals(x, sxy.x) && Objects.equals(y, sxy.y);
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(x);
            result = 31 * result + Objects.hashCode(y);
            return result;
        }

        @Override
        public String toString() {
            if (x == K.k && y == K.k) return "mirror";
            return "substitutor(" + x + ", " + y + ")";
        }
    }
}
