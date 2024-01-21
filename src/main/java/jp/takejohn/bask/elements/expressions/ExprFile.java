package jp.takejohn.bask.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import jp.takejohn.bask.elements.sections.SecOpenFile;
import jp.takejohn.bask.io.OpenedFile;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExprFile extends SimpleExpression<OpenedFile> {

    static {
        Skript.registerExpression(ExprFile.class, OpenedFile.class, ExpressionType.SIMPLE,
                "file", "file-<\\d+>");
    }

    private int sectionDepth;

    private SecOpenFile section;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed,
                        SkriptParser.@NotNull ParseResult parseResult) {
        final @NotNull List<SecOpenFile> sections = getParser().getCurrentSections(SecOpenFile.class);
        if (sections.isEmpty()) {
            Skript.error("There are no open file sections");
            return false;
        }
        if (matchedPattern == 0 && sections.size() > 1) {
            Skript.error("There are more than one open file sections nested. " +
                    "Use 'file-n' expression where 'n' is the depth of the intended section");
            return false;
        }
        if (matchedPattern == 1) {
            sectionDepth = Integer.parseInt(parseResult.regexes.get(0).group());
            if (sectionDepth > sections.size()) {
                Skript.error("There are not more than " + sections.size() + " open file sections nested");
                return false;
            }
        } else {
            sectionDepth = 1;
        }
        section = sections.get(sectionDepth - 1);
        return true;
    }

    @Override
    protected @NotNull OpenedFile @Nullable [] get(@NotNull Event event) {
        final @Nullable OpenedFile result = section.getResource(event);
        return result != null ? new OpenedFile[]{ result } : null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends OpenedFile> getReturnType() {
        return section.getResourceType();
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "file-" + sectionDepth;
    }

}
