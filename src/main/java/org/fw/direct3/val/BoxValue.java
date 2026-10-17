package org.fw.direct3.val;

import java.util.Objects;

final class BoxValue implements Value {
    final Value type;
    final Value payload;

    BoxValue(Value type, Value payload) {
        this.type = type;
        this.payload = payload;
    }

    @Override
    public Value call(Value arg) {
        return type.call(new Call(this, arg));
    }

    @Override
    public boolean isInNormalForm() {
        return type.isInNormalForm() && payload.isInNormalForm();
    }

    @Override
    public Value reduce() {
        return new BoxValue(type.reduce(), payload.reduce());
    }

    @Override
    public Value getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + "{" + payload + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        BoxValue boxValue = (BoxValue) o;
        return Objects.equals(type, boxValue.type) && Objects.equals(payload, boxValue.payload);
    }

    @Override
    public int hashCode() {
        return 31 * type.hashCode() + payload.hashCode();
    }

    static class Call implements Value {
        public static final Value type = new Value() {
            @Override
            public Value call(Value value) {
                return new LazyValueApplication(this, value);
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
                return "Call";
            }
        };

        private final Value val, arg;

        Call(Value val, Value arg) {
            this.val = val;
            this.arg = arg;
        }

        @Override
        public Value call(Value arg) {
            return V.match(arg, new LazyValueApplication(this, this.arg),
                    new Sym("val"), this.val,
                    new Sym("arg"), this.arg,
                    new Sym("instance"), null,
                    new Sym("unpack"), null
            );
        }

        @Override
        public Value getType() {
            return type;
        }

        @Override
        public boolean isInNormalForm() {
            return val.isInNormalForm() && arg.isInNormalForm();
        }

        @Override
        public Value reduce() {
            return new Call(val.reduce(), arg.reduce());
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            Call call = (Call) o;
            return Objects.equals(val, call.val) && Objects.equals(arg, call.arg);
        }

        @Override
        public int hashCode() {
            return 31 * val.hashCode() + arg.hashCode();
        }

        @Override
        public String toString() {
            return type + "{" + val + ", " + arg + "}";
        }
    }

    static class AllUnpacker implements Value {
        public static final AllUnpacker unpacker = new AllUnpacker();

        private AllUnpacker() {}

        @Override
        public Value call(Value value) {
            if (value instanceof BoxValue)
                return ((BoxValue) value).payload;
            return new LazyValueApplication(unpacker, value);
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
            return "_AllUnpacker";
        }
    }

    static Value unpack(Value value) {
        return AllUnpacker.unpacker.call(value);
    }

    static class Instancer implements Value {
        public static final Instancer type = new Instancer();

        private Instancer() {}

        @Override
        public Value call(Value value) {
            Value instance = value.call(new Sym("val"));
            return V.IfEquals(value.getType(), Call.type)
                    .call(V.IfEquals(instance.getType(), type)
                            .call(new BoxValue(unpack(instance), value.call(new Sym("arg"))))
                            .call(new LazyValueApplication(this, value)) // rip
                    )
                    .call(new LazyValueApplication(this, value)); // rip
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
            return "Instancer";
        }
    }
}
