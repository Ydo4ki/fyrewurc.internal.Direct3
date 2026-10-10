package val;

import java.util.Objects;

public final class ValueApplication implements Value {
    private final Value a, b;

    public ValueApplication(Value a, Value b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Perform beta-reduction.
     * @return reduced value
     */
    public Value reduce() {
        Value a = this.a,
              b = this.b;

        if (a instanceof ValueApplication)
            a = ((ValueApplication) a).reduce();

        return a.call(b);
    }

    @Override
    public Value call(Value c) {
        return new ValueApplication(this, c);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ValueApplication that = (ValueApplication) o;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(a);
        result = 31 * result + Objects.hashCode(b);
        return result;
    }
}
