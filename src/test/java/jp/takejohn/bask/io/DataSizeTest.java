package jp.takejohn.bask.io;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DataSizeTest {

    private final DataSize one_byte = Objects.requireNonNull(DataSize.valueOf("1B"));

    private final DataSize four_kilobytes = Objects.requireNonNull(DataSize.valueOf("4 kilobytes"));

    @Test
    void add() {
        assertEquals(DataSize.valueOf("4097 bytes"), one_byte.add(four_kilobytes));
    }

    @Test
    void subtract() {
        assertEquals(DataSize.valueOf("4095 bytes"), four_kilobytes.subtract(one_byte));
    }

    @Test
    void multiply() {
        assertEquals(DataSize.valueOf("42 bytes"), one_byte.multiply(42));
        assertEquals(DataSize.valueOf("4 megabytes"), four_kilobytes.multiply(1024));
    }

    @Test
    void testToString() {
        assertEquals("1 byte", one_byte.toString());
        assertEquals("4096 bytes", four_kilobytes.toString());
    }

    @Test
    void compareTo() {
        assertEquals(-1, one_byte.compareTo(four_kilobytes));
        assertEquals(1, four_kilobytes.compareTo(one_byte));
    }
}