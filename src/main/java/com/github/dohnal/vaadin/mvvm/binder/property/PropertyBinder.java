package com.github.dohnal.vaadin.mvvm.binder.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.ReactiveProperty;
import com.github.dohnal.vaadin.mvvm.binder.ReactiveBinder;

/**
 * @author dohnal
 */
public final class PropertyBinder<T> extends AbstractPropertyBinder<T>
{
    public PropertyBinder(final @Nonnull ReactiveProperty<T> property)
    {
        super(property);
    }

    @Nonnull
    public final PropertyBinder<T> to(final @Nonnull ReactiveProperty<T> property)
    {
        ReactiveBinder.bind(this.property.asObservable()).to(property);

        return this;
    }
}
