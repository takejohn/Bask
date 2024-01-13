package jp.takejohn.bask;

import jp.takejohn.bask.annotations.SkriptDoc;
import jp.takejohn.bask.annotations.SkriptType;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link AutoCloseable} storing exceptions instead of throwing them.
 */
@SkriptType(value = "resource", user = "resources?")
@SkriptDoc(name = "Resource",
        description = "Represents a resource available in 'with' section",
        since = "0.1.0")
public interface ExceptionStoringResource extends AutoCloseable {

    /**
     * Gets the lastly caught exception.
     * @return the lastly caught exception
     */
    @Nullable Exception getLastException();

    /**
     * Removes the stored exception.
     */
    void removeException();

    @Override
    void close();

}
