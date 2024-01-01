package jp.takejohn.bask.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class TupleTest {

    @Test
    void test() {
        final var nil = Tuple.of();
        final var single = Tuple.of("abc");
        final var dbl = Tuple.of("def", 123);
        final var triple = Tuple.of("ghi", 456, 7.89);

        assertIterableEquals(List.of(), nil);
        assertIterableEquals(List.of("abc"), single);
        assertIterableEquals(List.of("def", 123), dbl);
        assertIterableEquals(List.of("ghi", 456, 7.89), triple);

        assertEquals("()", nil.toString());
        assertEquals("(abc)", single.toString());
        assertEquals("(def, 123)", dbl.toString());
        assertEquals("(ghi, 456, 7.89)", triple.toString());
    }

}
