package jp.takejohn.bask.util;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodType;

public final class Primitives {

    private Primitives() {
        throw new AssertionError();
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrap(@NotNull Class<T> c) {
        return (Class<T>) MethodType.methodType(c).wrap().returnType();
    }

}
