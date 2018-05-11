/*
 * Copyright (c) 2018-present, reactive-mvvm Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package com.github.dohnal.vaadin.reactive.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Specification for {@link ReactiveProperty#delay()}
 *
 * @author dohnal
 */
public interface DelaySpecification extends BasePropertySpecification
{
    /**
     * Specification that tests behavior of property when its change notifications are delayed
     */
    abstract class AbstractDelaySpecification implements RequireProperty<Integer>
    {
        private final Integer INITIAL_VALUE = 1;

        @BeforeEach
        void setInitialValue()
        {
            getProperty().setValue(INITIAL_VALUE);
        }

        @Nested
        @DisplayName("When property change notifications are delayed")
        class WhenDelay
        {
            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                getProperty().delay();

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                getProperty().delay();

                assertEquals(INITIAL_VALUE, getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(INITIAL_VALUE);

                getProperty().delay();

                testObserver.assertValues(INITIAL_VALUE);
            }

            @Nested
            @DisplayName("After property change notifications are delayed")
            class AfterDelay
            {
                private Disposable disposable;

                @BeforeEach
                void delay()
                {
                    disposable = getProperty().delay();
                }

                @Nested
                @DisplayName("When value is set")
                class WhenSetValue
                {
                    private final Integer VALUE = 7;

                    @Test
                    @DisplayName("HasValue should be true")
                    public void testHasValue()
                    {
                        getProperty().setValue(VALUE);

                        assertTrue(getProperty().hasValue());
                    }

                    @Test
                    @DisplayName("Value should be correct")
                    public void testValue()
                    {
                        getProperty().setValue(VALUE);

                        assertEquals(VALUE, getProperty().getValue());
                    }

                    @Test
                    @DisplayName("Observable should not emit any value")
                    public void testObservable()
                    {
                        final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                        testObserver.assertValue(INITIAL_VALUE);

                        getProperty().setValue(VALUE);

                        testObserver.assertValues(INITIAL_VALUE);
                    }
                }

                @Nested
                @DisplayName("When value is updated")
                class WhenUpdateValue
                {
                    private final Integer VALUE = 7;

                    @Test
                    @DisplayName("HasValue should be true")
                    public void testHasValue()
                    {
                        getProperty().updateValue(value -> VALUE);

                        assertTrue(getProperty().hasValue());
                    }

                    @Test
                    @DisplayName("Value should be correct")
                    public void testValue()
                    {
                        getProperty().updateValue(value -> INITIAL_VALUE);

                        assertEquals(INITIAL_VALUE, getProperty().getValue());
                    }

                    @Test
                    @DisplayName("Observable should not emit any value")
                    public void testObservable()
                    {
                        final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                        testObserver.assertValue(INITIAL_VALUE);

                        getProperty().updateValue(value -> VALUE);

                        testObserver.assertValue(INITIAL_VALUE);
                    }
                }

                @Nested
                @DisplayName("When delay is disposed")
                class WhenDispose
                {
                    private final Integer VALUE = 3;
                    private final Integer LAST_VALUE = 5;

                    @Test
                    @DisplayName("HasValue should be true")
                    public void testHasValue()
                    {
                        getProperty().setValue(VALUE);
                        getProperty().setValue(LAST_VALUE);

                        disposable.dispose();

                        assertTrue(getProperty().hasValue());
                    }

                    @Test
                    @DisplayName("Value should be correct")
                    public void testValue()
                    {
                        getProperty().setValue(VALUE);
                        getProperty().setValue(LAST_VALUE);

                        disposable.dispose();

                        assertEquals(LAST_VALUE, getProperty().getValue());
                    }

                    @Test
                    @DisplayName("Observable should emit only last value")
                    public void testObservable()
                    {
                        final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                        testObserver.assertValue(INITIAL_VALUE);

                        getProperty().setValue(VALUE);
                        getProperty().setValue(LAST_VALUE);

                        disposable.dispose();

                        testObserver.assertValues(INITIAL_VALUE, LAST_VALUE);
                    }

                    @Nested
                    @DisplayName("After delay is disposed")
                    class AfterDispose implements SetValueSpecification, UpdateValueSpecification
                    {
                        @BeforeEach
                        void disposeSuppression()
                        {
                            disposable.dispose();
                        }

                        @Nested
                        @DisplayName("Set value specification")
                        class SetValue extends AbstractSetValueSpecification
                        {
                            @Nonnull
                            @Override
                            public ReactiveProperty<Integer> getProperty()
                            {
                                return AbstractDelaySpecification.this.getProperty();
                            }
                        }

                        @Nested
                        @DisplayName("Update value specification")
                        class UpdateValue extends AbstractUpdateValueSpecification
                        {
                            @Nonnull
                            @Override
                            public ReactiveProperty<Integer> getProperty()
                            {
                                return AbstractDelaySpecification.this.getProperty();
                            }
                        }
                    }
                }
            }
        }
    }
}
