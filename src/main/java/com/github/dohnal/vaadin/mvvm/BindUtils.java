package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSingleSelect;
import com.vaadin.ui.Label;
import io.reactivex.Observable;

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
}
