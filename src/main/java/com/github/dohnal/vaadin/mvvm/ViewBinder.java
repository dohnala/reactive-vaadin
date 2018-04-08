package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.binder.ViewPropertyBinder;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.Property;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;

/**
 * Specialization of {@link ReactiveBinder} for {@link AbstractView}
 *
 * @author dohnal
 */
public interface ViewBinder extends ReactiveBinder
{
    /**
     * Return binder for given property which is also observable
     *
     * @param property property
     * @param <T> type of value of property
     * @param <U> type of property
     * @return binder
     */
    @Nonnull
    default <T, U extends Property<T> & IsObservable<T>> ViewPropertyBinder<T, U> bind(final U property)
    {
        return new ViewPropertyBinder<>(property);
    }
}
