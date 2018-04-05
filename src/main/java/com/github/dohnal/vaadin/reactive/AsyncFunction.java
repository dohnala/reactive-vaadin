package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
     * Creates asynchronous function
     *
     * @return asynchronous function
     */
    static AsyncFunction<Void, Void> create()
    {
        return create(AsyncSupplier.create());
    }

    /**
     * Creates asynchronous function from runnable
     *
     * @param runnable runnable
     * @return asynchronous function
     */
    static AsyncFunction<Void, Void> create(final @Nonnull Runnable runnable)
    {
        return create(AsyncSupplier.create(runnable));
    }

    /**
     * Creates asynchronous function from runnable
     *
     * @param runnable runnable
     * @param executor executor where the runnable will be executed
     * @return asynchronous function
     */
    static AsyncFunction<Void, Void> create(final @Nonnull Runnable runnable,
                                            final @Nonnull Executor executor)
    {
        return create(AsyncSupplier.create(runnable, executor));
    }

    /**
     * Creates asynchronous function from synchronous supplier
     *
     * @param supplier supplier
     * @param <R> type of result
     * @return asynchronous function
     */
    static <R> AsyncFunction<Void, R> create(final @Nonnull Supplier<R> supplier)
    {
        return create(AsyncSupplier.create(supplier));
    }

    /**
     * Creates asynchronous function from synchronous supplier
     *
     * @param supplier supplier
     * @param executor executor where the supplier will be executed
     * @param <R> type of result
     * @return asynchronous function
     */
    static <R> AsyncFunction<Void, R> create(final @Nonnull Supplier<R> supplier,
                                             final @Nonnull Executor executor)
    {
        return create(AsyncSupplier.create(supplier, executor));
    }

    /**
     * Creates asynchronous function from asynchronous supplier
     *
     * @param supplier supplier
     * @param <R> type of result
     * @return asynchronous function
     */
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
    static <T> AsyncFunction<T, Void> create(final @Nonnull Consumer<T> consumer)
    {
        return create(input -> {
            consumer.accept(input);

            return null;
        });
    }

    /**
     * Creates asynchronous function from synchronous consumer
     *
     * @param consumer consumer
     * @param executor executor where the consumer will be executed
     * @param <T> type of input
     * @return asynchronous function
     */
    static <T> AsyncFunction<T, Void> create(final @Nonnull Consumer<T> consumer,
                                             final @Nonnull Executor executor)
    {
        return create(input -> {
            consumer.accept(input);

            return null;
        }, executor);
    }

    /**
     * Creates asynchronous function from synchronous function
     *
     * @param function function
     * @param <T> type of input
     * @param <R> type of result
     * @return asynchronous function
     */
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
    static <T, R> AsyncFunction<T, R> create(final @Nonnull Function<T, R> function,
                                             final @Nonnull Executor executor)
    {
        return input -> CompletableFuture.supplyAsync(() -> function.apply(input), executor);
    }
}
