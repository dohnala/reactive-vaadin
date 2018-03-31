package com.github.dohnal.vaadin.reactive.binder.observable;

import javax.annotation.Nonnull;

import com.vaadin.ui.AbstractField;
import rx.Observable;

/**
 * Binder for binding observable to read-only property of some Vaadin field
 *
 * @author dohnal
 */
public final class ObservableReadOnlyBinder extends AbstractObservableBinder<Boolean>
{
    public ObservableReadOnlyBinder(final @Nonnull Observable<Boolean> observable)
    {
        super(observable);
    }

    /**
     * Binds observable to read-only property of given field
     *
     * @param field field
     * @return this binder
     */
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
