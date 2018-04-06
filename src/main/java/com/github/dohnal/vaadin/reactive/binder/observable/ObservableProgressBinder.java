package com.github.dohnal.vaadin.reactive.binder.observable;

import javax.annotation.Nonnull;

import com.vaadin.ui.ProgressBar;
import rx.Observable;

/**
 * Binder for binding observable to progress property of some Vaadin component
 *
 * @author dohnal
 */
public final class ObservableProgressBinder extends AbstractObservableBinder<Float>
{
    public ObservableProgressBinder(final @Nonnull Observable<Float> observable)
    {
        super(observable);
    }

    /**
     * Binds observable to progress property of given progress bar
     *
     * @param progressBar progress bar
     * @return this binder
     */
    @Nonnull
    public final ObservableProgressBinder to(final @Nonnull ProgressBar progressBar)
    {
        addSubscription(observable.subscribe(value -> {
            if (progressBar.getUI() != null)
            {
                progressBar.getUI().access(() -> progressBar.setValue(value));
            }
            else
            {
                progressBar.setValue(value);
            }
        }));

        return this;
    }

    @Nonnull
    @Override
    public final ObservableProgressBinder unbind()
    {
        super.unbind();

        return this;
    }
}
