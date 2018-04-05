package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

import com.github.dohnal.vaadin.reactive.AsyncSupplier;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import rx.Observable;

/**
 * Asynchronous implementation of {@link ReactiveCommand}
 *
 * @param <R> type of command result
 * @author dohnal
 */
public final class AsyncCommand<R> extends AbstractCommand<R>
{
    protected final AsyncSupplier<R> execution;

    protected final Executor executor;

    /**
     * Creates new asynchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public AsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                        final @Nonnull AsyncSupplier<R> execution)
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
    public AsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                        final @Nonnull AsyncSupplier<R> execution,
                        final @Nullable Executor executor)
    {
        super(canExecute);

        this.execution = execution;
        this.executor = executor;
    }

    @Override
    public final void executeInternal()
    {
        this.isExecuting.setValue(true);

        if (executor != null)
        {
            execution.get().whenCompleteAsync((result, error) -> {
                handleResult(result, error);

                this.isExecuting.setValue(false);
            }, executor);
        }
        else
        {
            execution.get().whenCompleteAsync((result, error) -> {
                handleResult(result, error);

                this.isExecuting.setValue(false);
            });
        }
    }
}
