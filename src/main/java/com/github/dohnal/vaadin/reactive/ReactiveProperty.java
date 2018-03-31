package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.Lists;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Reactive property stores single editable value and also acts as observable
 *
 * @author dohnal
 */
public final class ReactiveProperty<T> implements Disposable<ReactiveProperty<T>>
{
    private BehaviorSubject<T> subject;

    private List<Disposable<?>> disposables;

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
        this.disposables = Lists.newArrayList();
    }

    /**
     * Creates new property with given default value
     *
     * @param defaultValue default value
     */
    private ReactiveProperty(final @Nullable T defaultValue)
    {
        this.subject = BehaviorSubject.create(defaultValue);
        this.disposables = Lists.newArrayList();
    }

    /**
     * Creates new property with observable bound to it
     *
     * @param observable observable
     */
    private ReactiveProperty(final @Nonnull Observable<T> observable)
    {
        this();

        this.disposables.add(ReactiveBinder.bind(observable).to(this));
    }

    /**
     * Creates new property with another property bound to it
     *
     * @param anotherProperty another property
     */
    private ReactiveProperty(final @Nonnull ReactiveProperty<T> anotherProperty)
    {
        this(anotherProperty.getValue());

        this.disposables.add(ReactiveBinder.bind(anotherProperty).to(this));
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
     * Updates a value by given update function and notifies all subscribers
     *
     * @param update update function
     */
    public final void updateValue(final @Nonnull Function<T, T> update)
    {
        setValue(update.apply(getValue()));
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

    @Nonnull
    @Override
    public final ReactiveProperty<T> unbind()
    {
        this.disposables.forEach(Disposable::unbind);

        return this;
    }
}
