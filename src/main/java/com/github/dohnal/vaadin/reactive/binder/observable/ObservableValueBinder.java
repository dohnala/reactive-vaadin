package com.github.dohnal.vaadin.reactive.binder.observable;

import javax.annotation.Nonnull;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSingleSelect;
import rx.Observable;

/**
 * Binder for binding observable to value property of some Vaadin field
 *
 * @param <T> type of observable
 * @author dohnal
 */
public final class ObservableValueBinder<T> extends AbstractObservableBinder<T>
{
    public ObservableValueBinder(final @Nonnull Observable<T> observable)
    {
        super(observable);
    }

    /**
     * Binds observable to value property of given field
     *
     * @param field field
     * @return this binder
     */
    @Nonnull
    public final ObservableValueBinder<T> to(final @Nonnull AbstractField<T> field)
    {
        addSubscription(observable.subscribe(value -> {
            if (field.getUI() != null)
            {
                field.getUI().access(() -> field.setValue(value));
            }
            else
            {
                field.setValue(value);
            }
        }));

        return this;
    }

    /**
     * Binds observable to value property of given select
     *
     * @param select select
     * @return this binder
     */
    @Nonnull
    public final ObservableValueBinder<T> to(final @Nonnull AbstractSingleSelect<T> select)
    {
        addSubscription(observable.subscribe(value -> {
            if (select.getUI() != null)
            {
                select.getUI().access(() -> select.setValue(value));
            }
            else
            {
                select.setValue(value);
            }
        }));

        return this;
    }

    @Nonnull
    @Override
    public final ObservableValueBinder<T> unbind()
    {
        super.unbind();

        return this;
    }
}
