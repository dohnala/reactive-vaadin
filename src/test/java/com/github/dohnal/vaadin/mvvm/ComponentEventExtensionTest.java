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

package com.github.dohnal.vaadin.mvvm;

import com.vaadin.data.HasValue;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link ComponentEventExtension}
 *
 * @author dohnal
 */
@DisplayName("Component event extension specification")
public class ComponentEventExtensionTest implements ComponentEventExtension
{
    @Nested
    @DisplayName("When clickedOn event is created with button")
    class WhenCreateClickedOnButton
    {
        private Button button;
        private Observable<Button.ClickEvent> event;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            button = new Button();
            event = clickedOn(button);
        }

        @Test
        @DisplayName("Event should not emit any value")
        public void testEvent()
        {
            event.test()
                    .assertNoValues();
        }

        @Nested
        @DisplayName("When button is clicked")
        class WhenClick
        {
            @Test
            @DisplayName("Event should emit correct value")
            public void testEvent()
            {
                final TestObserver<Button.ClickEvent> testObserver = event.test();

                button.click();

                testObserver.assertValue(clickEvent -> clickEvent.getButton().equals(button));
            }
        }
    }

    @Nested
    @DisplayName("When valueChangedOf event is created with field")
    class WhenCreateValueChangedOfField
    {
        private TextField field;
        private Observable<HasValue.ValueChangeEvent<String>> event;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            field = new TextField();
            event = valueChangedOf(field);
        }

        @Test
        @DisplayName("Event should not emit any value")
        public void testEvent()
        {
            event.test()
                    .assertNoValues();
        }

        @Nested
        @DisplayName("When value of field is changed")
        class WhenChange
        {
            @Test
            @DisplayName("Event should emit correct value")
            public void testEvent()
            {
                final TestObserver<HasValue.ValueChangeEvent<String>> testObserver = event.test();

                field.setValue("value");

                testObserver.assertValue(valueChangeEvent ->
                        valueChangeEvent.getSource().equals(field) &&
                                valueChangeEvent.getOldValue().equals("") &&
                                valueChangeEvent.getValue().equals("value"));
            }
        }
    }
}
