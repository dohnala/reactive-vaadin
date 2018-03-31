package com.github.dohnal.vaadin.reactive.binder.command;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.vaadin.ui.Button;
import rx.Observable;

/**
 * Binder for binging command execution
 *
 * @param <R> type of command result
 *
 * @author dohnal
 */
public final class CommandExecutionBinder<R> extends AbstractCommandBinder<R>
{
    public CommandExecutionBinder(final @Nonnull ReactiveCommand<R> command)
    {
        super(command);
    }

    /**
     * Binds command execution to click event of given button
     *
     * @param button button
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<R> to(final @Nonnull Button button)
    {
        addRegistration(button.addClickListener(event -> command.execute()));

        return this;
    }

    /**
     * Binds command execution to given observable, which means that whenever given observable emits
     * value, command will be executed
     *
     * @param observable observable
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<R> to(final @Nonnull Observable<?> observable)
    {
        addSubscription(observable.subscribe(value -> command.execute()));

        return this;
    }

    /**
     * Binds command execution to given reactive property, which means that whenever given
     * reactive property changes value, command will be executed
     *
     * @param property property
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<R> to(final @Nonnull ReactiveProperty<?> property)
    {
        return to(property.asObservable());
    }

    @Nonnull
    @Override
    public CommandExecutionBinder<R> unbind()
    {
        super.unbind();

        return this;
    }
}
