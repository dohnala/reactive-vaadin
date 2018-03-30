package com.github.dohnal.vaadin.mvvm.binder.command;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.ReactiveCommand;
import com.vaadin.ui.Button;

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

    @Nonnull
    @Override
    public CommandExecutionBinder<R> unbind()
    {
        super.unbind();

        return this;
    }
}
