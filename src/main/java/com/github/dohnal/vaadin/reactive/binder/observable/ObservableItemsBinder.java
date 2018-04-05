package com.github.dohnal.vaadin.reactive.binder.observable;

import javax.annotation.Nonnull;
import java.util.Collection;

import com.vaadin.data.HasItems;
import rx.Observable;

/**
 * Binder for binding observable to items property of some Vaadin component
 *
 * @param <T> type of observable
 * @author dohnal
 */
public final class ObservableItemsBinder<T> extends AbstractObservableBinder<Collection<T>>
{
    public ObservableItemsBinder(final @Nonnull Observable<Collection<T>> observable)
    {
        super(observable);
    }

    /**
     * Binds observable to items property of given component
     *
     * @param component component
     * @return this binder
     */
    @Nonnull
    public final ObservableItemsBinder<T> to(final @Nonnull HasItems<T> component)
    {
        addSubscription(observable.subscribe(value -> {
            if (component.getUI() != null)
            {
                component.getUI().access(() -> component.setItems(value));
            }
            else
            {
                component.setItems(value);
            }
        }));

        return this;
    }
}
