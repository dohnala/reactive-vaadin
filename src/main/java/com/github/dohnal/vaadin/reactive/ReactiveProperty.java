package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.property.Property;
import rx.Observable;

/**
 * Reactive property stores single editable value and also acts as observable
 *
 * @param <T> type of property
 * @author dohnal
 */
public interface ReactiveProperty<T> extends Disposable<ReactiveProperty<T>>
{
    /**
     * Creates new property with no value
     *
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    static <T> ReactiveProperty<T> empty()
    {
        return new Property<>();
    }

    /**
     * Creates new property with given default value
     *
     * @param defaultValue default value
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    static <T> ReactiveProperty<T> withValue(final @Nullable T defaultValue)
    {
        return new Property<>(defaultValue);
    }

    /**
     * Creates new property from given observable
     *
     * @param observable observable
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    static <T> ReactiveProperty<T> fromObservable(final @Nonnull Observable<T> observable)
    {
        return new Property<>(observable);
    }

    /**
     * Creates new property from given property
     *
     * @param property property
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    static <T> ReactiveProperty<T> fromProperty(final @Nonnull Observable<T> property)
    {
        return new Property<>(property);
    }

    /**
     * Returns current value
     *
     * @return current value
     */
    @Nullable
    T getValue();

    /**
     * Sets new value and notifies all subscribers
     *
     * @param value value
     */
    void setValue(final @Nullable T value);

    /**
     * Updates a value by given update function and notifies all subscribers
     *
     * @param update update function
     */
    void updateValue(final @Nonnull Function<T, T> update);

    /**
     * Return observable for this property which can be subscribed to
     *
     * @return observable for this property
     */
    @Nonnull
    Observable<T> asObservable();

    /**
     * Return whether this property has observers
     *
     * @return whether this property has observers
     */
    boolean hasObservers();
}
