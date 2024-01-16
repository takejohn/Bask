package jp.takejohn.bask.io;

import jp.takejohn.bask.BaskSyntaxException;
import jp.takejohn.bask.annotations.SkriptDoc;
import jp.takejohn.bask.annotations.SkriptType;
import jp.takejohn.bask.annotations.SkriptTypeParse;
import jp.takejohn.bask.util.Bytes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;

/**
 * An immutable binary data.
 */
@SkriptType(value = "blob", user = "blobs?")
@SkriptDoc(name = "Blob",
        description = "An immutable binary data.",
        since = "0.1.0")
public final class Blob implements Serializable {

    public static final class BlobLiteralException extends BaskSyntaxException {

        public BlobLiteralException(String message, int index) {
            super("At index " + index + ": " + message);
        }

    }

    private static final class Parser {

        private final char @NotNull[] src;

        private int index;

        private int markedIndex;

        private boolean isEnd;

        public Parser(@NotNull String s) {
            src = s.substring(2, s.length() - 1).toCharArray();  // characters between 'b"' and '"'
            index = 0;
            isEnd = !hasRestChars(1);
        }

        public @Nullable Blob parse() {
            final @NotNull List<@NotNull Byte> byteList = new ArrayList<>();
            while (!isEnd) {
                byteList.add(next());
            }
            if (hasRestChars(1)) {
                return null;
            }
            return new Blob(Bytes.toByteArray(byteList));
        }

        private boolean hasRestChars(int count) {
            return src.length - index >= count;
        }

        private char nextChar() {
            final char result = src[index];
            index++;
            if (!hasRestChars(1)) {
                isEnd = true;
            }
            return result;
        }

        private void setMark() {
            this.markedIndex = index;
        }

        private void resetMark() {
            this.index = markedIndex;
            if (hasRestChars(1)) {
                isEnd = false;
            }
        }

        private byte next() {
            final char c = nextChar();
            if (c == '\\') {
                return nextAfterBackslash();
            } else if (c == '"') {
                return nextAfterQuote();
            } else if (isPrintableAsciiCharacter(c)) {
                return (byte) c;
            } else {
                throw new BlobLiteralException("Unexpected character + '" + c + "'", index);
            }
        }

        private byte nextAfterBackslash() {
            setMark();
            final char escaped = nextChar();
            if (escaped == '\\') {
                return '\\';
            } else if (escaped == 'x' && hasRestChars(2)) {
                final char hi = nextChar();
                final char lo = nextChar();
                try {
                    return (byte) Integer.parseUnsignedInt(new String(new char[]{ hi, lo }), 16);
                } catch (NumberFormatException e) {
                    resetMark();
                    return '\\';
                }
            } else {
                resetMark();
                return '\\';
            }
        }

        private byte nextAfterQuote() {
            setMark();
            if (isEnd) {
                throw new BlobLiteralException("Unexpected end of blob literal", index);
            }
            final char escaped = nextChar();
            if (escaped != '"') {
                resetMark();
                isEnd = true;
            }
            return '"';
        }

    }

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
     * @param s a string represents the binary data.
     * @return a newly created Blob.
     */
    @SkriptTypeParse
    public static @Nullable Blob valueOf(@NotNull String s) {
        Objects.requireNonNull(s, "s cannot be null");
        if (!s.startsWith("b\"") || !s.endsWith("\"")) {
            return null;
        }
        return new Parser(s).parse();
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
     * Returns a string representation of this Blob.
     * @return The string representation of this Blob
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull String toString() {
        final @NotNull StringBuilder result = new StringBuilder("b\"");
        for (byte b : data) {
            final int unsignedByte = b & 0xFF;
            if (isPrintableAsciiCharacter(unsignedByte)) {
                if (unsignedByte == '"') {
                    result.append("\"\"");
                } else if (unsignedByte == '\\') {
                    result.append("\\\\");
                }else {
                    result.append((char) unsignedByte);
                }
            } else {
                result.append("\\x");
                final @NotNull String hexString = Integer.toHexString(unsignedByte);
                if (unsignedByte < 0x10) {
                    result.append('0').append(hexString);
                } else {
                    result.append(hexString);
                }
            }
        }
        result.append('\"');
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

    private static boolean isPrintableAsciiCharacter(int c) {
        return 0x20 <= c && c <= 0x7E;
    }

}
