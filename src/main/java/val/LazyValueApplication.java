package val;

import java.util.Objects;

final class LazyValueApplication implements Value {
    private final Value a, b;
    private Value reduced;

    LazyValueApplication(Value a, Value b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Perform beta-reduction.
     * @return reduced value
     */
    @Override
    public Value reduce() {
        if (this.reduced != null)
            return this.reduced;

        Value a = this.a,
              b = this.b;

        if (!a.isInNormalForm())
            a = a.reduce();

        return this.reduced = a.call(b);
    }

    @Override
    public boolean isInNormalForm() {
        return this.reduce().equals(this);
    }

    @Override
    public Value call(Value c) {
        return new LazyValueApplication(this, c);
    }

    @Override
    public String toString() {
        return a + "(" + b + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        LazyValueApplication that = (LazyValueApplication) o;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(a);
        result = 31 * result + Objects.hashCode(b);
        return result;
    }
}
