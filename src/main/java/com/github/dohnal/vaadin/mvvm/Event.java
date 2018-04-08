package com.github.dohnal.vaadin.mvvm;

import com.github.dohnal.vaadin.reactive.Action;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;

/**
 * Event represents something what happened or changed in the past and some {@link Action} can be called as
 * a reaction to event by {@link ReactiveBinder}
 *
 * @param <T> value this event generates
 * @author dohnal
 */
public interface Event<T> extends IsObservable<T>
{
}
