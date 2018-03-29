package com.github.dohnal.vaadin.mvvm.binder.property;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import com.github.dohnal.vaadin.mvvm.ReactiveProperty;
import com.github.dohnal.vaadin.mvvm.binder.Disposable;
import com.vaadin.shared.Registration;

/**
 * @author dohnal
 */
public abstract class AbstractPropertyBinder<T> implements Disposable<AbstractPropertyBinder<T>>
{
    protected final ReactiveProperty<T> property;

    protected final List<Disposable<?>> disposables;

    protected final List<Registration> registrations;

    public AbstractPropertyBinder(final @Nonnull ReactiveProperty<T> property)
    {
        this.property = property;
        this.disposables = new ArrayList<>();
        this.registrations = new ArrayList<>();
    }

    @Override
    public AbstractPropertyBinder<T> unbind()
    {
        disposables.forEach(Disposable::unbind);
        registrations.forEach(Registration::remove);

        disposables.clear();
        registrations.clear();

        return null;
    }

    protected void addDisposable(final @Nonnull Disposable<?> disposable)
    {
        disposables.add(disposable);
    }

    protected void addRegistration(final @Nonnull Registration registration)
    {
        registrations.add(registration);
    }
}
