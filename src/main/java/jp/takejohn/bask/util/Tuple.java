package jp.takejohn.bask.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public sealed interface Tuple extends Iterable<@Nullable Object> permits Pair, Nil {

    boolean hasElements();

    static @NotNull Nil of() {
        return Nil.INSTANCE;
    }

    static <E> @NotNull Pair<E, @NotNull Nil> of(E e) {
        return new Pair<>(e, Tuple.of());
    }

    static <E0, E1> @NotNull Pair<E0, @NotNull Pair<E1, @NotNull Nil>> of(E0 e0, E1 e1) {
        return new Pair<>(e0, Tuple.of(e1));
    }

    static <E0, E1, E2> @NotNull Pair<E0, @NotNull Pair<E1, @NotNull Pair<E2, @NotNull Nil>>> of(E0 e0, E1 e1, E2 e2) {
        return new Pair<>(e0, Tuple.of(e1, e2));
    }

}
