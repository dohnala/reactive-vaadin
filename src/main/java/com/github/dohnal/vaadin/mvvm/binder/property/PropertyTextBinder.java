package com.github.dohnal.vaadin.mvvm.binder.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.ReactiveProperty;
import com.github.dohnal.vaadin.mvvm.binder.ReactiveBinder;
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

    @Override
    public PropertyTextBinder unbind()
    {
        super.unbind();

        return null;
    }
}
