package com.github.dohnal.vaadin.mvvm.component.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.Property;
import com.vaadin.ui.Component;

/**
 * Base class for component properties
 *
 * @param <T> type of value
 * @author dohnal
 */
public abstract class AbstractComponentProperty<T> implements Property<T>
{
    /**
     * Runs given action with a lock to given component
     *
     * @param component component
     * @param action action
     */
    protected void withUIAccess(final @Nonnull Component component, final @Nonnull Runnable action)
    {
        if (component.isAttached())
        {
            component.getUI().access(action);
        }
        else
        {
            action.run();
        }
    }
}
