package jp.takejohn.bask.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A tuple containing at least one element.
 * @param first The first element of the tuple
 * @param next The tuple containing leading elements
 * @param <E> The base type of every element in the tuple
 * @param <A> The type of <code>first</code>
 * @param <B> The type of <code>next</code>
 */
public record Pair<E, A extends E, B extends @NotNull Tuple<E>>(A first, B next) implements Tuple<E> {

    @Override
    public boolean hasElements() {
        return true;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {

            @NotNull Tuple<E> rest = Pair.this;

            @Override
            public boolean hasNext() {
                return rest.hasElements();
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("This iterator has no more elements rest");
                }
                final @NotNull Pair<E, ?, ?> pair = (Pair<E, ?, ?>) rest;
                final E result = pair.first();
                rest = pair.next();
                return result;
            }

        };
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(", ", "(", ")");
        for (@Nullable Object element : this) {
            joiner.add(Objects.toString(element));
        }
        return joiner.toString();
    }

}
