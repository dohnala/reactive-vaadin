package com.github.dohnal.vaadin.reactive.binder.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.vaadin.ui.Button;
import rx.Observable;

/**
 * Binder for binging execution for commands with input
 *
 * @param <T> type of command input
 * @param <R> type of command result
 * @author dohnal
 */
public final class CommandExecutionBinder<T, R> extends AbstractCommandBinder<T, R>
{
    public CommandExecutionBinder(final @Nonnull ReactiveCommand<T, R> command)
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
    public final CommandExecutionBinder<T, R> to(final @Nonnull Button button)
    {
        return to(button, (T) null);
    }

    /**
     * Binds command execution to click event of given button and provides input to command
     *
     * @param button button
     * @param input input to command
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<T, R> to(final @Nonnull Button button,
                                                 final @Nullable T input)
    {
        addRegistration(button.addClickListener(event -> command.execute(input)));

        addDisposable(ReactiveBinder.bindEnabled(command.canExecute()).to(button));

        return this;
    }

    /**
     * Binds command execution to click event of given button and provides supplier of input to command
     *
     * @param button button
     * @param input supplier of input to command
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<T, R> to(final @Nonnull Button button,
                                                 final @Nonnull Supplier<T> input)
    {
        return to(button, input.get());
    }

    /**
     * Binds command execution to given observable, which means that whenever given observable emits
     * value, command will be executed with given input
     * NOTE: observable values are ignored
     *
     * @param observable observable
     * @param input input to command
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<T, R> to(final @Nonnull Observable<?> observable,
                                                 final @Nullable T input)
    {
        addSubscription(observable.subscribe(value -> command.execute(input)));

        return this;
    }

    /**
     * Binds command execution to given observable, which means that whenever given observable emits
     * value, command will be executed with input provided by given supplier
     * NOTE: observable values are ignored
     *
     * @param observable observable
     * @param input supplier of input to command
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<T, R> to(final @Nonnull Observable<?> observable,
                                                 final @Nonnull Supplier<T> input)
    {
        return to(observable, input.get());
    }

    /**
     * Binds command execution to given observable, which means that whenever given observable emits
     * value, command will be executed directly with that value
     *
     * @param observable observable
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<T, R> to(final @Nonnull Observable<T> observable)
    {
        addSubscription(observable.subscribe(command::execute));

        return this;
    }

    /**
     * Binds command execution to given reactive property, which means that whenever given
     * reactive property changes value, command will be executed with given input
     * NOTE: reactive property values are ignored
     *
     * @param property property
     * @param input input to command
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<T, R> to(final @Nonnull ReactiveProperty<?> property,
                                                 final @Nullable T input)
    {
        return to(property.asObservable(), input);
    }

    /**
     * Binds command execution to given reactive property, which means that whenever given
     * reactive property changes value, command will be executed with input provided by given supplier
     * NOTE: reactive property values are ignored
     *
     * @param property property
     * @param input supplier of input to command
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<T, R> to(final @Nonnull ReactiveProperty<?> property,
                                                 final @Nonnull Supplier<T> input)
    {
        return to(property.asObservable(), input.get());
    }

    /**
     * Binds command execution to given reactive property, which means that whenever given
     * reactive property changes value, command will be executed directly with that value
     *
     * @param property property
     * @return this binder
     */
    @Nonnull
    public final CommandExecutionBinder<T, R> to(final @Nonnull ReactiveProperty<T> property)
    {
        return to(property.asObservable());
    }

    @Nonnull
    @Override
    public final CommandExecutionBinder<T, R> unbind()
    {
        super.unbind();

        return this;
    }
}
