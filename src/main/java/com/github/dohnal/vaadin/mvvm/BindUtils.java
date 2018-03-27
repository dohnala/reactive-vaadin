package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;

import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSingleSelect;
import com.vaadin.ui.Label;
import rx.Observable;

/**
 * Utility class for binding reactive primitives to Vaadin components
 *
 * @author dohnal
 */
public class BindUtils
{
    /**
     * Binds given observable to label
     *
     * @param observable observable
     * @param label label
     */
    public static void bind(final @Nonnull Observable<String> observable,
                            final @Nonnull Label label)
    {
        observable.subscribe(value -> label.getUI().access(() -> label.setValue(value)));
    }

    /**
     * Binds given observable to field
     *
     * @param observable observable
     * @param field field
     * @param <T> type of value
     */
    public static <T> void bind(final @Nonnull Observable<T> observable,
                                final @Nonnull AbstractField<T> field)
    {
        observable.subscribe(value -> field.getUI().access(() -> field.setValue(value)));
    }

    /**
     * Binds given observable to select
     *
     * @param observable observable
     * @param select select
     * @param <T> type of value
     */
    public static <T> void bind(final @Nonnull Observable<T> observable,
                                final @Nonnull AbstractSingleSelect<T> select)
    {
        observable.subscribe(value -> select.getUI().access(() -> select.setValue(value)));
    }

    /**
     * Binds given property to label
     *
     * @param property property
     * @param label label
     */
    public static void bind(final @Nonnull Property<String> property,
                            final @Nonnull Label label)
    {
        bind(property.asObservable(), label);
    }

    /**
     * Binds given property to field
     *
     * @param property property
     * @param field field
     * @param <T> type of value
     * @return registration object to remove added listener
     */
    @Nonnull
    public static <T> Registration bind(final @Nonnull Property<T> property,
                                        final @Nonnull AbstractField<T> field)
    {
        bind(property.asObservable(), field);

        return bind(field, property);
    }

    /**
     * Binds given property to field
     *
     * @param property property
     * @param select select
     * @param <T> type of value
     * @return registration object to remove added listener
     */
    @Nonnull
    public static <T> Registration bind(final @Nonnull Property<T> property,
                                        final @Nonnull AbstractSingleSelect<T> select)
    {
        bind(property.asObservable(), select);

        return bind(select, property);
    }

    /**
     * Binds given field component which has user-editable value to property
     *
     * @param hasValue field component which has user-editable value
     * @param property property
     * @param <T> type of value
     * @return registration object to remove added listener
     */
    @Nonnull
    private static <T> Registration bind(final @Nonnull HasValue<T> hasValue,
                                         final @Nonnull Property<T> property)
    {
        return hasValue.addValueChangeListener(event -> property.setValue(event.getValue()));
    }
}
