package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.binder.ObservableActionBinder;
import com.github.dohnal.vaadin.reactive.binder.ObservablePropertyBinder;
import rx.Observable;

/**
 * Base interface for binders which can bind observables to properties or call actions as a reaction
 * when any observable is changed
 *
 * @author dohnal
 */
public interface ReactiveBinder
{
    /**
     * Creates binder for binding observable of values
     *
     * @param observable observable
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservablePropertyBinder<T> bind(final @Nonnull Observable<T> observable)
    {
        return new ObservablePropertyBinder<>(observable);
    }

    /**
     * Creates binder for binding something what can be observed
     *
     * @param isObservable something what can be observed
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservablePropertyBinder<T> bind(final @Nonnull IsObservable<T> isObservable)
    {
        return new ObservablePropertyBinder<>(isObservable.asObservable());
    }

    /**
     * Creates binder which can call action as a reaction when given observable is changed
     *
     * @param observable observable
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservableActionBinder<T> whenChanged(final @Nonnull Observable<T> observable)
    {
        return new ObservableActionBinder<>(observable);
    }

    /**
     * Creates binder which can call action as a reaction to change of something what can be observed
     *
     * @param isObservable something what can be observed
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservableActionBinder<T> whenChanged(final @Nonnull IsObservable<T> isObservable)
    {
        return whenChanged(isObservable.asObservable());
    }
}
