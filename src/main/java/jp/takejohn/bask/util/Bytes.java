package jp.takejohn.bask.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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

}
