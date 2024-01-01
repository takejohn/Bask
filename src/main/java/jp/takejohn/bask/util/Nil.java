package jp.takejohn.bask.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;

public final class Nil implements Tuple {

    public static final Nil INSTANCE = new Nil();

    private Nil() {}

    @Override
    public boolean hasElements() {
        return false;
    }

    @Override
    public @NotNull Iterator<@Nullable Object> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public String toString() {
        return "()";
    }

}
