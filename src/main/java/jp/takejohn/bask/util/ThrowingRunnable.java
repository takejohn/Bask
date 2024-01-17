package jp.takejohn.bask.util;

@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> {

    void run() throws E;

}
