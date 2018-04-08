package com.github.dohnal.vaadin.mvvm.component.event;

import javax.annotation.Nonnull;

import com.vaadin.data.HasValue;
import rx.Observable;

/**
 * @author dohnal
 */
public final class FieldValueChangedEvent<T> extends AbstractComponentEvent<T>
{
    private final HasValue<T> field;

    public FieldValueChangedEvent(final @Nonnull HasValue<T> field)
    {
        this.field = field;
    }

    @Nonnull
    @Override
    public final Observable<T> asObservable()
    {
        return valueChangeEvent().map(HasValue.ValueChangeEvent::getValue);
    }

    @Nonnull
    public final Observable<HasValue.ValueChangeEvent<T>> valueChangeEvent()
    {
        return toObservable(event -> event::accept, field::addValueChangeListener);
    }
}
