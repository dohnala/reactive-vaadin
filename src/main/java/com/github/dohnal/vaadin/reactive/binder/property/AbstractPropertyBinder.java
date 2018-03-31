package com.github.dohnal.vaadin.reactive.binder.property;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.vaadin.shared.Registration;

/**
 * Base binder for binding reactive property
 *
 * @param <T> type of reactive property
 *
 * @author dohnal
 */
public abstract class AbstractPropertyBinder<T> extends ReactiveBinder
{
    protected final ReactiveProperty<T> property;

    protected final List<Disposable<?>> disposables;

    protected final List<Registration> registrations;

    /**
     * Creates a new reactive property binder for reactive property
     *
     * @param property reactive property
     */
    public AbstractPropertyBinder(final @Nonnull ReactiveProperty<T> property)
    {
        this.property = property;
        this.disposables = new ArrayList<>();
        this.registrations = new ArrayList<>();
    }

    @Nonnull
    @Override
    public AbstractPropertyBinder<T> unbind()
    {
        disposables.forEach(Disposable::unbind);
        registrations.forEach(Registration::remove);

        disposables.clear();
        registrations.clear();

        return this;
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

    /**
     * Adds given Vaadin registration object to this binder
     *
     * @param registration registration
     */
    protected void addRegistration(final @Nonnull Registration registration)
    {
        registrations.add(registration);
    }
}
