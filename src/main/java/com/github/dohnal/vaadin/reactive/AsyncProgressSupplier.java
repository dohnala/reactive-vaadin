package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents an asynchronous supplier of results which takes progress
 *
 * @param <T> type of result
 * @author dohnal
 */
public interface AsyncProgressSupplier<T> extends Function<Progress, CompletableFuture<T>>
{
    /**
     * Creates asynchronous progress supplier from consumer
     *
     * @param consumer consumer
     * @return asynchronous progress supplier
     */
    @Nonnull
    static AsyncProgressSupplier<Void> create(final @Nonnull Consumer<Progress> consumer)
    {
        return progress -> CompletableFuture.runAsync(() -> consumer.accept(progress));
    }

    /**
     * Creates asynchronous progress supplier from consumer
     *
     * @param consumer consumer
     * @param executor executor where the consumer will be executed
     * @return asynchronous progress supplier
     */
    @Nonnull
    static AsyncProgressSupplier<Void> create(final @Nonnull Consumer<Progress> consumer,
                                              final @Nonnull Executor executor)
    {
        return progress -> CompletableFuture.runAsync(() -> consumer.accept(progress), executor);
    }

    /**
     * Creates asynchronous progress supplier from function
     *
     * @param function function
     * @param <T> type of result
     * @return asynchronous progress supplier
     */
    @Nonnull
    static <T> AsyncProgressSupplier<T> create(final @Nonnull Function<Progress, T> function)
    {
        return progress -> CompletableFuture.supplyAsync(() -> function.apply(progress));
    }

    /**
     * Creates asynchronous progress supplier from function
     *
     * @param function function
     * @param executor executor where the consumer will be executed
     * @param <T> type of result
     * @return asynchronous progress supplier
     */
    @Nonnull
    static <T> AsyncProgressSupplier<T> create(final @Nonnull Function<Progress, T> function,
                                               final @Nonnull Executor executor)
    {
        return progress -> CompletableFuture.supplyAsync(() -> function.apply(progress), executor);
    }
}
