package com.github.dohnal.vaadin.reactive.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.google.common.collect.Lists;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Basic implementation of {@link ReactiveProperty} based on {@link BehaviorSubject}
 *
 * @param <T> type of property
 * @author dohnal
 */
public final class Property<T> implements ReactiveProperty<T>
{
    private BehaviorSubject<T> subject;

    private List<Disposable<?>> disposables;

    /**
     * Creates new property with no value
     */
    public Property()
    {
        this.subject = BehaviorSubject.create();
        this.disposables = Lists.newArrayList();
    }

    /**
     * Creates new property with given default value
     *
     * @param defaultValue default value
     */
    public Property(final @Nullable T defaultValue)
    {
        this.subject = BehaviorSubject.create(defaultValue);
        this.disposables = Lists.newArrayList();
    }

    /**
     * Creates new property with observable bound to it
     *
     * @param observable observable
     */
    public Property(final @Nonnull Observable<T> observable)
    {
        this();

        this.disposables.add(ReactiveBinder.bind(observable).to(this));
    }

    /**
     * Creates new property with another property bound to it
     *
     * @param anotherProperty another property
     */
    public Property(final @Nonnull ReactiveProperty<T> anotherProperty)
    {
        this(anotherProperty.getValue());

        this.disposables.add(ReactiveBinder.bind(anotherProperty).to(this));
    }

    @Nullable
    @Override
    public final T getValue()
    {
        return subject.getValue();
    }

    @Override
    public final void setValue(final @Nullable T value)
    {
        subject.onNext(value);
    }

    @Override
    public final void updateValue(final @Nonnull Function<T, T> update)
    {
        setValue(update.apply(getValue()));
    }

    @Nonnull
    @Override
    public final Observable<T> asObservable()
    {
        return subject;
    }

    @Override
    public final boolean hasObservers()
    {
        return subject.hasObservers();
    }

    @Nonnull
    @Override
    public final ReactiveProperty<T> unbind()
    {
        this.disposables.forEach(Disposable::unbind);

        return this;
    }
}
