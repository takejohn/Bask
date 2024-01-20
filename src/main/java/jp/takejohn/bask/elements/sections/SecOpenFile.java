package jp.takejohn.bask.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.timings.SkriptTimings;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import jp.takejohn.bask.ExceptionStoringResource;
import jp.takejohn.bask.concurrent.BaskTasks;
import jp.takejohn.bask.io.ReadableFile;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Name("With File")
@Description("A section to handle files")
@Since("0.1.0")
public class SecOpenFile extends Section {

    static {
        Skript.registerSection(SecOpenFile.class, "open file %string% for reading");
    }

    private static class AutoClose extends TriggerItem {

        private final @NotNull ExceptionStoringResource resource;

        public AutoClose(@NotNull ExceptionStoringResource resource) {
            this.resource = resource;
        }

        @Override
        protected boolean run(@NotNull Event event) {
            resource.close();
            return true;
        }

        @Override
        public @NotNull String toString(@Nullable Event event, boolean debug) {
            return "auto-close";
        }

    }

    private Expression<String> pathExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed,
                        SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode,
                        @NotNull List<TriggerItem> triggerItems) {
        ParserInstance.get().setHasDelayBefore(Kleenean.TRUE);
        pathExpression = (Expression<String>) expressions[0];
        loadCode(sectionNode);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        debug(event, true);
        Delay.addDelayedEvent(event);
        final @Nullable Object locals = Variables.removeLocals(event);
        final @Nullable String pathString = pathExpression.getSingle(event);
        final @Nullable Path path = pathString != null ? Paths.get(pathString) : null;
        BaskTasks.supplyAsync(() -> ReadableFile.open(path)).thenAccept(openedFile -> {
            final TriggerItem autoClose = new AutoClose(openedFile);
            if (super.getNext() != null) {
                autoClose.setNext(super.getNext());
            }
            setNext(autoClose);
            Variables.setLocalVariables(event, locals);
            final @Nullable TriggerItem next = first != null ? first : getNext();
            if (next != null) {
                final @Nullable Object timing = startTiming();
                TriggerItem.walk(next, event);
                stopTiming(timing);
            }
        });
        return null;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "open file " + pathExpression.toString(event, debug) + " for reading";
    }

    private @Nullable Object startTiming() {
        final @Nullable Trigger trigger = getTrigger();
        if (SkriptTimings.enabled() && trigger != null) {
            return SkriptTimings.start(trigger.getDebugLabel());
        }
        return null;
    }

    public void stopTiming(@Nullable Object timing) {
        SkriptTimings.stop(timing);
    }

}
