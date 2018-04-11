package com.github.dohnal.vaadin.reactive.binder;

import javax.annotation.Nonnull;
import java.util.List;

import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.Property;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.google.common.collect.Lists;
import rx.Observable;
import rx.Subscription;

/**
 * Implementation of {@link ReactiveBinder} for binding {@link Observable} to properties
 *
 * @param <T> type of value in the observable
 * @author dohnal
 */
public class ObservablePropertyBinder<T> implements Disposable<ObservablePropertyBinder<T>>
{
    private final Observable<T> observable;

    private List<Subscription> subscriptions;

    public ObservablePropertyBinder(final @Nonnull Observable<T> observable)
    {
        this.observable = observable;
        this.subscriptions = Lists.newArrayList();
    }

    /**
     * Binds this observable to given property
     *
     * @param property property
     * @param <P> type of property
     * @return this binder
     */
    @Nonnull
    public <P extends Property<? super T>> ObservablePropertyBinder<T> to(final @Nonnull P property)
    {
        subscriptions.add(observable.subscribe(property::setValue));

        return this;
    }

    @Nonnull
    @Override
    public ObservablePropertyBinder<T> unbind()
    {
        subscriptions.stream()
                .filter(subscription -> !subscription.isUnsubscribed())
                .forEach(Subscription::unsubscribe);

        subscriptions.clear();

        return this;
    }
}
