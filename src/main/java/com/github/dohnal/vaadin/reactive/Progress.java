package com.github.dohnal.vaadin.reactive;

/**
 * Used to control progress on some computation by given float value from 0.0f to 1.0f inclusive
 *
 * @author dohnal
 */
public interface Progress
{
    /**
     * Sets progress by given value
     *
     * @param value value in range 0.0f to 1.0f inclusive
     */
    void set(final float value);

    /**
     * Adds given value to current progress
     *
     * @param value value in range 0.0f to 1.0f inclusive
     */
    void add(final float value);
}
