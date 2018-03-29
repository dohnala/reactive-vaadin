package com.github.dohnal.vaadin.mvvm.binder.observable;

import javax.annotation.Nonnull;

import rx.Observable;

/**
 * @author dohnal
 */
public abstract class AbstractObservableBinder<T>
{
    protected final Observable<T> observable;

    public AbstractObservableBinder(final @Nonnull Observable<T> observable)
    {
        this.observable = observable;
    }
}
