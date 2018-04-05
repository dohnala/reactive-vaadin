package com.github.dohnal.vaadin.reactive.binder.command;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.binder.AbstractBinder;

/**
 * Base binder for binding reactive command
 *
 * @param <T> type of command input
 * @param <R> type of command result
 * @author dohnal
 */
public abstract class AbstractCommandBinder<T, R> extends AbstractBinder
{
    protected final ReactiveCommand<T, R> command;

    /**
     * Creates a new command binder for given command
     *
     * @param command reactive command
     */
    public AbstractCommandBinder(final @Nonnull ReactiveCommand<T, R> command)
    {
        super();

        this.command = command;
    }
}
