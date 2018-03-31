package com.github.dohnal.vaadin.reactive.binder.command;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.binder.AbstractBinder;

/**
 * Base binder for binding reactive command
 *
 * @param <R> type of command result
 *
 * @author dohnal
 */
public abstract class AbstractCommandBinder<R> extends AbstractBinder
{
    protected final ReactiveCommand<R> command;

    /**
     * Creates a new command binder for given command
     *
     * @param command reactive command
     */
    public AbstractCommandBinder(final @Nonnull ReactiveCommand<R> command)
    {
        super();

        this.command = command;
    }
}
