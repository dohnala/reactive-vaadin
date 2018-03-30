package com.github.dohnal.vaadin.mvvm.binder.observable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import com.github.dohnal.vaadin.mvvm.ReactiveBinder;
import rx.Observable;
import rx.Subscription;

/**
 * Base binder for binding observable
 *
 * @param <T> type of observable
 *
 * @author dohnal
 */
public abstract class AbstractObservableBinder<T> extends ReactiveBinder
{
    protected final Observable<T> observable;

    protected final List<Subscription> subscriptions;

    /**
     * Creates a new observable binder for observable
     *
     * @param observable observable
     */
    public AbstractObservableBinder(final @Nonnull Observable<T> observable)
    {
        this.observable = observable;
        this.subscriptions = new ArrayList<>();
    }

    @Nonnull
    @Override
    public AbstractObservableBinder<T> unbind()
    {
        subscriptions.stream()
                .filter(Subscription::isUnsubscribed)
                .forEach(Subscription::unsubscribe);

        subscriptions.clear();

        return this;
    }

    /**
     * Adds given observable subscription object to this binder
     *
     * @param subscription subscription
     */
    protected void addSubscription(final @Nonnull Subscription subscription)
    {
        subscriptions.add(subscription);
    }
}
