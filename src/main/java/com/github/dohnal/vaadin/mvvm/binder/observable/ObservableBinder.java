package com.github.dohnal.vaadin.mvvm.binder.observable;


import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.ReactiveProperty;
import rx.Observable;

/**
 * @author dohnal
 */
public final class ObservableBinder<T> extends AbstractObservableBinder<T>
{
    public ObservableBinder(final @Nonnull Observable<T> observable)
    {
        super(observable);
    }

    @Nonnull
    public final ObservableBinder<T> to(final @Nonnull ReactiveProperty<T> property)
    {
        addSubscription(observable.subscribe(property::setValue));

        return this;
    }

    @Nonnull
    @Override
    public final ObservableBinder<T> unbind()
    {
        super.unbind();

        return this;
    }
}
