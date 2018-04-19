package com.github.dohnal.vaadin.reactive.binder;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

/**
 * Specification for binding observable to property by {@link ReactiveBinder#bind(Observable)}
 *
 * @author dohnal
 */
public interface ObservableToPropertySpecification extends BasePropertyBinderSpecification
{
    abstract class WhenBindObservableToPropertySpecification implements ReactiveBinder
    {
        private TestScheduler testScheduler;
        private TestSubject<Integer> testSubject;
        private ReactiveProperty<Integer> property;
        private ObservablePropertyBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void bindObservableToProperty()
        {
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            property = Mockito.mock(ReactiveProperty.class);

            binder = bind(testSubject).to(property);
        }

        @Test
        @DisplayName("Property value should not be set")
        public void testPropertyValue()
        {
            Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
        }

        @Nested
        @DisplayName("When source observable emits value")
        class WhenSourceObservableEmitsValue extends WhenSourceEmitsValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                testSubject.onNext(value);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("When source observable emits value after observable is unbound from property")
        class WhenSourceObservableEmitsValueAfterUnbind extends WhenSourceEmitsValueAfterUnbindSpecification
        {

            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Nonnull
            @Override
            public Disposable getDisposable()
            {
                return binder;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                testSubject.onNext(value);
                testScheduler.triggerActions();
            }
        }
    }
}
