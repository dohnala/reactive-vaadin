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
public interface AsyncProgressSupplier<T> extends Function<ProgressContext, CompletableFuture<T>>
{
    /**
     * Creates asynchronous progress supplier from consumer
     *
     * @param consumer consumer
     * @return asynchronous progress supplier
     */
    @Nonnull
    static AsyncProgressSupplier<Void> create(final @Nonnull Consumer<ProgressContext> consumer)
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
    static AsyncProgressSupplier<Void> create(final @Nonnull Consumer<ProgressContext> consumer,
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
    static <T> AsyncProgressSupplier<T> create(final @Nonnull Function<ProgressContext, T> function)
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
    static <T> AsyncProgressSupplier<T> create(final @Nonnull Function<ProgressContext, T> function,
                                               final @Nonnull Executor executor)
    {
        return progress -> CompletableFuture.supplyAsync(() -> function.apply(progress), executor);
    }
}
