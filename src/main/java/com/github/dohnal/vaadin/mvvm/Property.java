package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Property stores single editable value and also acts as observable
 *
 * @author dohnal
 */
public class Property<T>
{
    private BehaviorSubject<T> subject;

    /**
     * Creates new property with no value
     */
    public Property()
    {
        this.subject = BehaviorSubject.create();
    }

    /**
     * Creates new property with given default value
     *
     * @param defaultValue default value
     */
    public Property(final @Nullable T defaultValue)
    {
        this.subject = BehaviorSubject.create(defaultValue);
    }

    /**
     * Creates new property with observable bound to it
     *
     * @param observable observable
     */
    public Property(final @Nonnull Observable<T> observable)
    {
        this();

        BindUtils.bind(observable, this);
    }

    /**
     * Creates new property with another property bound to it
     *
     * @param anotherProperty another property
     */
    public Property(final @Nonnull Property<T> anotherProperty)
    {
        this(anotherProperty.getValue());

        BindUtils.bind(anotherProperty, this);
    }

    /**
     * Sets new value and notifies all subscribers
     *
     * @param value value
     */
    public void setValue(final @Nullable T value)
    {
        subject.onNext(value);
    }

    /**
     * Returns current value
     *
     * @return current value
     */
    @Nullable
    public T getValue()
    {
        return subject.getValue();
    }

    /**
     * Return observable for this property which can be subscribed to
     *
     * @return observable for this property
     */
    @Nonnull
    public Observable<T> asObservable()
    {
        return subject;
    }
}
