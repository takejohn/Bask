package jp.takejohn.bask.io;

import jp.takejohn.bask.annotations.SkriptDoc;
import jp.takejohn.bask.annotations.SkriptType;
import jp.takejohn.bask.annotations.SkriptTypeParse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SkriptType(value = "datasize", user = "data[ ]sizes?")
@SkriptDoc(
        name = "Data Size",
        description = "A size of data",
        since = "0.1.0"
)
public final class DataSize implements Serializable {

    private enum Unit {

        BYTE(1L, "byte", "bytes", "B"),
        KILOBYTE(1L << 10, "kilobyte", "kilobytes", "kB", "kiB"),
        MEGABYTE(1L << 20, "megabyte", "megabytes", "MB", "MiB"),
        GIGABYTE(1L << 30, "gigabyte", "gigabytes", "GB", "GiB"),
        TERABYTE(1L << 40, "terabyte", "terabytes", "TB", "TiB"),
        PETABYTE(1L << 50, "petabyte", "petabytes", "PB", "PiB"),
        EXABYTE(1L << 60, "exabyte", "exabytes", "EB", "EiB");

        public final long multiple;

        private final @NotNull List<String> representations;

        Unit(long multiple, @NotNull String @NotNull... representations) {
            this.multiple = multiple;
            this.representations = List.of(representations);
        }

        public static @NotNull Unit forRepresentation(@NotNull String s) {
            for (@NotNull Unit unit : values()) {
                for (@NotNull String representation : unit.representations) {
                    if (representation.equalsIgnoreCase(s)) {
                        return unit;
                    }
                }
            }
            throw new IllegalArgumentException(s + " is not a valid unit");
        }

    }

    private static final Pattern pattern = Pattern.compile("(\\d+)? *([A-Za-z]+)");

    private final long bytes;

    public DataSize(long bytes) {
        this.bytes = bytes;
    }

    @SkriptTypeParse
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull DataSize valueOf(@NotNull String s) {
        final @NotNull Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("\"" + s + "\" does not match the regex pattern " + pattern);
        }
        return new DataSize(Long.parseLong(matcher.group(1)) * Unit.forRepresentation(matcher.group(2)).multiple);
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull DataSize add(@NotNull DataSize val) {
        Objects.requireNonNull(val, "val cannnot be null");
        return new DataSize(this.bytes + val.bytes);
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull DataSize subtract(@NotNull DataSize val) {
        Objects.requireNonNull(val, "val cannot be null");
        return new DataSize(this.bytes - val.bytes);
    }

    @Override
    @Contract(pure = true)
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DataSize dataSize)) {
            return false;
        }
        return bytes == dataSize.bytes;
    }

    @Override
    @Contract(pure = true)
    public int hashCode() {
        return Long.hashCode(bytes);
    }

    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        if (bytes == 1) {
            return "1 byte";
        }
        return bytes + " bytes";
    }

}
