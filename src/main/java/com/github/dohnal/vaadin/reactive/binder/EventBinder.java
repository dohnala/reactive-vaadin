package com.github.dohnal.vaadin.reactive.binder;

import javax.annotation.Nonnull;
import java.util.List;

import com.github.dohnal.vaadin.reactive.Action;
import com.github.dohnal.vaadin.reactive.Event;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.google.common.collect.Lists;
import rx.Subscription;

/**
 * Implementation of {@link ReactiveBinder} for binding actions as a reaction to {@link Event}
 *
 * @param <T> type of value in the event
 * @author dohnal
 */
public class EventBinder<T> implements ReactiveBinder
{
    private final Event<T> event;

    private List<Subscription> subscriptions;

    public EventBinder(final @Nonnull Event<T> event)
    {
        this.event = event;
        this.subscriptions = Lists.newArrayList();
    }

    /**
     * Binds given action which will be called whenever this event happened
     *
     * @param action action
     * @param <A> type of action
     * @return this binder
     */
    @Nonnull
    public <A extends Action<? super T>> EventBinder<T> then(final @Nonnull A action)
    {
        subscriptions.add(event.asObservable().subscribe(action::call));

        return this;
    }

    @Nonnull
    @Override
    public EventBinder<T> unbind()
    {
        subscriptions.stream()
                .filter(subscription -> !subscription.isUnsubscribed())
                .forEach(Subscription::unsubscribe);

        subscriptions.clear();

        return this;
    }
}
