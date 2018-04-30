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
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import rx.Observable;

/**
 * Synchronous implementation of {@link ReactiveCommand}
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public final class SyncCommand<T, R> extends AbstractCommand<T, R>
{
    protected final Supplier<R> noInputExecution;

    protected final Function<T, R> inputExecution;

    /**
     * Creates new synchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public SyncCommand(final @Nonnull Observable<Boolean> canExecute,
                       final @Nonnull Supplier<R> execution)
    {
        super(canExecute);

        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        this.noInputExecution = execution;
        this.inputExecution = null;
    }

    /**
     * Creates new synchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public SyncCommand(final @Nonnull Observable<Boolean> canExecute,
                       final @Nonnull Function<T, R> execution)
    {
        super(canExecute);

        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        this.noInputExecution = null;
        this.inputExecution = execution;
    }

    @Override
    public final void executeInternal(final @Nullable T input)
    {
        if (input == null && noInputExecution == null)
        {
            throw new IllegalArgumentException("Input is null, but command requires input");
        }

        try
        {
            handleStart();

            if (input == null)
            {
                handleResult(noInputExecution.get(), null);
            }
            else
            {
                handleResult(inputExecution.apply(input), null);
            }

        }
        catch (final Throwable error)
        {
            handleResult(null, error);
        }
        finally
        {
            handleComplete();
        }
    }

    @Override
    protected void handleError(final @Nonnull Throwable throwable)
    {
        Objects.requireNonNull(throwable, "Throwable cannot be null");

        throw new RuntimeException("Unexpected error during command execution", throwable);
    }
}
