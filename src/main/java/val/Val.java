package val;

import java.util.Objects;
import java.util.function.BiFunction;

public final class Val {
    private Value value;
    @SuppressWarnings("FieldMayBeFinal")
    private BiFunction<Val, Val, Val> optimizedImplementationNative;

    private Val(Value value) {
        this.value = value;
        ;
    }

    public Val call(Val val) {
        if (optimizedImplementationNative == null)
            this.optimizedImplementationNative = m_getInitialOptimizedImplementationNative(value);

        return optimizedImplementationNative.apply(this, val);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Val val = (Val) o;
        this.m_fullyReduce();
        val.m_fullyReduce();
        return Objects.equals(value, val.value);
    }

    @Override
    public int hashCode() {
        m_fullyReduce();
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        m_fullyReduce();
        return value.toString();
    }

    public static final Val substitutor = m_wrap(val.S.s);
    public static final Val konst = m_wrap(val.K.k);
    public static final Val eq = m_wrap(Equals.eq);
    public static Val symbol(String identifier)
        { return m_wrap(new Sym(identifier)); }


    /* private api */


    // "me.value" is the same as the "value" argument, we add "me" to the function to avoid unnecessary lambda allocations
    private static BiFunction<Val, Val, Val> m_getInitialOptimizedImplementationNative(Value value) {
        if (value instanceof S.Sxy) {
            S.Sxy s = (S.Sxy) value;
            if (s.x == val.K.k && s.y == val.K.k) {
//                System.out.println("Im fast yoy");
                return (me, arg) -> arg;
            }
        }
        return (me, arg) -> m_wrap(new LazyValueApplication(me.value, arg.value));
    }

    private static Val m_wrap(Value value) {
        return new Val(value);
    }

    private Val m_fullyReduce() {
        while (!this.value.isInNormalForm())
            m_reduce();

        return this;
    }

    private Val m_reduce() {
        Value old = this.value;
        this.value = this.value.reduce();
        if (this.value != old) // using equals seems to be too slow for such a minor optimization
            this.optimizedImplementationNative = null; // invalidate
        return this;
    }
}
