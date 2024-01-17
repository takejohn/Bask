package jp.takejohn.bask;

import jp.takejohn.bask.util.ThrowingRunnable;
import jp.takejohn.bask.util.ThrowingSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An abstract class implementing {@link ExceptionStoringResource}
 * @param <E> the type of exception
 */
public abstract class AbstractResource<E extends Exception> implements ExceptionStoringResource {

    private @Nullable E lastException;

    /**
     * Stores an exception
     * @param exception the lastly caught exception
     */
    protected void storeException(@NotNull E exception) {
        lastException = exception;
    }

    @SuppressWarnings("unchecked")
    protected <R> R execute(@NotNull ThrowingSupplier<R, ? extends E> throwingSupplier) {
        try {
            return throwingSupplier.get();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            storeException((E) e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected void execute(@NotNull ThrowingRunnable<? extends E> throwingRunnable) {
        try {
            throwingRunnable.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            storeException((E) e);
        }
    }

    @Override
    public @Nullable E getLastException() {
        return lastException;
    }

    @Override
    public void removeException() {
        lastException = null;
    }

}
