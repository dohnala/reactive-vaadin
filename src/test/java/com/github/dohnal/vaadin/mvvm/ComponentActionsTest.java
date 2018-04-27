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

import java.util.function.Consumer;
import java.util.function.Function;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link ComponentActions}
 *
 * @author dohnal
 */
@DisplayName("Component actions specification")
public class ComponentActionsTest implements ComponentActions
{
    private Page page;

    @BeforeEach
    protected void setUp()
    {
        page = Mockito.mock(Page.class);

        final UI ui = Mockito.mock(UI.class);

        Mockito.when(ui.getPage()).thenReturn(page);

        UI.setCurrent(ui);
    }

    @Nested
    @DisplayName("When show action is created with notification")
    class WhenCreateShowWithNotification
    {
        private Notification notification;
        private Runnable action;

        @BeforeEach
        protected void create()
        {
            notification = Mockito.mock(Notification.class);
            action = show(notification);
        }

        @Test
        @DisplayName("Notification should not be shown")
        public void testExecution()
        {
            Mockito.verifyZeroInteractions(notification);
        }

        @Nested
        @DisplayName("When action is called")
        class WhenCall
        {
            @BeforeEach
            protected void call()
            {
                action.run();
            }

            @Test
            @DisplayName("Notification should be shown")
            public void testExecution()
            {
                Mockito.verify(notification).show(page);
            }
        }
    }

    @Nested
    @DisplayName("When show action is created with notification function")
    class WhenCreateShowWithNotificationFunction
    {
        protected final String INPUT = "message";

        private Notification notification;
        private Function<String, Notification> notificationFunction;
        private Consumer<String> action;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            notification = Mockito.mock(Notification.class);
            notificationFunction = Mockito.mock(Function.class);

            Mockito.when(notificationFunction.apply(INPUT)).thenReturn(notification);

            action = show(notificationFunction);
        }

        @Test
        @DisplayName("Notification function should not be called")
        public void testExecution()
        {
            Mockito.verifyZeroInteractions(notificationFunction);
        }

        @Nested
        @DisplayName("When action is called")
        class WhenCall
        {
            @BeforeEach
            protected void call()
            {
                action.accept(INPUT);
            }

            @Test
            @DisplayName("Notification function should be called")
            public void testExecution()
            {
                Mockito.verify(notificationFunction).apply(INPUT);
            }

            @Test
            @DisplayName("Notification show be shown")
            public void testNotification()
            {
                Mockito.verify(notification).show(page);
            }
        }
    }
}
