package org.fw.direct3.val;

import java.util.Objects;

final class Telephonist implements Value {

    static final Telephonist telephonist = new Telephonist(null);

    private final Telephonist instance;

    Telephonist(Telephonist instance) {
        this.instance = instance;
    }

    @Override
    public Value call(Value value) {
        return new LazyValueApplication(this, instance);
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
        if (instance == null)
            return "Telephonist";
        return Type.type + "(" + instance + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Telephonist that = (Telephonist) o;
        return Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instance);
    }
}
