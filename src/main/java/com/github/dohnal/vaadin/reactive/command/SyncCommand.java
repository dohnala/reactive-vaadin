package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import rx.Observable;

/**
 * Synchronous implementation of {@link ReactiveCommand}
 *
 * @param <R> type of command result
 * @author dohnal
 */
public final class SyncCommand<R> extends AbstractCommand<R>
{
    protected final Supplier<R> execution;

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

        this.execution = execution;
    }

    @Override
    public final void executeInternal()
    {
        try
        {
            this.isExecuting.setValue(true);

            handleResult(execution.get(), null);
        }
        catch (final Throwable error)
        {
            handleResult(null, error);
        } finally
        {
            this.isExecuting.setValue(false);
        }
    }
}
