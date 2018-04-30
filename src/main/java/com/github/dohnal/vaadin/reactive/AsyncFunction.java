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
     * Creates asynchronous function from synchronous consumer
     *
     * @param consumer consumer
     * @param <T> type of input
     * @return asynchronous function
     */
    @Nonnull
    static <T> AsyncFunction<T, Void> create(final @Nonnull Consumer<T> consumer)
    {
        Objects.requireNonNull(consumer, "Consumer cannot be null");

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
        Objects.requireNonNull(consumer, "Consumer cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

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
        Objects.requireNonNull(function, "Function cannot be null");

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
        Objects.requireNonNull(function, "Function cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return input -> CompletableFuture.supplyAsync(() -> function.apply(input), executor);
    }
}
