package jp.takejohn.bask.util;

import org.jetbrains.annotations.NotNull;

/**
 * An immutable container keeping type-safety while containing different types of elements.
 * @param <E> The base type of every element in the tuple
 */
public sealed interface Tuple<E> extends Iterable<E> permits Pair, Nil {

    boolean hasElements();

    static <A> @NotNull Nil<A> of() {
        return Nil.getInstance();
    }

    static <E0> @NotNull Pair<E0, E0, @NotNull Nil<E0>> of(E0 e0) {
        return new Pair<>(e0, Nil.getInstance());
    }

    static <A, E0 extends A, E1 extends A> @NotNull Pair<A, E0, @NotNull Pair<A, E1, @NotNull Nil<A>>> of(E0 e0, E1 e1) {
        return new Pair<>(e0, new Pair<>(e1, Nil.getInstance()));
    }

    static <A, E0 extends A, E1 extends A, E2 extends A>
    @NotNull Pair<A, E0, @NotNull Pair<A, E1, @NotNull Pair<A, E2, @NotNull Nil<A>>>> of(E0 e0, E1 e1, E2 e2) {
        return new Pair<>(e0, new Pair<>(e1, new Pair<>(e2, Nil.getInstance())));
    }

}
