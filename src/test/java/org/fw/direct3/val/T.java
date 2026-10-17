package org.fw.direct3.val;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class T {

    private static final List<Val> vals = Arrays.asList(
            Val.konst,
            Val.substitutor,
            Val.mirror,
            Val.telephonize,
            Val.type,
            Val.telephonist
    );

    static Stream<Arguments> V1() {
        return vals.stream().map(Arguments::of);
    }

    static Stream<Arguments> V2() {
        return vals.stream().flatMap(a -> vals.stream().map(b -> Arguments.of(a, b)));
    }

    @ParameterizedTest
    @MethodSource("V2")
    void testPurity(Val x, Val y) {
        Val r = x.call(y);
        assertNotNull(r);
        assertEquals(r, x.call(y));
    }

    @ParameterizedTest
    @MethodSource("V1")
    void testTelephony(Val x) {
        Val ox = x;
        try {
            while(!x.equals(Val.telephonist)) x = Val.type.call(x);
        } catch (StackOverflowError e) {
            fail(ox + " seems to be from another universe");
        }
    }

    @ParameterizedTest
    @MethodSource("V2")
    void testTypeBehavior(Value x, Value y) {
        Value a = x.call(y);
        Value tx = Type.type.call(x);
        Value b = tx.call(new BoxValue.Call(x, y));
        assertEquals(a, b);
    }
}
