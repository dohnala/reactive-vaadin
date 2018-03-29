package com.github.dohnal.vaadin.mvvm.binder.observable;

import javax.annotation.Nonnull;

import com.vaadin.ui.AbstractComponent;
import rx.Observable;

/**
 * @author dohnal
 */
public final class ObservableVisibleBinder extends AbstractObservableBinder<Boolean>
{
    public ObservableVisibleBinder(final @Nonnull Observable<Boolean> observable)
    {
        super(observable);
    }

    @Nonnull
    public final ObservableVisibleBinder to(final @Nonnull AbstractComponent component)
    {
        addSubscription(observable.subscribe(value -> {
            if (component.getUI() != null)
            {
                component.getUI().access(() -> component.setVisible(value));
            }
            else
            {
                component.setVisible(value);
            }
        }));

        return this;
    }

    @Nonnull
    @Override
    public final ObservableVisibleBinder unbind()
    {
        super.unbind();

        return this;
    }
}
