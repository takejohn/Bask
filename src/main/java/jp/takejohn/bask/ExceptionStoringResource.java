package jp.takejohn.bask;

import org.jetbrains.annotations.Nullable;

/**
 * An {@link AutoCloseable} storing exceptions instead of throwing them.
 */
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
