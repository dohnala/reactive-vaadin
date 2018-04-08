package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;

import rx.Observable;

/**
 * Base interface for something which can be observed
 *
 * @param <T> type of values which can be observed
 * @author dohnal
 */
public interface IsObservable<T>
{
    /**
     * Returns stream of values which can be observed
     *
     * @return stream of values which can be observed
     */
    @Nonnull
    Observable<T> asObservable();
}
