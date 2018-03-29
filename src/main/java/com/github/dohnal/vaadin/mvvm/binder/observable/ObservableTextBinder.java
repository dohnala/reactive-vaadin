package com.github.dohnal.vaadin.mvvm.binder.observable;

import javax.annotation.Nonnull;

import com.vaadin.ui.Label;
import rx.Observable;

/**
 * @author dohnal
 */
public final class ObservableTextBinder extends AbstractObservableBinder<String>
{
    public ObservableTextBinder(final @Nonnull Observable<String> observable)
    {
        super(observable);
    }

    @Nonnull
    public final ObservableTextBinder to(final @Nonnull Label label)
    {
        addSubscription(observable.subscribe(value -> {
            if (label.getUI() != null)
            {
                label.getUI().access(() -> label.setValue(value));
            }
            else
            {
                label.setValue(value);
            }
        }));

        return this;
    }

    @Nonnull
    @Override
    public final ObservableTextBinder unbind()
    {
        super.unbind();

        return this;
    }
}
