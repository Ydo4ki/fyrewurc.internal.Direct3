package org.fw.direct3.val;

import java.util.Objects;

class UNDECIDABILITY implements Value {
    static Value generator = new Value() {
        @Override
        public Value call(Value value) {
            return value.markUndecidable();
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
        public Value getType() {
            return Telephonist.telephonist;
        }

        @Override
        public String toString() {
            return "\\bot";
        }
    };


    private final Value source;

    UNDECIDABILITY(Value source) {
        this.source = source;
    }

    @Override
    public Value call(Value value) {
        return new LazyValueApplication(this, value).markUndecidable();
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
    public Value getType() {
        return new LazyValueApplication(Type.type, this).markUndecidable();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        UNDECIDABILITY that = (UNDECIDABILITY) o;
        return Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(source);
    }

    @Override
    public String toString() {
        return "⊥";
    }

    @Override
    public Value markUndecidable() {
        return this;
    }
}
