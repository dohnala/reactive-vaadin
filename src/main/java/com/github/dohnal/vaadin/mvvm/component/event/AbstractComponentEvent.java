package com.github.dohnal.vaadin.mvvm.component.event;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.dohnal.vaadin.mvvm.Event;
import com.vaadin.shared.Registration;
import rx.Emitter;
import rx.Observable;

/**
 * Base class for component events
 *
 * @param <T> type of value
 * @author dohnal
 */
public abstract class AbstractComponentEvent<T> implements Event<T>
{
    /**
     * Converts given functions to create and register listener to observable of values given listener
     * produces
     *
     * @param createListener function to create listener
     * @param registerListener function to register listener
     * @param <T> type of value given listener produces
     * @param <L> type of listener
     * @return observable of values given listener produces
     */
    @Nonnull
    static <T, L> Observable<T> toObservable(final @Nonnull Function<Consumer<T>, L> createListener,
                                             final @Nonnull Function<L, Registration> registerListener)
    {
        return Observable.create(eventEmitter -> {
            final L listener = createListener.apply(eventEmitter::onNext);

            final Registration registration = registerListener.apply(listener);

            eventEmitter.setCancellation(registration::remove);

        }, Emitter.BackpressureMode.BUFFER);
    }
}
