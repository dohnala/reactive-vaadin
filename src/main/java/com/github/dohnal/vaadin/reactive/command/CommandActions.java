package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.dohnal.vaadin.reactive.Action;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;

/**
 * List of all command actions
 *
 * @author dohnal
 */
public interface CommandActions
{
    /**
     * Returns an action which will execute given command without any input parameter
     *
     * @param command command
     * @return action
     */
    @Nonnull
    default Action<Object> execute(final @Nonnull ReactiveCommand<Void, ?> command)
    {
        return value -> command.execute(null);
    }

    /**
     * Returns an action which will execute given command with given input parameter
     *
     * @param command command
     * @param input input parameter to command execution
     * @param <T> type of command input
     * @return action
     */
    @Nonnull
    default <T> Action<Object> executeWithInput(final @Nonnull ReactiveCommand<T, ?> command,
                                                final @Nullable T input)
    {
        return value -> command.execute(input);
    }

    /**
     * Returns an action which will execute given command with value passed to this action as
     * command input parameter
     *
     * @param command command
     * @param <T> input parameter to command execution
     * @return action
     */
    @Nonnull
    default <T> Action<T> executeWithInput(final @Nonnull ReactiveCommand<T, ?> command)
    {
        return command::execute;
    }
}
