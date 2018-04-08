package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nullable;

/**
 * Action can be called by {@link ReactiveBinder} as a reaction when some {@link Event} happened
 *
 * @param <T> value passed to action
 * @author dohnal
 */
public interface Action<T>
{
    /**
     * Call action with given value
     *
     * @param value value
     */
    void call(final @Nullable T value);
}
