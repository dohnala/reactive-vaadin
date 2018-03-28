package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;

import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSingleSelect;
import com.vaadin.ui.Label;
import rx.Observable;

/**
 * Reactive binder for binding reactive primitives
 *
 * @author dohnal
 */
public class ReactiveBinder
{
    /**
     * Binds given observable to label
     *
     * @param observable observable
     * @param label label
     */
    public static void bindObservable(final @Nonnull Observable<String> observable,
                                      final @Nonnull Label label)
    {
        observable.subscribe(value -> {
            if (label.getUI() != null)
            {
                label.getUI().access(() -> label.setValue(value));
            }
            else
            {
                label.setValue(value);
            }
        });
    }

    /**
     * Binds given observable to field
     *
     * @param observable observable
     * @param field field
     * @param <T> type of value
     */
    public static <T> void bindObservable(final @Nonnull Observable<T> observable,
                                          final @Nonnull AbstractField<T> field)
    {
        observable.subscribe(value -> {
            if (field.getUI() != null)
            {
                field.getUI().access(() -> field.setValue(value));
            }
            else
            {
                field.setValue(value);
            }
        });
    }

    /**
     * Binds given observable to select
     *
     * @param observable observable
     * @param select select
     * @param <T> type of value
     */
    public static <T> void bindObservable(final @Nonnull Observable<T> observable,
                                          final @Nonnull AbstractSingleSelect<T> select)
    {
        observable.subscribe(value -> {
            if (select.getUI() != null)
            {
                select.getUI().access(() -> select.setValue(value));
            }
            else
            {
                select.setValue(value);
            }
        });
    }

    /**
     * Binds given observable to property
     *
     * @param observable observable
     * @param property property
     * @param <T> type of value
     */
    public static <T> void bindObservable(final @Nonnull Observable<T> observable,
                                          final @Nonnull ReactiveProperty<T> property)
    {
        observable.subscribe(property::setValue);
    }

    /**
     * Binds given reactive property to label
     *
     * @param property reactive property
     * @param label label
     */
    public static void bindProperty(final @Nonnull ReactiveProperty<String> property,
                                    final @Nonnull Label label)
    {
        bindObservable(property.asObservable(), label);
    }

    /**
     * Binds given reactive property to field
     *
     * @param property reactive property
     * @param field field
     * @param <T> type of value
     * @return registration object to remove added listener
     */
    @Nonnull
    public static <T> Registration bindProperty(final @Nonnull ReactiveProperty<T> property,
                                                final @Nonnull AbstractField<T> field)
    {
        bindObservable(property.asObservable(), field);

        return bindComponent(field, property);
    }

    /**
     * Binds given reactive property to field
     *
     * @param property reactive property
     * @param select select
     * @param <T> type of value
     * @return registration object to remove added listener
     */
    @Nonnull
    public static <T> Registration bindProperty(final @Nonnull ReactiveProperty<T> property,
                                                final @Nonnull AbstractSingleSelect<T> select)
    {
        bindObservable(property.asObservable(), select);

        return bindComponent(select, property);
    }

    /**
     * Binds given reactive property to another reactive property
     *
     * @param property reactive property
     * @param anotherProperty another reactive property
     * @param <T> type of value
     */
    public static <T> void bindProperty(final @Nonnull ReactiveProperty<T> property,
                                        final @Nonnull ReactiveProperty<T> anotherProperty)
    {
        bindObservable(property.asObservable(), anotherProperty);
    }

    /**
     * Binds given component which has user-editable value to property
     *
     * @param hasValue component which has user-editable value
     * @param property reactive property
     * @param <T> type of value
     * @return registration object to remove added listener
     */
    @Nonnull
    private static <T> Registration bindComponent(final @Nonnull HasValue<T> hasValue,
                                                  final @Nonnull ReactiveProperty<T> property)
    {
        return hasValue.addValueChangeListener(event -> property.setValue(event.getValue()));
    }
}
