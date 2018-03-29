package com.github.dohnal.vaadin.mvvm.binder.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.ReactiveProperty;

/**
 * @author dohnal
 */
public abstract class AbstractPropertyBinder<T>
{
    protected final ReactiveProperty<T> property;

    public AbstractPropertyBinder(final @Nonnull ReactiveProperty<T> property)
    {
        this.property = property;
    }
}
