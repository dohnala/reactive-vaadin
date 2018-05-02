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

package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.github.dohnal.vaadin.reactive.AsyncFunction;
import com.github.dohnal.vaadin.reactive.AsyncSupplier;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Asynchronous implementation of {@link ReactiveCommand}
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public final class AsyncCommand<T, R> extends AbstractCommand<T, R>
{
    protected final AsyncSupplier<R> noInputExecution;

    protected final AsyncFunction<T, R> inputExecution;

    /**
     * Creates new asynchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public AsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                        final @Nonnull AsyncSupplier<R> execution)
    {
        super(canExecute);

        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        this.noInputExecution = execution;
        this.inputExecution = null;
    }

    /**
     * Creates new asynchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public AsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                        final @Nonnull AsyncFunction<T, R> execution)
    {
        super(canExecute);

        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        this.noInputExecution = null;
        this.inputExecution = execution;
    }

    @Override
    public final Completable executeInternal(final @Nullable T input)
    {
        if (input == null && noInputExecution == null)
        {
            throw new IllegalArgumentException("Command requires input");
        }

        return Completable.fromFuture(
                startExecution(input)
                        .handle(this::handleExecution)
                        .whenComplete((result, error) -> handleComplete()));
    }

    @Nonnull
    private CompletableFuture<R> startExecution(final @Nullable T input)
    {
        handleStart();

        return input == null ? noInputExecution.get() : inputExecution.apply(input);
    }

    @Nullable
    private R handleExecution(final @Nullable R result, final @Nonnull Throwable error)
    {
        if (error instanceof CompletionException)
        {
            handleResult(result, error.getCause());
        }
        else
        {
            handleResult(result, error);
        }

        return result;
    }
}
