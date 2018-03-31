package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.Collection;

import com.github.dohnal.vaadin.reactive.binder.command.CommandExecutionBinder;
import com.github.dohnal.vaadin.reactive.binder.observable.ObservableBinder;
import com.github.dohnal.vaadin.reactive.binder.observable.ObservableEnabledBinder;
import com.github.dohnal.vaadin.reactive.binder.observable.ObservableItemsBinder;
import com.github.dohnal.vaadin.reactive.binder.observable.ObservableReadOnlyBinder;
import com.github.dohnal.vaadin.reactive.binder.observable.ObservableTextBinder;
import com.github.dohnal.vaadin.reactive.binder.observable.ObservableValueBinder;
import com.github.dohnal.vaadin.reactive.binder.observable.ObservableVisibleBinder;
import com.github.dohnal.vaadin.reactive.binder.property.PropertyBinder;
import com.github.dohnal.vaadin.reactive.binder.property.PropertyValueBinder;
import rx.Observable;

/**
 * Reactive binder for binding reactive primitives
 *
 * @author dohnal
 */
public abstract class ReactiveBinder implements Disposable<ReactiveBinder>
{
    /**
     * Return binder for given observable
     *
     * @param observable observable
     * @param <T> type of observable
     * @return binder
     */
    @Nonnull
    public static <T> ObservableBinder<T> bind(final @Nonnull Observable<T> observable)
    {
        return new ObservableBinder<>(observable);
    }

    /**
     * Return binder for given property
     *
     * @param property property
     * @param <T> type of property
     * @return binder
     */
    @Nonnull
    public static <T> PropertyBinder<T> bind(final @Nonnull ReactiveProperty<T> property)
    {
        return new PropertyBinder<>(property);
    }

    /**
     * Return text binder for given observable
     *
     * @param observable observable
     * @return binder
     */
    @Nonnull
    public static ObservableTextBinder bindText(final @Nonnull Observable<String> observable)
    {
        return new ObservableTextBinder(observable);
    }

    /**
     * Return text binder for given property
     *
     * @param property property
     * @return binder
     */
    @Nonnull
    public static ObservableTextBinder bindText(final @Nonnull ReactiveProperty<String> property)
    {
        return new ObservableTextBinder(property.asObservable());
    }

    /**
     * Return value binder for given observable
     *
     * @param observable observable
     * @param <T> type of observable
     * @return binder
     */
    @Nonnull
    public static <T> ObservableValueBinder<T> bindValue(final @Nonnull Observable<T> observable)
    {
        return new ObservableValueBinder<>(observable);
    }

    /**
     * Return value binder for given property
     *
     * @param property property
     * @param <T> type of property
     * @return binder
     */
    @Nonnull
    public static <T> PropertyValueBinder<T> bindValue(final @Nonnull ReactiveProperty<T> property)
    {
        return new PropertyValueBinder<>(property);
    }

    /**
     * Return visible binder for given observable
     *
     * @param observable observable
     * @return binder
     */
    @Nonnull
    public static ObservableVisibleBinder bindVisible(final @Nonnull Observable<Boolean> observable)
    {
        return new ObservableVisibleBinder(observable);
    }

    /**
     * Return value binder for given property
     *
     * @param property property
     * @return binder
     */
    @Nonnull
    public static ObservableVisibleBinder bindVisible(final @Nonnull ReactiveProperty<Boolean> property)
    {
        return new ObservableVisibleBinder(property.asObservable());
    }

    /**
     * Return enabled binder for given observable
     *
     * @param observable observable
     * @return binder
     */
    @Nonnull
    public static ObservableEnabledBinder bindEnabled(final @Nonnull Observable<Boolean> observable)
    {
        return new ObservableEnabledBinder(observable);
    }

    /**
     * Return enabled binder for given property
     *
     * @param property property
     * @return binder
     */
    @Nonnull
    public static ObservableEnabledBinder bindEnabled(final @Nonnull ReactiveProperty<Boolean> property)
    {
        return new ObservableEnabledBinder(property.asObservable());
    }

    /**
     * Return read-only binder for given observable
     *
     * @param observable observable
     * @return binder
     */
    @Nonnull
    public static ObservableReadOnlyBinder bindReadOnly(final @Nonnull Observable<Boolean> observable)
    {
        return new ObservableReadOnlyBinder(observable);
    }

    /**
     * Return read-only binder for given property
     *
     * @param property property
     * @return binder
     */
    @Nonnull
    public static ObservableReadOnlyBinder bindReadOnly(final @Nonnull ReactiveProperty<Boolean> property)
    {
        return new ObservableReadOnlyBinder(property.asObservable());
    }

    /**
     * Return items binder for given observable
     *
     * @param observable observable
     * @param <T> type of item
     * @return binder
     */
    @Nonnull
    public static <T> ObservableItemsBinder<T> bindItems(final @Nonnull Observable<Collection<T>> observable)
    {
        return new ObservableItemsBinder<>(observable);
    }

    /**
     * Return items binder for given reactive property
     *
     * @param property reactive property
     * @param <T> type of item
     * @return binder
     */
    @Nonnull
    public static <T> ObservableItemsBinder<T> bindItems(final @Nonnull ReactiveProperty<Collection<T>> property)
    {
        return new ObservableItemsBinder<>(property.asObservable());
    }

    /**
     * Return command execution binder for given reactive command
     *
     * @param command reactive command
     * @param <R> type of command result
     * @return binder
     */
    @Nonnull
    public static <R> CommandExecutionBinder<R> bindCommand(final @Nonnull ReactiveCommand<R> command)
    {
        return new CommandExecutionBinder<>(command);
    }

    /**
     * Return command result binder for given reactive command
     *
     * @param command reactive command
     * @param <R> type of command result
     * @return binder
     */
    @Nonnull
    public static <R> ObservableValueBinder<R> bindCommandResult(final @Nonnull ReactiveCommand<R> command)
    {
        return new ObservableValueBinder<>(command.getResult());
    }

    /**
     * Return command success binder for given reactive command
     *
     * @param command reactive command
     * @param <R> type of command result
     * @return binder
     */
    @Nonnull
    public static <R> ObservableBinder<R> bindCommandSuccess(final @Nonnull ReactiveCommand<R> command)
    {
        return new ObservableBinder<>(command.getResult());
    }

    /**
     * Return command error binder for given reactive command
     *
     * @param command reactive command
     * @param <R> type of command result
     * @return binder
     */
    @Nonnull
    public static <R> ObservableBinder<Throwable> bindCommandError(final @Nonnull ReactiveCommand<R> command)
    {
        return new ObservableBinder<>(command.getError());
    }
}
