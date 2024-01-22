package jp.takejohn.bask.io;

import jp.takejohn.bask.annotations.SkriptDoc;
import jp.takejohn.bask.annotations.SkriptType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@SkriptType(value = "readablefile", user = "readable[ ]files?")
@SkriptDoc(
        name = "Readable File",
        description = "A file opened for reading.",
        since = "0.1.0"
)
public class ReadableFile extends OpenedFile {

    final @NotNull InputStream inputStream;

    @Nullable Blob lastlyReadData;

    public ReadableFile(@NotNull Path path) throws IOException {
        super(path);
        inputStream = new BufferedInputStream(Files.newInputStream(path));
    }

    public static @Nullable ReadableFile open(@Nullable Path path) {
        if (path == null) {
            return null;
        }
        try {
            return new ReadableFile(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads some bytes up to the specified size from the file.
     * @param maxSize the maximum size to read
     * @return the read data or null if reaches to the end of file or an error occurs
     */
    public @Nullable Blob read(@Nullable DataSize maxSize) {
        Objects.requireNonNull(maxSize, "maxSize cannot be null");
        final long maxLength = maxSize.asBytes();
        if (maxLength > Integer.MAX_VALUE) {
           return setLastlyReadData(null);
        }
        final byte[] buffer = new byte[(int) maxLength];
        final int length = execute(() -> inputStream.read(buffer));
        if (length < 0) {
            return setLastlyReadData(null);
        }
        return setLastlyReadData(new Blob(buffer, length));
    }

    protected @Nullable Blob setLastlyReadData(@Nullable Blob data) {
        lastlyReadData = data;
        return data;
    }

    public @Nullable Blob lastlyReadData() {
        return lastlyReadData;
    }

    @Override
    public void close() {
        execute(inputStream::close);
    }

}
