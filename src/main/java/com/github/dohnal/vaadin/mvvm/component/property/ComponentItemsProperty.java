package com.github.dohnal.vaadin.mvvm.component.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

import com.vaadin.data.HasItems;

/**
 * @author dohnal
 */
public final class ComponentItemsProperty<T, C extends Collection<T>> extends AbstractComponentProperty<C>
{
    private final HasItems<T> component;

    public ComponentItemsProperty(final @Nonnull HasItems<T> component)
    {
        this.component = component;
    }

    @Override
    public final void setValue(final @Nullable C value)
    {
        withUIAccess(component, () -> component.setItems(value));
    }
}
