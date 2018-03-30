package com.github.dohnal.vaadin.mvvm.command;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.mvvm.ReactiveCommand;

/**
 * Synchronous implementation of {@link ReactiveCommand}
 *
 * @param <R> type of command result
 *
 * @author dohnal
 */
public final class SyncCommand<R> extends ReactiveCommand<R>
{
    /**
     * Creates new synchronous reactive command with given execution
     *
     * @param execution execution
     */
    public SyncCommand(final @Nonnull Supplier<R> execution)
    {
        super(execution);
    }

    @Override
    public void execute()
    {
        value.setValue(execution.get());
    }
}
