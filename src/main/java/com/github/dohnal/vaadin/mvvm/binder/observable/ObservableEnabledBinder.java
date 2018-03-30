package com.github.dohnal.vaadin.mvvm.binder.observable;

import javax.annotation.Nonnull;

import com.vaadin.ui.AbstractComponent;
import rx.Observable;

/**
 * Binder for binding observable to enabled property of some Vaadin component
 *
 * @author dohnal
 */
public final class ObservableEnabledBinder extends AbstractObservableBinder<Boolean>
{
    public ObservableEnabledBinder(final @Nonnull Observable<Boolean> observable)
    {
        super(observable);
    }

    /**
     * Binds observable to enabled property of given component
     *
     * @param component component
     * @return this binder
     */
    @Nonnull
    public final ObservableEnabledBinder to(final @Nonnull AbstractComponent component)
    {
        addSubscription(observable.subscribe(value -> {
            if (component.getUI() != null)
            {
                component.getUI().access(() -> component.setEnabled(value));
            }
            else
            {
                component.setEnabled(value);
            }
        }));

        return this;
    }

    @Nonnull
    @Override
    public final ObservableEnabledBinder unbind()
    {
        super.unbind();

        return this;
    }
}
