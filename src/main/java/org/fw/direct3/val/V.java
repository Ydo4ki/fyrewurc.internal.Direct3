package org.fw.direct3.val;

final class V {
    static Value IfEquals(Value a, Value b) {
        return Equals.eq.call(a).call(b);
    }

    static Value match(Value arg, Value $else, Value... values) {
        if (values.length % 2 == 1)
            throw new IllegalStateException("Wrong number of arguments " + values.length);

        for (int i = values.length - 2; i >= 0; i -= 2) {
            $else = IfEquals(arg, values[i])
                    .call(values[i + 1])
                    .call($else);
        }
        return $else;
    }
}
