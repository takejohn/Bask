package jp.takejohn.bask.annotations;

import ch.njol.skript.lang.ParseContext;
import org.jetbrains.annotations.NotNull;

public enum SkriptParseContext {
    DEFAULT("default", ParseContext.DEFAULT),
    EVENT("event", ParseContext.EVENT),
    COMMAND("command", ParseContext.COMMAND),
    CONFIG("config", ParseContext.CONFIG),
    SCRIPT("script", ParseContext.SCRIPT);

    private final @NotNull String string;

    private final @NotNull ParseContext parseContext;

    SkriptParseContext(@NotNull String string, @NotNull ParseContext parseContext) {
        this.string = string;
        this.parseContext = parseContext;
    }

    public @NotNull ParseContext asParseContext() {
        return parseContext;
    }

    @Override
    public String toString() {
        return string;
    }

}
