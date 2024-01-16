package jp.takejohn.bask.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class Bytes {

    private Bytes() {
        throw new AssertionError();
    }

    @Contract(value = "_ -> new", pure = true)
    public static byte @NotNull [] toByteArray(int @NotNull... integers) {
        Objects.requireNonNull(integers, "integers cannot be null");
        final int length = integers.length;
        final byte @NotNull[] byteData = new byte[length];
        for (int i = 0 ; i < length ; i++) {
            byteData[i] = (byte) integers[i];
        }
        return byteData;
    }

    @Contract(value = "_ -> new", pure = true)
    public static byte @NotNull[] toByteArray(@NotNull List<@NotNull Byte> list) {
        Objects.requireNonNull(list, "list cannot be null");
        final byte @NotNull[] bytes = new byte[list.size()];
        int i = 0;
        for (@NotNull Byte b : list) {
            bytes[i] = b;
            i++;
        }
        return bytes;
    }

}
