package com.github.dohnal.vaadin.mvvm.binder.observable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import com.github.dohnal.vaadin.mvvm.ReactiveBinder;
import rx.Observable;
import rx.Subscription;

/**
 * @author dohnal
 */
public abstract class AbstractObservableBinder<T> extends ReactiveBinder
{
    protected final Observable<T> observable;

    protected final List<Subscription> subscriptions;

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

    protected void addSubscription(final @Nonnull Subscription subscription)
    {
        subscriptions.add(subscription);
    }
}
