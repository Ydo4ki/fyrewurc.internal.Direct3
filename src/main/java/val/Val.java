package val;

import java.util.Objects;
import java.util.function.BiFunction;

public final class Val {
    private final Value value;
    @SuppressWarnings("FieldMayBeFinal")
    private BiFunction<Val, Val, Val> optimizedImplementationNative;

    private Val(Value value) {
        this.value = value;
        this.optimizedImplementationNative = getInitialOptimizedImplementationNative(value);
    }

    // "me.value" is the same as the "value" argument, we add "me" to the function to avoid unnecessary lambda allocations
    private static BiFunction<Val, Val, Val> getInitialOptimizedImplementationNative(Value value) {
        if (value instanceof S.Sxy) {
            S.Sxy s = (S.Sxy) value;
            if (s.x == val.K.k && s.y == val.K.k) {
//                System.out.println("Im fast yoy");
                return (me, arg) -> arg;
            }
        }
        return (me, arg) -> wrap(me.value.call(arg.value));
    }

    private static Val wrap(Value value) {
        return new Val(value);
    }

    public Val call(Val val) {
        return optimizedImplementationNative.apply(this, val);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Val val = (Val) o;
        return Objects.equals(value, val.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public static final Val substitutor = wrap(val.S.s);
    public static final Val konst = wrap(val.K.k);
    public static Val symbol(String identifier)
        { return wrap(new Sym(identifier)); }
}
