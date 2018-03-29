package com.github.dohnal.vaadin.mvvm.binder.observable;

import javax.annotation.Nonnull;

import com.vaadin.ui.AbstractField;
import rx.Observable;

/**
 * @author dohnal
 */
public final class ObservableReadOnlyBinder extends AbstractObservableBinder<Boolean>
{
    public ObservableReadOnlyBinder(final @Nonnull Observable<Boolean> observable)
    {
        super(observable);
    }

    @Nonnull
    public final <T> ObservableReadOnlyBinder to(final @Nonnull AbstractField<T> field)
    {
        addSubscription(observable.subscribe(value -> {
            if (field.getUI() != null)
            {
                field.getUI().access(() -> field.setReadOnly(value));
            }
            else
            {
                field.setReadOnly(value);
            }
        }));

        return this;
    }

    @Nonnull
    @Override
    public final ObservableReadOnlyBinder unbind()
    {
        super.unbind();

        return this;
    }
}
