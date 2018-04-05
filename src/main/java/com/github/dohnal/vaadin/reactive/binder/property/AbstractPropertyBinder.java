package com.github.dohnal.vaadin.reactive.binder.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.binder.AbstractBinder;

/**
 * Base binder for binding reactive property
 *
 * @param <T> type of reactive property
 * @author dohnal
 */
public abstract class AbstractPropertyBinder<T> extends AbstractBinder
{
    protected final ReactiveProperty<T> property;

    /**
     * Creates a new reactive property binder for reactive property
     *
     * @param property reactive property
     */
    public AbstractPropertyBinder(final @Nonnull ReactiveProperty<T> property)
    {
        super();

        this.property = property;
    }
}
