package com.github.dohnal.vaadin.reactive.property;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link BehaviorSubjectProperty}
 *
 * @author dohnal
 */
public class BehaviorSubjectPropertyTest
{
    @Test
    @DisplayName("Value should be null after empty property is created")
    public void testValueAfterCreateEmpty()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        assertNull(property.getValue());
    }

    @Test
    @DisplayName("Observable shouldn't emit any value after empty property is created")
    public void testObservableAfterCreateEmpty()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.asObservable().test()
                .assertNoValues();
    }

    @Test
    @DisplayName("Value should be correct after property is created with value")
    public void testValueAfterCreateWithValue()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.withValue(5);

        assertEquals(new Integer(5), property.getValue());
    }

    @Test
    @DisplayName("Observable should emit default value after property is created with default value")
    public void testObservableAfterCreateWithValue()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.withValue(5);

        property.asObservable().test()
                .assertValue(5);
    }

    @Test
    @DisplayName("Value should be null after property is created from empty observable")
    public void testValueAfterCreateFromEmptyObservable()
    {
        final TestSubject<Integer> testSubject = TestSubject.create(Schedulers.test());

        final ReactiveProperty<Integer> property = ReactiveProperty.fromObservable(testSubject);

        assertNull(property.getValue());
    }

    @Test
    @DisplayName("Observable shouldn't emit any value after property is created from empty observable")
    public void testObservableAfterCreateFromEmptyObservable()
    {
        final TestSubject<Integer> testSubject = TestSubject.create(Schedulers.test());

        final ReactiveProperty<Integer> property = ReactiveProperty.fromObservable(testSubject);

        property.asObservable().test()
                .assertNoValues();
    }

    @Test
    @DisplayName("Value should be correct after source observable emits value")
    public void testValueAfterSourceObservableEmitsValue()
    {
        final TestScheduler testScheduler = Schedulers.test();
        final TestSubject<Integer> testSubject = TestSubject.create(testScheduler);

        final ReactiveProperty<Integer> property = ReactiveProperty.fromObservable(testSubject);

        testSubject.onNext(5);
        testScheduler.triggerActions();

        assertEquals(new Integer(5), property.getValue());
    }

    @Test
    @DisplayName("Observable should emit correct value after source observable emits value")
    public void testObservableAfterSourceObservableEmitsValue()
    {
        final TestScheduler testScheduler = Schedulers.test();
        final TestSubject<Integer> testSubject = TestSubject.create(testScheduler);

        final ReactiveProperty<Integer> property = ReactiveProperty.fromObservable(testSubject);

        testSubject.onNext(5);
        testScheduler.triggerActions();

        property.asObservable().test()
                .assertValue(5);
    }

    @Test
    @DisplayName("Value should be correct after source observable emits different value")
    public void testValueAfterSourceObservableEmitsDifferentValue()
    {
        final TestScheduler testScheduler = Schedulers.test();
        final TestSubject<Integer> testSubject = TestSubject.create(testScheduler);

        final ReactiveProperty<Integer> property = ReactiveProperty.fromObservable(testSubject);

        testSubject.onNext(5);
        testSubject.onNext(7);
        testScheduler.triggerActions();

        assertEquals(new Integer(7), property.getValue());
    }

    @Test
    @DisplayName("Observable should emit correct value after source observable emits different value")
    public void testObservableAfterSourceObservableEmitsDifferentValue()
    {
        final TestScheduler testScheduler = Schedulers.test();
        final TestSubject<Integer> testSubject = TestSubject.create(testScheduler);

        final ReactiveProperty<Integer> property = ReactiveProperty.fromObservable(testSubject);

        testSubject.onNext(5);
        testScheduler.triggerActions();

        property.asObservable().test()
                .perform(() -> {
                    testSubject.onNext(7);
                    testScheduler.triggerActions();
                })
                .assertValues(5, 7);
    }

    @Test
    @DisplayName("Observable shouldn't emit correct value after source observable emits same value")
    public void testObservableAfterSourceObservableEmitsSameValue()
    {
        final TestScheduler testScheduler = Schedulers.test();
        final TestSubject<Integer> testSubject = TestSubject.create(testScheduler);

        final ReactiveProperty<Integer> property = ReactiveProperty.fromObservable(testSubject);

        testSubject.onNext(5);
        testScheduler.triggerActions();

        property.asObservable().test()
                .perform(() -> {
                    testSubject.onNext(5);
                    testScheduler.triggerActions();
                })
                .assertValues(5);
    }

    @Test
    @DisplayName("Value should be null after property is created from empty property")
    public void testValueAfterCreateFromEmptyProperty()
    {
        final ReactiveProperty<Integer> sourceProperty = ReactiveProperty.empty();

        final ReactiveProperty<Integer> property = ReactiveProperty.fromProperty(sourceProperty);

        assertNull(property.getValue());
    }

    @Test
    @DisplayName("Observable should emit value after property is created from empty property")
    public void testObservableAfterCreateFromEmptyProperty()
    {
        final ReactiveProperty<Integer> sourceProperty = ReactiveProperty.empty();

        final ReactiveProperty<Integer> property = ReactiveProperty.fromProperty(sourceProperty);

        property.asObservable().test()
                .assertValue(null);
    }

    @Test
    @DisplayName("Value should be correct after property is created from property with value")
    public void testValueAfterCreateFromPropertyWithValue()
    {
        final ReactiveProperty<Integer> sourceProperty = ReactiveProperty.withValue(5);

        final ReactiveProperty<Integer> property = ReactiveProperty.fromProperty(sourceProperty);

        assertEquals(new Integer(5), property.getValue());
    }

    @Test
    @DisplayName("Observable should emit value after property is created from property with value")
    public void testObservableAfterCreateFromPropertyWithValue()
    {
        final ReactiveProperty<Integer> sourceProperty = ReactiveProperty.withValue(5);

        final ReactiveProperty<Integer> property = ReactiveProperty.fromProperty(sourceProperty);

        property.asObservable().test()
                .assertValue(5);
    }

    @Test
    @DisplayName("Value should be correct after source property emits value")
    public void testValueAfterSourcePropertyEmitsValue()
    {
        final ReactiveProperty<Integer> sourceProperty = ReactiveProperty.empty();

        final ReactiveProperty<Integer> property = ReactiveProperty.fromProperty(sourceProperty);

        sourceProperty.setValue(7);

        assertEquals(new Integer(7), property.getValue());
    }

    @Test
    @DisplayName("Observable should emit correct value after source property emits value")
    public void testObservableAfterSourcePropertyEmitsValue()
    {
        final ReactiveProperty<Integer> sourceProperty = ReactiveProperty.empty();

        final ReactiveProperty<Integer> property = ReactiveProperty.fromProperty(sourceProperty);

        sourceProperty.setValue(7);

        property.asObservable().test()
                .assertValue(7);
    }

    @Test
    @DisplayName("Value should be correct after source property emits different value")
    public void testValueAfterSourcePropertyEmitsDifferentValue()
    {
        final ReactiveProperty<Integer> sourceProperty = ReactiveProperty.empty();

        final ReactiveProperty<Integer> property = ReactiveProperty.fromProperty(sourceProperty);

        property.setValue(5);
        property.setValue(7);

        assertEquals(new Integer(7), property.getValue());
    }

    @Test
    @DisplayName("Observable should emit correct value after source property emits different value")
    public void testObservableAfterSourcePropertyEmitsDifferentValue()
    {
        final ReactiveProperty<Integer> sourceProperty = ReactiveProperty.empty();

        final ReactiveProperty<Integer> property = ReactiveProperty.fromProperty(sourceProperty);

        property.setValue(5);

        property.asObservable().test()
                .perform(() -> property.setValue(7))
                .assertValues(5, 7);
    }

    @Test
    @DisplayName("Observable shouldn't emit any value after source property emits same value")
    public void testObservableAfterSourcePropertyEmitsSameValue()
    {
        final ReactiveProperty<Integer> sourceProperty = ReactiveProperty.empty();

        final ReactiveProperty<Integer> property = ReactiveProperty.fromProperty(sourceProperty);

        property.setValue(5);

        property.asObservable().test()
                .perform(() -> property.setValue(5))
                .assertValues(5);
    }

    @Test
    @DisplayName("Value should be updated after value is set")
    public void testValueAfterSetValue()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.setValue(2);

        assertEquals(new Integer(2), property.getValue());
    }

    @Test
    @DisplayName("Observable should emit value after value is set")
    public void testObservableAfterSetValue()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.asObservable().test()
                .perform(() -> property.setValue(2))
                .assertValue(2);
    }

    @Test
    @DisplayName("Value should be updated after different value is set")
    public void testValueAfterSetDifferentValue()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.setValue(2);
        property.setValue(7);

        assertEquals(new Integer(7), property.getValue());
    }

    @Test
    @DisplayName("Observable should emit value after different value is set")
    public void testObservableAfterSetDifferentValue()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.setValue(2);

        property.asObservable().test()
                .perform(() -> property.setValue(7))
                .assertValues(2, 7);
    }

    @Test
    @DisplayName("Observable should not emit value after same value is set again")
    public void testObservableAfterSetSameValueMultipleTimes()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.asObservable().test()
                .perform(() -> {
                    property.setValue(2);
                    property.setValue(2);
                })
                .assertValue(2);
    }

    @Test
    @DisplayName("Observable should emit only last value after subscribed")
    public void testObservableAfterSetMultipleValues()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.setValue(2);
        property.setValue(7);
        property.setValue(5);

        property.asObservable().test()
                .assertValue(5);
    }

    @Test
    @DisplayName("Value should be updated after update")
    public void testValueAfterUpdateValue()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.updateValue(value -> 5);

        assertEquals(new Integer(5), property.getValue());
    }

    @Test
    @DisplayName("Observable should emit value after update")
    public void testObservableAfterUpdateValue()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.asObservable().test()
                .perform(() -> property.updateValue(value -> 5))
                .assertValue(5);
    }

    @Test
    @DisplayName("Value should be updated after different update")
    public void testValueAfterDifferentUpdate()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.setValue(2);

        property.updateValue(value -> value + 5);

        assertEquals(new Integer(7), property.getValue());
    }

    @Test
    @DisplayName("Observable should emit value after different update")
    public void testObservableAfterDifferentUpdate()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.setValue(2);

        property.asObservable().test()
                .perform(() -> property.updateValue(value -> value + 5))
                .assertValues(2, 7);
    }

    @Test
    @DisplayName("Observable should not emit value after same update")
    public void testObservableAfterSameUpdate()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.asObservable().test()
                .perform(() -> {
                    property.setValue(2);
                    property.updateValue(value -> 2);
                })
                .assertValue(2);
    }

    @Test
    @DisplayName("Observable should emit only last value after subscribed")
    public void testObservableAfterMultipleUpdates()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.setValue(2);
        property.updateValue(value -> value + 2);
        property.updateValue(value -> value + 2);

        property.asObservable().test()
                .assertValue(6);
    }

    @Test
    @DisplayName("Observable should not have observers after it is created")
    public void testHasObserversAfterCreate()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        assertFalse(property.hasObservers());
    }

    @Test
    @DisplayName("Observable should have observers after it is subscribed")
    public void testHasObserversAfterSubscribe()
    {
        final ReactiveProperty<Integer> property = ReactiveProperty.empty();

        property.asObservable().test();

        assertTrue(property.hasObservers());
    }
}
