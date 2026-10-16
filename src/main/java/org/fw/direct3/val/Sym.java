package org.fw.direct3.val;

import java.util.Objects;

final class Sym implements Value {
    private final String identifier;

    Sym(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public Value call(Value value) {
        return new LazyValueApplication(this, value); // irreducible
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
        return "'" + identifier + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Sym sym = (Sym) o;
        return Objects.equals(identifier, sym.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifier);
    }
}
