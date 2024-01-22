package jp.takejohn.bask.elements.expressions;

import jp.takejohn.bask.elements.sections.SecOpenFile;
import jp.takejohn.bask.io.OpenedFile;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprFile extends SectionExpression<SecOpenFile, OpenedFile> {

    static {
        SectionExpression.register(ExprFile.class, OpenedFile.class, "file");
    }

    @Override
    protected @NotNull Class<SecOpenFile> getSectionType() {
        return SecOpenFile.class;
    }

    @Override
    protected @NotNull String getSectionName() {
        return "open file";
    }

    @Override
    protected @NotNull String getPattern() {
        return "file";
    }

    @Override
    protected @NotNull OpenedFile @Nullable [] get(@NotNull Event event) {
        final @Nullable OpenedFile result = getSection().getResource(event);
        return result != null ? new OpenedFile[]{ result } : null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends OpenedFile> getReturnType() {
        return getSection().getResourceType();
    }

}
