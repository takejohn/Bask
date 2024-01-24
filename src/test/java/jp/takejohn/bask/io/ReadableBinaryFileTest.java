package jp.takejohn.bask.io;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ReadableBinaryFileTest {

    private static Path path;

    @BeforeAll
    static void before() {
        final URL url = ReadableBinaryFileTest.class.getClassLoader().getResource("test/dummy.txt");
        try {
            final URI uri = Objects.requireNonNull(url).toURI();
            path = Paths.get(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void read() {
        try (final ReadableBinaryFile file = new ReadableBinaryFile(path)) {
            assertEquals(Blob.valueOf("b\"012\""), file.read(DataSize.valueOf("3 bytes")));
            assertEquals(Blob.valueOf("b\"3\""), file.read(DataSize.valueOf("1 byte")));
            assertEquals(Blob.valueOf("b\"\""), file.read(DataSize.valueOf("0 byte")));
            assertEquals(Blob.valueOf("b\"45\""), file.read(DataSize.valueOf("4 bytes")));
            assertNull(file.read(DataSize.valueOf("1 byte")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}