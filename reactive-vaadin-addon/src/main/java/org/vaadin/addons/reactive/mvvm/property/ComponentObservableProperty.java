package org.vaadin.addons.reactive.mvvm.property;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import org.vaadin.addons.reactive.ObservableProperty;

/**
 * Represents Vaadin component property which is also observable
 *
 * @param <T> type of value
 * @author dohnal
 */
public class ComponentObservableProperty<T> implements ObservableProperty<T>
{
    private final Supplier<Observable<T>> observable;

    private final Consumer<T> property;

    private final AtomicInteger suppressed;

    /**
     * Creates new component property
     *
     * @param observable observable used to emit values
     * @param property property used to set values
     */
    public ComponentObservableProperty(final @Nonnull Supplier<Observable<T>> observable,
                                       final @Nonnull Consumer<T> property)
    {
        this.observable = observable;
        this.property = property;
        this.suppressed = new AtomicInteger(0);
    }

    @Nonnull
    @Override
    public Observable<T> asObservable()
    {
        return observable.get().filter(value -> !isSuppressed());
    }

    @Override
    public void setValue(final @Nonnull T value)
    {
        property.accept(value);
    }

    @Override
    public boolean isSuppressed()
    {
        return suppressed.get() > 0;
    }

    @Nonnull
    @Override
    public Disposable suppress()
    {
        suppressed.incrementAndGet();

        return Disposables.fromRunnable(suppressed::decrementAndGet);
    }
}
