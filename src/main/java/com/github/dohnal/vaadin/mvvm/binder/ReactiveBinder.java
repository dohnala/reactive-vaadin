package com.github.dohnal.vaadin.mvvm.binder;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.ReactiveProperty;
import com.github.dohnal.vaadin.mvvm.binder.observable.ObservableBinder;
import com.github.dohnal.vaadin.mvvm.binder.observable.ObservableTextBinder;
import com.github.dohnal.vaadin.mvvm.binder.observable.ObservableValueBinder;
import com.github.dohnal.vaadin.mvvm.binder.property.PropertyBinder;
import com.github.dohnal.vaadin.mvvm.binder.property.PropertyTextBinder;
import com.github.dohnal.vaadin.mvvm.binder.property.PropertyValueBinder;
import rx.Observable;

/**
 * Reactive binder for binding reactive primitives
 *
 * @author dohnal
 */
public class ReactiveBinder
{
    /**
     * Return binder for given observable
     *
     * @param observable observable
     * @param <T> type of observable
     * @return binder
     */
    @Nonnull
    public static <T> ObservableBinder<T> bind(final @Nonnull Observable<T> observable)
    {
        return new ObservableBinder<>(observable);
    }

    /**
     * Return text binder for given observable
     *
     * @param observable observable
     * @return binder
     */
    @Nonnull
    public static ObservableTextBinder bindText(final @Nonnull Observable<String> observable)
    {
        return new ObservableTextBinder(observable);
    }

    /**
     * Return value binder for given observable
     *
     * @param observable observable
     * @param <T> type of observable
     * @return binder
     */
    @Nonnull
    public static <T> ObservableValueBinder<T> bindValue(final @Nonnull Observable<T> observable)
    {
        return new ObservableValueBinder<>(observable);
    }

    /**
     * Return binder for given property
     *
     * @param property property
     * @param <T> type of property
     * @return binder
     */
    @Nonnull
    public static <T> PropertyBinder<T> bind(final @Nonnull ReactiveProperty<T> property)
    {
        return new PropertyBinder<>(property);
    }

    /**
     * Return text binder for given property
     *
     * @param property property
     * @return binder
     */
    @Nonnull
    public static PropertyTextBinder bindText(final @Nonnull ReactiveProperty<String> property)
    {
        return new PropertyTextBinder(property);
    }

    /**
     * Return value binder for given property
     *
     * @param property property
     * @param <T> type of property
     * @return binder
     */
    @Nonnull
    public static <T> PropertyValueBinder<T> bindValue(final @Nonnull ReactiveProperty<T> property)
    {
        return new PropertyValueBinder<>(property);
    }
}
