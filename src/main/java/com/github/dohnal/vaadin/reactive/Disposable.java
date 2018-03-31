package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;

/**
 * Interface returned from {@link ReactiveBinder} bind methods after binding to support unbinding
 *
 * @param <T> type of bean which supports unbind
 * @author dohnal
 */
public interface Disposable<T>
{
    /**
     * Unbind all previously bound bindings
     *
     * @return this bean
     */
    @Nonnull
    T unbind();
}
