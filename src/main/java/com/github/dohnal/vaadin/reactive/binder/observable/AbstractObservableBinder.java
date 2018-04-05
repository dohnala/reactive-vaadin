package com.github.dohnal.vaadin.reactive.binder.observable;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.binder.AbstractBinder;
import rx.Observable;

/**
 * Base binder for binding observable
 *
 * @param <T> type of observable
 * @author dohnal
 */
public abstract class AbstractObservableBinder<T> extends AbstractBinder
{
    protected final Observable<T> observable;

    /**
     * Creates a new observable binder for observable
     *
     * @param observable observable
     */
    public AbstractObservableBinder(final @Nonnull Observable<T> observable)
    {
        super();

        this.observable = observable;
    }
}
