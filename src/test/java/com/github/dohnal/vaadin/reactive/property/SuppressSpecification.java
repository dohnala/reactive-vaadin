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
 * Specification for {@link ReactiveProperty#suppress()}
 *
 * @author dohnal
 */
public interface SuppressSpecification
{
    /**
     * Specification that tests behavior of property when its change notifications are suppressed
     */
    abstract class AbstractSuppressSpecification implements RequireProperty<Integer>
    {
        private final Integer INITIAL_VALUE = 1;

        @BeforeEach
        void setInitialValue()
        {
            getProperty().setValue(INITIAL_VALUE);
        }

        @Nested
        @DisplayName("When property change notifications are suppressed")
        class WhenSuppress
        {
            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                getProperty().suppress();

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                getProperty().suppress();

                assertEquals(INITIAL_VALUE, getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(INITIAL_VALUE);

                getProperty().suppress();

                testObserver.assertValues(INITIAL_VALUE);
            }

            @Nested
            @DisplayName("After property change notifications are suppressed")
            class AfterSuppress
            {
                private Disposable disposable;

                @BeforeEach
                void suppress()
                {
                    disposable = getProperty().suppress();
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
                @DisplayName("When suppression is disposed")
                class WhenDispose
                {
                    @Test
                    @DisplayName("HasValue should be true")
                    public void testHasValue()
                    {
                        disposable.dispose();

                        assertTrue(getProperty().hasValue());
                    }

                    @Test
                    @DisplayName("Value should be correct")
                    public void testValue()
                    {
                        disposable.dispose();

                        assertEquals(INITIAL_VALUE, getProperty().getValue());
                    }

                    @Test
                    @DisplayName("Observable should not emit any value")
                    public void testObservable()
                    {
                        final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                        testObserver.assertValue(INITIAL_VALUE);

                        disposable.dispose();

                        testObserver.assertValues(INITIAL_VALUE);
                    }

                    @Nested
                    @DisplayName("After suppression is disposed")
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
                                return AbstractSuppressSpecification.this.getProperty();
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
                                return AbstractSuppressSpecification.this.getProperty();
                            }
                        }
                    }
                }
            }
        }
    }
}
