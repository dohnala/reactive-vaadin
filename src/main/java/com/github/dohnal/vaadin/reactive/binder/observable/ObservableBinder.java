package com.github.dohnal.vaadin.reactive.binder.observable;


import javax.annotation.Nonnull;
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import rx.Observable;

/**
 * Binder for binding observable
 *
 * @param <T> type of observable
 *
 * @author dohnal
 */
public final class ObservableBinder<T> extends AbstractObservableBinder<T>
{
    public ObservableBinder(final @Nonnull Observable<T> observable)
    {
        super(observable);
    }

    /**
     * Binds observable to given reactive property
     *
     * @param property reactive property
     * @return this binder
     */
    @Nonnull
    public final ObservableBinder<T> to(final @Nonnull ReactiveProperty<T> property)
    {
        addSubscription(observable.subscribe(property::setValue));

        return this;
    }

    /**
     * Binds observable to given consumer
     *
     * @param consumer consumer
     * @return this binder
     */
    @Nonnull
    public final ObservableBinder<T> to(final @Nonnull Consumer<T> consumer)
    {
        addSubscription(observable.subscribe(consumer::accept));

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
