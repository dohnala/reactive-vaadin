package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

import com.github.dohnal.vaadin.reactive.AsyncProgressFunction;
import com.github.dohnal.vaadin.reactive.Progress;
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

    protected final Executor executor;

    /**
     * Creates new asynchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public ProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                           final @Nonnull AsyncProgressFunction<T, R> execution)
    {
        this(canExecute, execution, null);
    }

    /**
     * Creates new asynchronous reactive command with given execution and executor
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     * @param executor executor
     */
    public ProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                           final @Nonnull AsyncProgressFunction<T, R> execution,
                           final @Nullable Executor executor)
    {
        super(canExecute);

        this.execution = execution;
        this.executor = executor;
    }

    @Override
    public final void executeInternal(final @Nullable T input)
    {
        this.progress.setValue(0.0f);
        this.isExecuting.setValue(true);

        if (executor != null)
        {
            execution.apply(createProgress(), input).whenCompleteAsync((result, error) -> {
                handleResult(result, error);

                this.progress.setValue(1.0f);
                this.isExecuting.setValue(false);
                this.progress.setValue(0.0f);
                this.executionCount.updateValue(count -> count + 1);
            }, executor);
        }
        else
        {
            execution.apply(createProgress(), input).whenCompleteAsync((result, error) -> {
                handleResult(result, error);

                this.progress.setValue(1.0f);
                this.isExecuting.setValue(false);
                this.executionCount.updateValue(count -> count + 1);
            });
        }
    }

    @Nonnull
    private Progress createProgress()
    {
        return new Progress()
        {
            @Override
            public void set(float value)
            {
                progress.setValue(value);
            }

            @Override
            public void add(float value)
            {
                progress.updateValue(current -> current + value);
            }
        };
    }
}
