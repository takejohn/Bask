package jp.takejohn.bask.io;

import jp.takejohn.bask.util.Bytes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlobTest {

    private static final Blob blob = new Blob(Bytes.toByteArray(0xCA, 0xFE, 0xBA, 0xBE));

    private static final Blob blob2 = new Blob(Bytes.toByteArray(0x00, 0x01, 0x02, 0x03));

    private static final Blob blob3 = new Blob(Bytes.toByteArray(0x04, 0x05, 0x06));

    private static final Blob blob4 = new Blob(Bytes.toByteArray(0xCA, 0xFE, 0xBA, 0xBE));

    private static final Blob empty = new Blob();

    @Test
    void size() {
        assertEquals(4, blob.size());
        assertEquals(4, blob2.size());
        assertEquals(3, blob3.size());
        assertEquals(4, blob4.size());
        assertEquals(0, empty.size());
    }

    @Test
    void getBytes() {
        assertArrayEquals(Bytes.toByteArray(0xCA, 0xFE, 0xBA, 0xBE), blob.getBytes());
        assertArrayEquals(new byte[0], empty.getBytes());
    }

    @Test
    void testToString() {
        assertEquals("cafebabe", blob.toString());
        assertEquals("00010203", blob2.toString());
        assertEquals("040506", blob3.toString());
        assertEquals("", empty.toString());
    }

    @Test
    void testEquals() {
        assertEquals(blob, blob);
        assertEquals(blob, blob4);
        assertNotEquals(blob, blob2);
        assertNotEquals(blob2, blob3);
        assertNotEquals(blob, new Object());
    }

    @Test
    void testHashCode() {
        assertEquals(blob.hashCode(), blob4.hashCode());
    }

}