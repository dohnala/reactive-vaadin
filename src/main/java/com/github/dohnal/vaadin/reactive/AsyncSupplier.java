package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Represents an asynchronous supplier of results
 *
 * @param <T> type of result
 * @author dohnal
 */
public interface AsyncSupplier<T> extends Supplier<CompletableFuture<T>>
{
    /**
     * Creates asynchronous supplier
     *
     * @return asynchronous supplier
     */
    @Nonnull
    static AsyncSupplier<Void> create()
    {
        return create(() -> {
        });
    }

    /**
     * Creates asynchronous supplier from runnable
     *
     * @param runnable runnable
     * @return asynchronous supplier
     */
    @Nonnull
    static AsyncSupplier<Void> create(final @Nonnull Runnable runnable)
    {
        return () -> CompletableFuture.runAsync(runnable);
    }

    /**
     * Creates asynchronous supplier from runnable
     *
     * @param runnable runnable
     * @param executor executor where the runnable will be executed
     * @return asynchronous supplier
     */
    @Nonnull
    static AsyncSupplier<Void> create(final @Nonnull Runnable runnable,
                                      final @Nonnull Executor executor)
    {
        return () -> CompletableFuture.runAsync(runnable, executor);
    }

    /**
     * Creates asynchronous supplier from synchronous supplier
     *
     * @param supplier supplier
     * @param <T> type of result
     * @return asynchronous supplier
     */
    @Nonnull
    static <T> AsyncSupplier<T> create(final @Nonnull Supplier<T> supplier)
    {
        return () -> CompletableFuture.supplyAsync(supplier);
    }

    /**
     * Creates asynchronous supplier from synchronous supplier
     *
     * @param supplier supplier
     * @param executor executor where the supplier will be executed
     * @param <T> type of result
     * @return asynchronous supplier
     */
    @Nonnull
    static <T> AsyncSupplier<T> create(final @Nonnull Supplier<T> supplier,
                                       final @Nonnull Executor executor)
    {
        return () -> CompletableFuture.supplyAsync(supplier, executor);
    }
}
