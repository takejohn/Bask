package jp.takejohn.bask.io;

import jp.takejohn.bask.AbstractResource;
import jp.takejohn.bask.annotations.SkriptDoc;
import jp.takejohn.bask.annotations.SkriptType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

@SkriptType(value = "openedfile", user = "openedfiles?")
@SkriptDoc(
        name = "Opened File",
        description = "An opened file.",
        since = "0.1.0"
)
public abstract class OpenedFile extends AbstractResource<IOException> {

    private final @NotNull Path path;

    protected OpenedFile(@NotNull Path path) {
        this.path = path;
    }

    public final @NotNull Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return path.toString();
    }

}
