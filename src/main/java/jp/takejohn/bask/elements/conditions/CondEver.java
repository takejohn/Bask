package jp.takejohn.bask.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Ever")
@Description("A condition always or never passed.")
@Examples({
        "while ever:",
        "    chance of 4%:",
        "        exit loop",
        "    wait a tick"
})
@Since("0.1.0")
public class CondEver extends Condition {

    static {
        Skript.registerCondition(CondEver.class, "ever|always|anytime", "never");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed,
                        SkriptParser.@NotNull ParseResult parseResult) {
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(@Nullable Event event) {
        return check();
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return check() ? "ever" : "never";
    }

    private boolean check() {
        return !isNegated();
    }

}
