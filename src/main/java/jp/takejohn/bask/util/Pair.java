package jp.takejohn.bask.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

public record Pair<E, F extends @NotNull Tuple>(E first, F next) implements Tuple {

    @Override
    public boolean hasElements() {
        return true;
    }

    @NotNull
    @Override
    public Iterator<@Nullable Object> iterator() {
        return new Iterator<>() {

            @NotNull Tuple rest = Pair.this;

            @Override
            public boolean hasNext() {
                return rest.hasElements();
            }

            @Override
            public Object next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("This iterator has no more elements rest");
                }
                final @NotNull Pair<?, ?> pair = (Pair<?, ?>) rest;
                final @Nullable Object result = pair.first();
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
