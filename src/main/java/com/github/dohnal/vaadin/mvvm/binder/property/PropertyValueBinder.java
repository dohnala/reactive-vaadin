package com.github.dohnal.vaadin.mvvm.binder.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.ReactiveProperty;
import com.github.dohnal.vaadin.mvvm.binder.ReactiveBinder;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSingleSelect;

/**
 * @author dohnal
 */
public final class PropertyValueBinder<T> extends AbstractPropertyBinder<T>
{
    public PropertyValueBinder(final @Nonnull ReactiveProperty<T> property)
    {
        super(property);
    }

    @Nonnull
    public final PropertyValueBinder<T> to(final @Nonnull AbstractField<T> field)
    {
        addDisposable(ReactiveBinder.bindValue(this.property.asObservable()).to(field));

        addRegistration(field.addValueChangeListener(event -> property.setValue(event.getValue())));

        return this;
    }

    @Nonnull
    public final PropertyValueBinder<T> to(final @Nonnull AbstractSingleSelect<T> select)
    {
        addDisposable(ReactiveBinder.bindValue(this.property.asObservable()).to(select));

        addRegistration(select.addValueChangeListener(event -> property.setValue(event.getValue())));

        return this;
    }

    @Nonnull
    @Override
    public final PropertyValueBinder<T> unbind()
    {
        super.unbind();

        return this;
    }
}
