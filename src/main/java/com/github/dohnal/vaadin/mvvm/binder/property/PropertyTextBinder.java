package com.github.dohnal.vaadin.mvvm.binder.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.ReactiveBinder;
import com.github.dohnal.vaadin.mvvm.ReactiveProperty;
import com.vaadin.ui.Label;

/**
 * @author dohnal
 */
public final class PropertyTextBinder extends AbstractPropertyBinder<String>
{
    public PropertyTextBinder(final @Nonnull ReactiveProperty<String> property)
    {
        super(property);
    }

    @Nonnull
    public final PropertyTextBinder to(final @Nonnull Label label)
    {
        addDisposable(ReactiveBinder.bindText(this.property.asObservable()).to(label));

        return this;
    }

    @Nonnull
    @Override
    public final PropertyTextBinder unbind()
    {
        super.unbind();

        return this;
    }
}
