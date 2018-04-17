package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletionException;

import com.github.dohnal.vaadin.reactive.AsyncProgressFunction;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import rx.Observable;

/**
 * Asynchronous implementation of {@link ReactiveCommand} with support of controlling progress
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public final class ProgressCommand<T, R> extends AbstractCommand<T, R>
{
    protected final AsyncProgressFunction<T, R> execution;

    /**
     * Creates new asynchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public ProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                           final @Nonnull AsyncProgressFunction<T, R> execution)
    {
        super(canExecute);

        this.execution = execution;
    }

    @Override
    public final void executeInternal(final @Nullable T input)
    {
        handleStart();

        execution.apply(new ReactiveProgress(progress), input)
                .handle((result, error) -> {
                    if (error instanceof CompletionException)
                    {
                        handleResult(result, error.getCause());
                    }
                    else
                    {
                        handleResult(result, error);
                    }

                    return result;
                })
                .whenComplete((result, error) -> handleComplete());
    }
}
