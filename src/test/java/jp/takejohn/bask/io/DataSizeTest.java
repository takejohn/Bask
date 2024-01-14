package jp.takejohn.bask.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataSizeTest {

    private final DataSize one_byte = DataSize.valueOf("1B");

    private final DataSize four_kilobytes = DataSize.valueOf("4 kilobytes");

    @Test
    void add() {
        assertEquals(DataSize.valueOf("4097 bytes"), one_byte.add(four_kilobytes));
    }

    @Test
    void subtract() {
        assertEquals(DataSize.valueOf("4095 bytes"), four_kilobytes.subtract(one_byte));
    }

    @Test
    void testToString() {
        assertEquals("1 byte", one_byte.toString());
        assertEquals("4096 bytes", four_kilobytes.toString());
    }

}