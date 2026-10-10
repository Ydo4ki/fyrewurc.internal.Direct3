package val;

import java.util.Objects;

public class Sym implements Value {
    private final String identifier;

    Sym(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "'" + identifier + "'";
    }

    @Override
    public Value call(Value value) {
        System.out.println("Useless action was just performed");
        return new ValueApplication(this, value); // irreducible
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
