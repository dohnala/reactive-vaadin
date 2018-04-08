package com.github.dohnal.vaadin.mvvm.component.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.vaadin.ui.Component;

/**
 * @author dohnal
 */
public final class ComponentEnabledProperty extends AbstractComponentProperty<Boolean>
{
    private final Component component;

    public ComponentEnabledProperty(final @Nonnull Component component)
    {
        this.component = component;
    }

    @Override
    public final void setValue(final @Nullable Boolean value)
    {
        withUIAccess(component, () -> component.setEnabled(Boolean.TRUE.equals(value)));
    }
}
