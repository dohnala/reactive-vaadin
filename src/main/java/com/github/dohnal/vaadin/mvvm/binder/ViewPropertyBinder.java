package com.github.dohnal.vaadin.mvvm.binder;

import javax.annotation.Nonnull;
import java.util.List;

import com.github.dohnal.vaadin.mvvm.ViewBinder;
import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.Property;
import com.google.common.collect.Lists;
import rx.Subscription;

/**
 * Implementation of {@link ViewBinder} for two way binding of properties which are also observables
 *
 * @param <T> type of value in the event
 * @author dohnal
 */
public class ViewPropertyBinder<T, U extends Property<T> & IsObservable<T>>
        implements Disposable<ViewPropertyBinder<T, U>>
{
    private final U property;

    private List<Subscription> subscriptions;

    public ViewPropertyBinder(final @Nonnull U property)
    {
        this.property = property;
        this.subscriptions = Lists.newArrayList();
    }

    /**
     * Binds this property to given property in two way manner
     *
     * @param anotherProperty another property
     * @param <V> type of property
     * @return this binder
     */
    @Nonnull
    public <V extends Property<T> & IsObservable<T>> ViewPropertyBinder<T, U> to(final @Nonnull V anotherProperty)
    {
        subscriptions.add(property.asObservable().subscribe(anotherProperty::setValue));
        subscriptions.add(anotherProperty.asObservable().subscribe(property::setValue));

        return this;
    }

    @Nonnull
    @Override
    public ViewPropertyBinder<T, U> unbind()
    {
        subscriptions.stream()
                .filter(subscription -> !subscription.isUnsubscribed())
                .forEach(Subscription::unsubscribe);

        subscriptions.clear();

        return this;
    }
}
