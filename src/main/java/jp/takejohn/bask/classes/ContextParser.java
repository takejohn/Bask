package jp.takejohn.bask.classes;

import ch.njol.skript.lang.ParseContext;
import org.jetbrains.annotations.NotNull;

/**
 * A parser to parse data from a string in Skript.
 * @param <T> the type of data
 */
public interface ContextParser<T> {

    boolean canParse(@NotNull ParseContext context);

    T parse(@NotNull String s, @NotNull ParseContext context);

}
