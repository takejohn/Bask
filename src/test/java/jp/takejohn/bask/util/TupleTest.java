package jp.takejohn.bask.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {

    @Test
    void test() {
        final var single = Tuple.of("abc");
        final var dbl = Tuple.of("def", 123);
        final var triple = Tuple.of("ghi", 456, 7.89);

        assertIterableEquals(List.of("abc"), single);
        assertIterableEquals(List.of("def", 123), dbl);
        assertIterableEquals(List.of("ghi", 456, 7.89), triple);

        assertEquals("(abc)", single.toString());
        assertEquals("(def, 123)", dbl.toString());
        assertEquals("(ghi, 456, 7.89)", triple.toString());
    }

}