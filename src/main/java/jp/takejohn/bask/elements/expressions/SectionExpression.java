package jp.takejohn.bask.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SectionExpression<S extends Section, T> extends SimpleExpression<T> {

    public static <U> void register(@NotNull Class<? extends Expression<U>> expressionClass,
                                    @NotNull Class<U> returnType, @NotNull String pattern) {
        Skript.registerExpression(expressionClass, returnType, ExpressionType.SIMPLE,
                "[the] " + pattern + "[1¦(-| )<\\d+>]");
    }

    private String name;

    private S section;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed,
                        SkriptParser.@NotNull ParseResult parseResult) {
        name = parseResult.expr;
        final @NotNull List<S> sections = getParser().getCurrentSections(getSectionType());
        if (sections.isEmpty()) {
            Skript.error("There are no " + getSectionName() + " sections");
            return false;
        }
        if (parseResult.mark == 0 && sections.size() > 1) {
            Skript.error("There are more than one " + getSectionName() + " sections nested. " +
                    "Use '" + getPattern() + "-n' expression where 'n' is the depth of the intended section");
            return false;
        }
        if (parseResult.mark == 1) {
            final int sectionDepth = Integer.parseInt(parseResult.regexes.get(0).group());
            if (sectionDepth > sections.size()) {
                Skript.error("There are not more than " + sections.size() + " " + getSectionName() + " sections nested");
                return false;
            }
            section = sections.get(sectionDepth - 1);
        } else {
            section = sections.get(0);
        }
        return true;
    }

    protected abstract @NotNull Class<S> getSectionType();

    protected abstract @NotNull String getSectionName();

    protected abstract @NotNull String getPattern();

    public S getSection() {
        return section;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return name;
    }

}
