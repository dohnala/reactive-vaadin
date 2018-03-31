package com.github.dohnal.vaadin.reactive.binder.property;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;

/**
 * Binder for binding reactive property
 *
 * @param <T> type of reactive property
 *
 * @author dohnal
 */
public final class PropertyBinder<T> extends AbstractPropertyBinder<T>
{
    public PropertyBinder(final @Nonnull ReactiveProperty<T> property)
    {
        super(property);
    }

    /**
     * Binds reactive property to given reactive property
     *
     * @param property reactive property
     * @return this binder
     */
    @Nonnull
    public final PropertyBinder<T> to(final @Nonnull ReactiveProperty<T> property)
    {
        addDisposable(ReactiveBinder.bind(this.property.asObservable()).to(property));

        return this;
    }

    /**
     * Binds reactive property to given consumer
     *
     * @param consumer consumer
     * @return this binder
     */
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
