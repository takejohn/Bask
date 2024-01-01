package jp.takejohn.bask.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

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

        assertEquals(Tuple.of("ghi", 456, 7.89), triple);
    }

    @Test
    void iterator() {
        final var tuple = Tuple.of(false, 1, 3.14);
        final var iterator = tuple.iterator();
        assertEquals(false, iterator.next());
        assertEquals(1, iterator.next());
        assertEquals(3.14, iterator.next());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

}
