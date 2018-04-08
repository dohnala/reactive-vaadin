package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nullable;

/**
 * Base interface for properties whose can be set
 *
 * @param <T> type of values which can be set
 * @author dohnal
 */
public interface Property<T>
{
    /**
     * Sets given value to this property
     *
     * @param value value
     */
    void setValue(final @Nullable T value);
}
