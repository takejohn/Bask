package jp.takejohn.bask.io;

import jp.takejohn.bask.util.Bytes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlobTest {

    private static final Blob blob = new Blob(Bytes.toByteArray(0xCA, 0xFE, 0xBA, 0xBE));

    private static final Blob blob2 = new Blob(Bytes.toByteArray(0x00, 0x01, 0x02, 0x03));

    private static final Blob blob3 = new Blob(Bytes.toByteArray(0x04, 0x05, 0x06));

    private static final Blob blob4 = new Blob(Bytes.toByteArray(0xCA, 0xFE, 0xBA, 0xBE));

    private static final Blob blob5 = new Blob(Bytes.toByteArray('H', 'e', 'l', 'l', 'o'));

    private static final Blob blob6 = new Blob(Bytes.toByteArray('"'));

    private static final Blob blob7 = new Blob(Bytes.toByteArray('\\'));

    private static final Blob blob8 = new Blob(Bytes.toByteArray('\\', 'x', 'h', 'h'));

    private static final Blob blob9 = new Blob(Bytes.toByteArray('\\', 'a'));

    private static final Blob empty = new Blob();

    @Test
    void valueOf() {
        assertEquals(blob, Blob.valueOf("b\"\\xCA\\xFE\\xBA\\xBE\""));
        assertEquals(empty, Blob.valueOf("b\"\""));
        assertNull(Blob.valueOf("0"));
        assertEquals(blob6, Blob.valueOf("b\"\"\"\""));
        assertThrows(Blob.BlobLiteralException.class, () -> Blob.valueOf("b\"\"\""));
        assertThrows(Blob.BlobLiteralException.class, () -> Blob.valueOf("b\"α\""));
        assertNull(Blob.valueOf("b\"\" \""));
        assertEquals(blob5, Blob.valueOf("b\"Hello\""));
        assertEquals(blob7, Blob.valueOf("b\"\\\\\""));
        assertEquals(blob8, Blob.valueOf("b\"\\xhh\""));
        assertEquals(blob9, Blob.valueOf("b\"\\a\""));
    }

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
        assertEquals("b\"\\xca\\xfe\\xba\\xbe\"", blob.toString());
        assertEquals("b\"\\x00\\x01\\x02\\x03\"", blob2.toString());
        assertEquals("b\"\\x04\\x05\\x06\"", blob3.toString());
        assertEquals("b\"Hello\"", blob5.toString());
        assertEquals("b\"\"\"\"", blob6.toString());
        assertEquals("b\"\\\\\"", blob7.toString());
        assertEquals("b\"\\\\xhh\"", blob8.toString());
        assertEquals("b\"\\\\a\"", blob9.toString());
        assertEquals("b\"\"", empty.toString());
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