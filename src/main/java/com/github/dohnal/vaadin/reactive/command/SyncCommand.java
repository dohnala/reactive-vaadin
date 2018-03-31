package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;

/**
 * Synchronous implementation of {@link ReactiveCommand}
 *
 * @param <R> type of command result
 *
 * @author dohnal
 */
public final class SyncCommand<R> extends ReactiveCommand<R>
{
    protected final Supplier<R> execution;

    /**
     * Creates new synchronous reactive command with given execution
     *
     * @param execution execution
     */
    public SyncCommand(final @Nonnull Supplier<R> execution)
    {
        super();

        this.execution = execution;
    }

    @Override
    public final void execute()
    {
        try
        {
            handleResult(execution.get(), null);
        }
        catch (final Throwable error)
        {
            handleResult(null, error);
        }
    }
}
