package jp.takejohn.bask.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import jp.takejohn.bask.elements.expressions.ExprFile;
import jp.takejohn.bask.io.DataSize;
import jp.takejohn.bask.io.OpenedFile;
import jp.takejohn.bask.io.ReadableBinaryFile;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffRead extends AsyncEffect {

    static {
        Skript.registerEffect(EffRead.class, "read %datasize% from %openedfile%");
    }

    private Expression<DataSize> dataSizeExpression;

    private Expression<OpenedFile> openedFileExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed,
                        SkriptParser.@NotNull ParseResult parseResult) {
        ParserInstance.get().setHasDelayBefore(Kleenean.TRUE);
        dataSizeExpression = (Expression<DataSize>) expressions[0];
        openedFileExpression = (Expression<OpenedFile>) expressions[1];
        if (openedFileExpression instanceof ExprFile exprFile && !ReadableBinaryFile.class.isAssignableFrom(exprFile.getReturnType())) {
                Skript.error("The file is not for reading");
                return false;
        }
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        final @Nullable OpenedFile openedFile = openedFileExpression.getSingle(event);
        if (!(openedFile instanceof ReadableBinaryFile readableBinaryFile)) {
            return getNext();
        }
        final @Nullable DataSize dataSize = dataSizeExpression.getSingle(event);
        if (dataSize == null || readableBinaryFile.availableBytes() >= dataSize.asBytes()) {
            readableBinaryFile.read(dataSize);
            return getNext();
        }
        return super.walk(event);
    }

    @Override
    protected void execute(@NotNull Event event) {
        final @Nullable OpenedFile openedFile = openedFileExpression.getSingle(event);
        if (openedFile instanceof ReadableBinaryFile readableBinaryFile) {
            readableBinaryFile.read(dataSizeExpression.getSingle(event));
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "read " + dataSizeExpression.toString(event, debug) + " from " + openedFileExpression.toString(event, debug);
    }

}
