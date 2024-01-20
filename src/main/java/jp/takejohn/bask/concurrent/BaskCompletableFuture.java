package jp.takejohn.bask.concurrent;

import jp.takejohn.bask.Bask;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class BaskCompletableFuture<T> extends CompletableFuture<T> {

    private record Pair<T, U>(T t, U u) {}

    private static final Executor defaultExecutor =
            command -> Bukkit.getScheduler().runTaskAsynchronously(Bask.getInstance(), command);

    private static final Executor tickExecutor = command -> Bukkit.getScheduler().runTask(Bask.getInstance(), command);

    @Override
    public <U> CompletableFuture<U> newIncompleteFuture() {
        return new BaskCompletableFuture<>();
    }

    @Override
    public Executor defaultExecutor() {
        return defaultExecutor;
    }

    @Override
    public <U> CompletableFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        return super.thenCompose(t -> {
            final CompletableFuture<U> result = new CompletableFuture<>();
            tickExecutor.execute(() -> result.complete(fn.apply(t)));
            return result;
        });
    }

    @Override
    public CompletableFuture<Void> thenAccept(Consumer<? super T> action) {
        return thenApply(t -> {
            action.accept(t);
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> thenRun(Runnable action) {
        return thenAccept(t -> action.run());
    }

    @Override
    public <U, V> CompletableFuture<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        final @NotNull CompletableFuture<Pair<T, ? extends U>> instant = super.thenCombine(other, Pair::new);
        assert instant instanceof BaskCompletableFuture<Pair<T, ? extends U>>;
        return instant.thenApply(pair -> fn.apply(pair.t, pair.u));
    }

    @Override
    public <U> CompletableFuture<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return thenCombine(other, (t, u) -> {
            action.accept(t, u);
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> runAfterBoth(CompletionStage<?> other, Runnable action) {
        return thenAcceptBoth(other, (t, u) -> action.run());
    }

    @Override
    public <U> CompletableFuture<U> applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        final @NotNull BaskCompletableFuture<? extends T> instant =
                (BaskCompletableFuture<? extends T>) super.applyToEither(other, Function.identity());
        return instant.thenApply(fn);
    }

    @Override
    public CompletableFuture<Void> acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return applyToEither(other, t -> {
            action.accept(t);
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> runAfterEither(CompletionStage<?> other, Runnable action) {
        final @NotNull BaskCompletableFuture<Void> instant =
                (BaskCompletableFuture<Void>) super.runAfterEither(other, () -> {});
        return instant.thenRun(action);
    }

    @Override
    public CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> action) {
        return handle((t, throwable) -> {
            action.accept(t, throwable);
            return t;
        });
    }

    @Override
    public <U> CompletableFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> fn) {
        final @NotNull BaskCompletableFuture<Pair<T, Throwable>> instant =
                (BaskCompletableFuture<Pair<T, Throwable>>) super.handle(Pair::new);
        return instant.thenApply(pair -> fn.apply(pair.t, pair.u));
    }

    @Override
    public CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> fn) {
        return super.exceptionallyCompose(throwable -> {
            final CompletableFuture<T> result = new CompletableFuture<>();
            tickExecutor.execute(() -> result.complete(fn.apply(throwable)));
            return result;
        });
    }

}
