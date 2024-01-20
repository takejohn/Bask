package jp.takejohn.bask.concurrent;

import jp.takejohn.bask.Bask;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class BaskTasks {

    private BaskTasks() {
        throw new AssertionError();
    }

    public static void runAsync(Runnable runnable) {
        supplyAsync(() -> {
            runnable.run();
            return null;
        });
    }

    public static <T> @NotNull CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        final @NotNull CompletableFuture<T> future = new BaskCompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(Bask.getInstance(), () -> {
            final T result = supplier.get();
            Bukkit.getScheduler().runTask(Bask.getInstance(), () -> future.complete(result));
        });
        return future;
    }

}
