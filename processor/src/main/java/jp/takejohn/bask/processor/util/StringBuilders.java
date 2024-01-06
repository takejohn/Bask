package jp.takejohn.bask.processor.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class StringBuilders {

    private StringBuilders() {
        throw new AssertionError();
    }

    public static void appendJoined(@NotNull StringBuilder stringBuilder, @NotNull CharSequence delimiter,
                                    @NotNull Iterable<?> objects) {
        final @NotNull Iterator<?> iterator = objects.iterator();
        if (!iterator.hasNext()) {
            return;
        }
        stringBuilder.append(iterator.next());
        iterator.forEachRemaining(object -> {
            stringBuilder.append(delimiter);
            stringBuilder.append(object);
        });
    }

}
