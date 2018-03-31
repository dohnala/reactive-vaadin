package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;

/**
 * Asynchronous implementation of {@link ReactiveCommand}
 *
 * @param <R> type of command result
 *
 * @author dohnal
 */
public class AsyncCommand<R> extends ReactiveCommand<R>
{
    protected final AsyncSupplier<R> execution;

    protected final Executor executor;

    /**
     * Creates new asynchronous reactive command with given execution
     *
     * @param execution execution
     */
    public AsyncCommand(final @Nonnull AsyncSupplier<R> execution)
    {
        this(execution, null);
    }

    /**
     * Creates new asynchronous reactive command with given execution and executor
     *
     * @param execution execution
     * @param executor executor
     */
    public AsyncCommand(final @Nonnull AsyncSupplier<R> execution,
                        final @Nullable Executor executor)
    {
        super();

        this.execution = execution;
        this.executor = executor;
    }

    @Override
    public final void execute()
    {
        if (executor != null)
        {
            execution.get().whenCompleteAsync(this::handleResult, executor);
        }
        else
        {
            execution.get().whenCompleteAsync(this::handleResult);
        }
    }
}
