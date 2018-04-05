package com.github.dohnal.vaadin.reactive.binder;

import javax.annotation.Nonnull;
import java.util.List;

import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.google.common.collect.Lists;
import com.vaadin.shared.Registration;
import rx.Subscription;

/**
 * Base binder for binding reactive primitives
 *
 * @author dohnal
 */
public abstract class AbstractBinder implements ReactiveBinder
{
    protected final List<Subscription> subscriptions;

    protected final List<Registration> registrations;

    protected final List<Disposable<?>> disposables;

    public AbstractBinder()
    {
        this.subscriptions = Lists.newArrayList();
        this.registrations = Lists.newArrayList();
        this.disposables = Lists.newArrayList();
    }

    @Nonnull
    @Override
    public AbstractBinder unbind()
    {
        subscriptions.stream()
                .filter(Subscription::isUnsubscribed)
                .forEach(Subscription::unsubscribe);

        registrations.forEach(Registration::remove);
        disposables.forEach(Disposable::unbind);

        subscriptions.clear();
        registrations.clear();
        disposables.clear();

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

    /**
     * Adds given Vaadin registration object to this binder
     *
     * @param registration registration
     */
    protected void addRegistration(final @Nonnull Registration registration)
    {
        registrations.add(registration);
    }

    /**
     * Adds given disposable object to this binder
     *
     * @param disposable disposable
     */
    protected void addDisposable(final @Nonnull Disposable<?> disposable)
    {
        disposables.add(disposable);
    }
}
