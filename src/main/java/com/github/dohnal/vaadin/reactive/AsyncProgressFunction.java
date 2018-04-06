package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Represents an asynchronous progress function
 *
 * @param <T> type of input
 * @param <R> type of result
 * @author dohnal
 */
public interface AsyncProgressFunction<T, R> extends BiFunction<Progress, T, CompletableFuture<R>>
{
    /**
     * Creates asynchronous progress function from asynchronous progress supplier
     *
     * @param supplier supplier
     * @param <R> type of result
     * @return asynchronous progress function
     */
    @Nonnull
    static <R> AsyncProgressFunction<Void, R> create(final @Nonnull AsyncProgressSupplier<R> supplier)
    {
        return (progress, input) -> supplier.apply(progress);
    }

    /**
     * Creates asynchronous progress function from consumer
     *
     * @param consumer consumer
     * @param <T> type of input
     * @return asynchronous progress function
     */
    @Nonnull
    static <T> AsyncProgressFunction<T, Void> create(final @Nonnull BiConsumer<Progress, T> consumer)
    {
        return (progress, input) -> CompletableFuture.runAsync(() -> consumer.accept(progress, input));
    }

    /**
     * Creates asynchronous progress function from consumer
     *
     * @param consumer consumer
     * @param executor executor where the consumer will be executed
     * @param <T> type of input
     * @return asynchronous progress function
     */
    @Nonnull
    static <T> AsyncProgressFunction<T, Void> create(final @Nonnull BiConsumer<Progress, T> consumer,
                                                     final @Nonnull Executor executor)
    {
        return (progress, input) -> CompletableFuture.runAsync(() -> consumer.accept(progress, input), executor);
    }

    /**
     * Creates asynchronous progress function from function
     *
     * @param function function
     * @param <T> type of input
     * @param <R> type of result
     * @return asynchronous progress function
     */
    @Nonnull
    static <T, R> AsyncProgressFunction<T, R> create(final @Nonnull BiFunction<Progress, T, R> function)
    {
        return (progress, input) -> CompletableFuture.supplyAsync(() -> function.apply(progress, input));
    }

    /**
     * Creates asynchronous progress function from function
     *
     * @param function function
     * @param executor executor where the consumer will be executed
     * @param <T> type of input
     * @param <R> type of result
     * @return asynchronous progress function
     */
    @Nonnull
    static <T, R> AsyncProgressFunction<T, R> create(final @Nonnull BiFunction<Progress, T, R> function,
                                                     final @Nonnull Executor executor)
    {
        return (progress, input) -> CompletableFuture.supplyAsync(() -> function.apply(progress, input), executor);
    }
}
