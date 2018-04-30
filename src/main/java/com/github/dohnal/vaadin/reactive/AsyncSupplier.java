/*
 * Copyright (c) 2018-present, reactive-mvvm Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.Objects;
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
        return create(() -> {});
    }

    /**
     * Creates asynchronous supplier
     *
     * @return asynchronous supplier
     */
    @Nonnull
    static AsyncSupplier<Void> create(final @Nonnull Executor executor)
    {
        Objects.requireNonNull(executor, "Executor cannot be null");

        return create(() -> {}, executor);
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
        Objects.requireNonNull(runnable, "Runnable cannot be null");

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
        Objects.requireNonNull(runnable, "Runnable cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

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
        Objects.requireNonNull(supplier, "Supplier cannot be null");

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
        Objects.requireNonNull(supplier, "Supplier cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return () -> CompletableFuture.supplyAsync(supplier, executor);
    }
}
