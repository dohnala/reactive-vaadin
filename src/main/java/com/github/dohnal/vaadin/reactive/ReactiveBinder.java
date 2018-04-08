package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.binder.EventBinder;
import com.github.dohnal.vaadin.reactive.binder.ObservableBinder;
import com.github.dohnal.vaadin.reactive.observable.ObservableChangedEvent;
import rx.Observable;

/**
 * Base interface for binders which can bind observables to properties or call actions as a reaction
 * to events that happened
 *
 * @author dohnal
 */
public interface ReactiveBinder extends Disposable<ReactiveBinder>
{
    /**
     * Creates binder for binding observable of values
     *
     * @param observable observable
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservableBinder<T> bind(final @Nonnull Observable<T> observable)
    {
        return new ObservableBinder<>(observable);
    }

    /**
     * Creates binder for binding something what can be observed
     *
     * @param isObservable something what can be observed
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservableBinder<T> bind(final @Nonnull IsObservable<T> isObservable)
    {
        return new ObservableBinder<>(isObservable.asObservable());
    }

    /**
     * Creates binder which can call action as a reaction to change of given observable
     *
     * @param observable observable
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> EventBinder<T> whenChanged(final @Nonnull Observable<T> observable)
    {
        return when(new ObservableChangedEvent<>(observable));
    }

    /**
     * Creates binder which can call action as a reaction to change of something what can be observed
     *
     * @param isObservable something what can be observed
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> EventBinder<T> whenChanged(final @Nonnull IsObservable<T> isObservable)
    {
        return whenChanged(isObservable.asObservable());
    }

    /**
     * Creates binder which can call action as a reaction to some event
     *
     * @param event event
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> EventBinder<T> when(final @Nonnull Event<T> event)
    {
        return new EventBinder<>(event);
    }
}
