package com.github.dohnal.vaadin.mvvm.binder.observable;

import javax.annotation.Nonnull;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSingleSelect;
import rx.Observable;

/**
 * @author dohnal
 */
public final class ObservableValueBinder<T> extends AbstractObservableBinder<T>
{
    public ObservableValueBinder(final @Nonnull Observable<T> observable)
    {
        super(observable);
    }

    @Nonnull
    public final ObservableValueBinder<T> to(final @Nonnull AbstractField<T> field)
    {
        observable.subscribe(value -> {
            if (field.getUI() != null)
            {
                field.getUI().access(() -> field.setValue(value));
            }
            else
            {
                field.setValue(value);
            }
        });

        return this;
    }

    @Nonnull
    public final ObservableValueBinder<T> to(final @Nonnull AbstractSingleSelect<T> select)
    {
        observable.subscribe(value -> {
            if (select.getUI() != null)
            {
                select.getUI().access(() -> select.setValue(value));
            }
            else
            {
                select.setValue(value);
            }
        });

        return this;
    }
}
