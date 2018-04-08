package com.github.dohnal.vaadin.reactive.observable;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.Event;
import rx.Observable;

/**
 * Event which informs that given observable was changed
 *
 * @param <T> type of values
 * @author dohnal
 */
public final class ObservableChangedEvent<T> implements Event<T>
{
    private final Observable<T> observable;

    public ObservableChangedEvent(final @Nonnull Observable<T> observable)
    {
        this.observable = observable;
    }

    @Nonnull
    @Override
    public final Observable<T> asObservable()
    {
        return observable;
    }
}
