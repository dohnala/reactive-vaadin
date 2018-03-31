package com.github.dohnal.vaadin.reactive.binder.command;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.vaadin.shared.Registration;

/**
 * Base binder for binding reactive command
 *
 * @param <R> type of command result
 *
 * @author dohnal
 */
public abstract class AbstractCommandBinder<R> extends ReactiveBinder
{
    protected final ReactiveCommand<R> command;

    protected final List<Registration> registrations;

    /**
     * Creates a new command binder for given command
     *
     * @param command reactive command
     */
    public AbstractCommandBinder(final @Nonnull ReactiveCommand<R> command)
    {
        this.command = command;
        this.registrations = new ArrayList<>();
    }

    @Nonnull
    @Override
    public AbstractCommandBinder<R> unbind()
    {
        registrations.forEach(Registration::remove);

        registrations.clear();

        return this;
    }

    /**
     * Adds given Vaadin registration object to this binder
     *
     * @param registration registration
     */
    protected void addRegistration(final @Nonnull Registration registration)
    {
        registrations.add(registration);
    }
}
