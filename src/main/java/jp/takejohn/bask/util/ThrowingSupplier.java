package jp.takejohn.bask.util;

@FunctionalInterface
public interface ThrowingSupplier<V, E extends Exception> {

    V get() throws E;

}
