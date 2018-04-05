package com.github.dohnal.vaadin.reactive.binder.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSingleSelect;

/**
 * Binder for binding observable to value property of some Vaadin field
 *
 * @param <T> type of reactive property
 * @author dohnal
 */
public final class PropertyValueBinder<T> extends AbstractPropertyBinder<T>
{
    public PropertyValueBinder(final @Nonnull ReactiveProperty<T> property)
    {
        super(property);
    }

    /**
     * Binds reactive property to value property of given field
     *
     * @param field field
     * @return this binder
     */
    @Nonnull
    public final PropertyValueBinder<T> to(final @Nonnull AbstractField<T> field)
    {
        addDisposable(ReactiveBinder.bindValue(this.property.asObservable()).to(field));

        addRegistration(field.addValueChangeListener(event -> property.setValue(event.getValue())));

        return this;
    }

    /**
     * Binds reactive property to value property of given select
     *
     * @param select select
     * @return this binder
     */
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
