package com.github.dohnal.vaadin.mvvm.binder.observable;

import javax.annotation.Nonnull;

import com.vaadin.ui.AbstractComponent;
import rx.Observable;

/**
 * Binder for binding observable to visible property of some Vaadin component
 *
 * @author dohnal
 */
public final class ObservableVisibleBinder extends AbstractObservableBinder<Boolean>
{
    public ObservableVisibleBinder(final @Nonnull Observable<Boolean> observable)
    {
        super(observable);
    }

    /**
     * Binds observable to visible property of given component
     *
     * @param component component
     * @return this binder
     */
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
