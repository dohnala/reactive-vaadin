package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents an asynchronous function
 *
 * @param <T> type of input
 * @param <R> type of result
 * @author dohnal
 */
public interface AsyncFunction<T, R> extends Function<T, CompletableFuture<R>>
{
    /**
     * Creates asynchronous function from asynchronous supplier
     *
     * @param supplier supplier
     * @param <R> type of result
     * @return asynchronous function
     */
    @Nonnull
    static <R> AsyncFunction<Void, R> create(final @Nonnull AsyncSupplier<R> supplier)
    {
        return input -> supplier.get();
    }

    /**
     * Creates asynchronous function from synchronous consumer
     *
     * @param consumer consumer
     * @param <T> type of input
     * @return asynchronous function
     */
    @Nonnull
    static <T> AsyncFunction<T, Void> create(final @Nonnull Consumer<T> consumer)
    {
        return input -> CompletableFuture.runAsync(() -> consumer.accept(input));
    }

    /**
     * Creates asynchronous function from synchronous consumer
     *
     * @param consumer consumer
     * @param executor executor where the consumer will be executed
     * @param <T> type of input
     * @return asynchronous function
     */
    @Nonnull
    static <T> AsyncFunction<T, Void> create(final @Nonnull Consumer<T> consumer,
                                             final @Nonnull Executor executor)
    {
        return input -> CompletableFuture.runAsync(() -> consumer.accept(input), executor);
    }

    /**
     * Creates asynchronous function from synchronous function
     *
     * @param function function
     * @param <T> type of input
     * @param <R> type of result
     * @return asynchronous function
     */
    @Nonnull
    static <T, R> AsyncFunction<T, R> create(final @Nonnull Function<T, R> function)
    {
        return input -> CompletableFuture.supplyAsync(() -> function.apply(input));
    }

    /**
     * Creates asynchronous function from synchronous function
     *
     * @param function function
     * @param executor executor where the function will be executed
     * @param <T> type of input
     * @param <R> type of result
     * @return asynchronous function
     */
    @Nonnull
    static <T, R> AsyncFunction<T, R> create(final @Nonnull Function<T, R> function,
                                             final @Nonnull Executor executor)
    {
        return input -> CompletableFuture.supplyAsync(() -> function.apply(input), executor);
    }
}
