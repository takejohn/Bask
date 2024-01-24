package jp.takejohn.bask.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import jp.takejohn.bask.io.Blob;
import jp.takejohn.bask.io.OpenedFile;
import jp.takejohn.bask.io.ReadableFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprBuffer extends SimplePropertyExpression<OpenedFile, Blob> {

    static {
        PropertyExpression.register(ExprBuffer.class, Blob.class, "binary buffer", "openedfile");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed,
                        SkriptParser.@NotNull ParseResult parseResult) {
        if (!super.init(expressions, matchedPattern, isDelayed, parseResult)) {
            return false;
        }
        final Expression<? extends OpenedFile> expr = getExpr();
        if (expr instanceof ExprFile exprFile && !ReadableFile.class.isAssignableFrom(exprFile.getReturnType())) {
            Skript.error("The file is not a readable binary file");
            return false;
        }
        return true;
    }

    @Override
    public @Nullable Blob convert(OpenedFile from) {
        if (from instanceof ReadableFile readableFile) {
            return readableFile.lastlyReadData();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "binary buffer";
    }

    @Override
    public @NotNull Class<? extends Blob> getReturnType() {
        return Blob.class;
    }

}
