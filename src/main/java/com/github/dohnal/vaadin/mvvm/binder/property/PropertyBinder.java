package com.github.dohnal.vaadin.mvvm.binder.property;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import com.github.dohnal.vaadin.mvvm.ReactiveBinder;
import com.github.dohnal.vaadin.mvvm.ReactiveProperty;

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
        addDisposable(ReactiveBinder.bind(this.property.asObservable()).to(property));

        return this;
    }

    @Nonnull
    public final PropertyBinder<T> to(final @Nonnull Consumer<T> consumer)
    {
        addDisposable(ReactiveBinder.bind(this.property.asObservable()).to(consumer));

        return this;
    }

    @Nonnull
    @Override
    public final PropertyBinder<T> unbind()
    {
        super.unbind();

        return this;
    }
}
