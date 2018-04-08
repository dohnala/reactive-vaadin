package com.github.dohnal.vaadin.mvvm.component.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.dohnal.vaadin.mvvm.component.event.FieldValueChangedEvent;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.vaadin.ui.AbstractField;
import rx.Observable;

/**
 * @author dohnal
 */
public final class FieldValueProperty<T> extends AbstractComponentProperty<T> implements IsObservable<T>
{
    private final AbstractField<T> field;

    public FieldValueProperty(final @Nonnull AbstractField<T> field)
    {
        this.field = field;
    }

    @Nonnull
    @Override
    public final Observable<T> asObservable()
    {
        return new FieldValueChangedEvent<>(field).asObservable();
    }

    @Override
    public final void setValue(final @Nullable T value)
    {
        withUIAccess(field, () -> field.setValue(value));
    }
}
