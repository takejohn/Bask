package jp.takejohn.bask.io;

import jp.takejohn.bask.annotations.SkriptDoc;
import jp.takejohn.bask.annotations.SkriptType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * An immutable binary data.
 */
@SkriptType(value = "blob", user = "blobs?")
@SkriptDoc(name = "Blob",
        description = "An immutable binary data.",
        since = "0.1.0")
public final class Blob implements Serializable {

    private final byte @NotNull[] data;

    /**
     * Creates a Blob from a specified array of bytes.
     * @param data an array of bytes
     */
    public Blob(byte @NotNull... data) {
        this.data = Objects.requireNonNull(data, "data cannot be null");
    }

    /**
     * Creates a Blob from a string.
     * When <code>s</code> is a string, <code>Blob.valueOf(s).toString().equalsIgnoreCase(s)</code> should be true
     * @param s a string represents the binary data in hexadecimal.
     * @return a newly created Blob.
     */
    @Contract("_ -> new")
    public static @NotNull Blob valueOf(@NotNull String s) {
        Objects.requireNonNull(s, "s cannot be null");
        final int sLength = s.length();
        if (sLength % 2 != 0) {
            throw new IllegalArgumentException("the length of s is not divisible by 2");
        }
        final int dataLength = sLength / 2;
        final byte[] data = new byte[dataLength];
        for (int i = 0 ; i < dataLength ; i++) {
            data[i] = (byte) Integer.parseUnsignedInt(s, i * 2, i * 2 + 2, 16);
        }
        return new Blob(data);
    }

    /**
     * Returns the size of this Blob in bytes.
     * @return The size of this Blob
     */
    public int size() {
        return data.length;
    }

    /**
     * Returns a copy of the bytes represented by this Blob.
     * @return an array of bytes
     */
    @Contract(value = " -> new", pure = true)
    public byte @NotNull[] getBytes() {
        return Arrays.copyOf(data, data.length);
    }

    /**
     * Returns a hexadecimal representation of this Blob.
     * The characters in the string are only <code>'0'</code> through <code>'9'</code>
     * and <code>'a'</code> through <code>'f'</code>.
     * When <code>blob</code> is an instance of <code>Blob</code>,
     * <code>blob.toString().length()</code> should be equal to <code>blob.size() * 2</code>.
     * @return The hexadecimal representation of this Blob
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull String toString() {
        final @NotNull StringBuilder result = new StringBuilder();
        for (byte b : data) {
            final int unsignedByte = b & 0xFF;
            final @NotNull String hexString = Integer.toHexString(unsignedByte);
            if (unsignedByte < 0x10) {
                result.append('0').append(hexString);
            } else {
                result.append(hexString);
            }
        }
        return new String(result);
    }

    @Contract(pure = true)
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Blob blob) {
            return Arrays.equals(data, blob.data);
        }
        return false;
    }

    @Contract(pure = true)
    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

}
