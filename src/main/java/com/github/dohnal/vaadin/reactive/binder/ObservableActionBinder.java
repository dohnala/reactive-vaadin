package com.github.dohnal.vaadin.reactive.binder;

import javax.annotation.Nonnull;
import java.util.List;

import com.github.dohnal.vaadin.reactive.Action;
import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.google.common.collect.Lists;
import rx.Observable;
import rx.Subscription;

/**
 * Implementation of {@link ReactiveBinder} for binding actions as a reaction to observable changes
 *
 * @param <T> type of value in the observabley
 * @author dohnal
 */
public class ObservableActionBinder<T> implements Disposable<ObservableActionBinder<T>>
{
    private final Observable<T> observable;

    private List<Subscription> subscriptions;

    public ObservableActionBinder(final @Nonnull Observable<T> observable)
    {
        this.observable = observable;
        this.subscriptions = Lists.newArrayList();
    }

    /**
     * Binds given action which will be called whenever this observable is changed
     *
     * @param action action
     * @param <A> type of action
     * @return this binder
     */
    @Nonnull
    public <A extends Action<? super T>> ObservableActionBinder<T> then(final @Nonnull A action)
    {
        subscriptions.add(observable.subscribe(action::call));

        return this;
    }

    @Nonnull
    @Override
    public ObservableActionBinder<T> unbind()
    {
        subscriptions.stream()
                .filter(subscription -> !subscription.isUnsubscribed())
                .forEach(Subscription::unsubscribe);

        subscriptions.clear();

        return this;
    }
}
