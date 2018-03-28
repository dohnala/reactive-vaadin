package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Reactive property stores single editable value and also acts as observable
 *
 * @author dohnal
 */
public final class ReactiveProperty<T>
{
    private BehaviorSubject<T> subject;

    /**
     * Creates new property with no value
     *
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    public static <T> ReactiveProperty<T> empty()
    {
        return new ReactiveProperty<>();
    }

    /**
     * Creates new property with given default value
     *
     * @param defaultValue default value
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    public static <T> ReactiveProperty<T> withValue(final @Nullable T defaultValue)
    {
        return new ReactiveProperty<>(defaultValue);
    }

    /**
     * Creates new property from given observable
     *
     * @param observable observable
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    public static <T> ReactiveProperty<T> fromObservable(final @Nonnull Observable<T> observable)
    {
        return new ReactiveProperty<>(observable);
    }

    /**
     * Creates new property from given property
     *
     * @param property property
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    public static <T> ReactiveProperty<T> fromProperty(final @Nonnull Observable<T> property)
    {
        return new ReactiveProperty<>(property);
    }

    /**
     * Creates new property with no value
     */
    private ReactiveProperty()
    {
        this.subject = BehaviorSubject.create();
    }

    /**
     * Creates new property with given default value
     *
     * @param defaultValue default value
     */
    private ReactiveProperty(final @Nullable T defaultValue)
    {
        this.subject = BehaviorSubject.create(defaultValue);
    }

    /**
     * Creates new property with observable bound to it
     *
     * @param observable observable
     */
    private ReactiveProperty(final @Nonnull Observable<T> observable)
    {
        this();

        ReactiveBinder.bindObservable(observable, this);
    }

    /**
     * Creates new property with another property bound to it
     *
     * @param anotherProperty another property
     */
    private ReactiveProperty(final @Nonnull ReactiveProperty<T> anotherProperty)
    {
        this(anotherProperty.getValue());

        ReactiveBinder.bindProperty(anotherProperty, this);
    }

    /**
     * Sets new value and notifies all subscribers
     *
     * @param value value
     */
    public final void setValue(final @Nullable T value)
    {
        subject.onNext(value);
    }

    /**
     * Returns current value
     *
     * @return current value
     */
    @Nullable
    public final T getValue()
    {
        return subject.getValue();
    }

    /**
     * Return observable for this property which can be subscribed to
     *
     * @return observable for this property
     */
    @Nonnull
    public final Observable<T> asObservable()
    {
        return subject;
    }
}
