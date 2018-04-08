package com.github.dohnal.vaadin.mvvm.component;

import javax.annotation.Nonnull;
import java.util.Collection;

import com.github.dohnal.vaadin.mvvm.component.property.ComponentEnabledProperty;
import com.github.dohnal.vaadin.mvvm.component.property.ComponentItemsProperty;
import com.github.dohnal.vaadin.mvvm.component.property.ComponentVisibleProperty;
import com.github.dohnal.vaadin.mvvm.component.property.FieldReadOnlyProperty;
import com.github.dohnal.vaadin.mvvm.component.property.FieldValueProperty;
import com.github.dohnal.vaadin.mvvm.component.property.ProgressBarValueProperty;
import com.vaadin.data.HasItems;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.ProgressBar;

/**
 * List of all component properties
 *
 * @author dohnal
 */
public interface ComponentProperties
{
    /**
     * Returns visible property of given component
     *
     * @param component component
     * @return visible property
     */
    @Nonnull
    default ComponentVisibleProperty visibleOf(final @Nonnull Component component)
    {
        return new ComponentVisibleProperty(component);
    }

    /**
     * Returns enabled property of given component
     *
     * @param component component
     * @return enabled property
     */
    @Nonnull
    default ComponentEnabledProperty enabledOf(final @Nonnull Component component)
    {
        return new ComponentEnabledProperty(component);
    }

    /**
     * Returns read-only property of given field
     *
     * @param field field
     * @return read-only property
     */
    @Nonnull
    default FieldReadOnlyProperty readOnlyOf(final @Nonnull AbstractField<?> field)
    {
        return new FieldReadOnlyProperty(field);
    }

    /**
     * Returns value property of given field
     *
     * @param field field
     * @param <T> type of value in field
     * @return value property
     */
    @Nonnull
    default <T> FieldValueProperty<T> valueOf(final @Nonnull AbstractField<T> field)
    {
        return new FieldValueProperty<>(field);
    }

    /**
     * Returns value property of given progress bar
     *
     * @param progressBar progress bar
     * @return value property
     */
    @Nonnull
    default ProgressBarValueProperty valueOf(final @Nonnull ProgressBar progressBar)
    {
        return new ProgressBarValueProperty(progressBar);
    }

    /**
     * Returns items property of given component
     *
     * @param component component with items
     * @param <T> type of item
     * @param <C> type of collection of items
     * @return items property
     */
    @Nonnull
    default <T, C extends Collection<T>> ComponentItemsProperty<T, C> itemsOf(final @Nonnull HasItems<T> component)
    {
        return new ComponentItemsProperty<>(component);
    }
}
