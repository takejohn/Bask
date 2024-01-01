package jp.takejohn.bask.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

/**
 * A tuple containing no elements
 * @param <E> The base type
 */
public final class Nil<E> implements Tuple<E> {

    @SuppressWarnings("rawtypes")
    private static final Nil INSTANCE = new Nil<>();

    private Nil() {}

    @SuppressWarnings("unchecked")
    public static <E> Nil<E> getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean hasElements() {
        return false;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public String toString() {
        return "()";
    }

}
