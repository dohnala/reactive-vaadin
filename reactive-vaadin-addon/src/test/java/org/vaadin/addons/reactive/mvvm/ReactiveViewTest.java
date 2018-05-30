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

package org.vaadin.addons.reactive.mvvm;

import javax.annotation.Nonnull;

import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ConnectorTracker;
import com.vaadin.ui.Label;
import com.vaadin.ui.PushConfiguration;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vaadin.addons.reactive.ReactiveCommand;
import org.vaadin.addons.reactive.ReactiveProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link ReactiveView}
 *
 * @author dohnal
 */
@DisplayName("Reactive view specification")
public class ReactiveViewTest
{
    private static class TestViewModel extends ReactiveViewModel
    {
        private final ReactiveProperty<String> property;

        private final ReactiveCommand<Void, String> command;

        public TestViewModel()
        {
            this.property = createProperty();
            this.command = createCommand(() -> property.getValue() != null ?
                    property.getValue().toLowerCase() : "");
        }
    }

    private static class TestView extends ReactiveView<TestViewModel>
    {
        private final TextField field;
        private final Label label;
        private final Button button;

        public TestView()
        {
            field = new TextField();
            label = new Label();
            button = new Button();
        }

        @Override
        protected void initView(final @Nonnull TestViewModel viewModel)
        {
            bind(valueOf(field)).to(viewModel.property);
            bind(valueOf(label)).to(viewModel.command.getResult());
            when(clickedOn(button)).then(execute(viewModel.command));

            setCompositionRoot(new VerticalLayout(field, label, button));
        }
    }

    @Nested
    @DisplayName("When new reactive view model is created")
    class WhenCreate
    {
        private TestView view;
        private TestViewModel viewModel;

        @BeforeEach
        void before()
        {
            final VaadinSession session = Mockito.mock(VaadinSession.class);
            final UI ui = Mockito.mock(UI.class);
            final PushConfiguration pushConfiguration = Mockito.mock(PushConfiguration.class);

            Mockito.when(session.hasLock()).thenReturn(true);
            Mockito.when(ui.getSession()).thenReturn(session);
            Mockito.when(ui.getConnectorTracker()).thenReturn(Mockito.mock(ConnectorTracker.class));
            Mockito.when(ui.isAttached()).thenReturn(true);
            Mockito.when(ui.getPushConfiguration()).thenReturn(pushConfiguration);
            Mockito.when(pushConfiguration.getPushMode()).thenReturn(PushMode.DISABLED);
            Mockito.doAnswer(invocation -> {
                final Runnable runnable = invocation.getArgument(0);

                runnable.run();

                return null;
            }).when(ui).access(Mockito.any(Runnable.class));

            VaadinSession.setCurrent(session);
            UI.setCurrent(ui);

            viewModel = new TestViewModel();
            view = new TestView();
        }

        @Test
        @DisplayName("Field should be empty")
        public void testField()
        {
            assertEquals("", view.field.getValue());
        }

        @Test
        @DisplayName("Label should be empty")
        public void testLabel()
        {
            assertEquals("", view.label.getValue());
        }

        @Nested
        @DisplayName("When field value is changed")
        class WhenChangeFieldValue
        {
            @BeforeEach
            void before()
            {
                view.field.setValue("Value");
            }

            @Test
            @DisplayName("Field should be changed")
            public void testField()
            {
                assertEquals("Value", view.field.getValue());
            }

            @Test
            @DisplayName("Label should be empty")
            public void testLabel()
            {
                assertEquals("", view.label.getValue());
            }

            @Nested
            @DisplayName("When button is clicked")
            class WhenClickButton
            {
                @BeforeEach
                void before()
                {
                    view.button.click();
                }

                @Test
                @DisplayName("Field should be same")
                public void testField()
                {
                    assertEquals("Value", view.field.getValue());
                }

                @Test
                @DisplayName("Label should be empty")
                public void testLabel()
                {
                    assertEquals("", view.label.getValue());
                }
            }
        }

        @Nested
        @DisplayName("When view is attached")
        class WhenAttach
        {
            @BeforeEach
            void before()
            {
                view.setParent(UI.getCurrent());
                view.attach();
            }

            @Test
            @DisplayName("Field should be empty")
            public void testField()
            {
                assertEquals("", view.field.getValue());
            }

            @Test
            @DisplayName("Label should be empty")
            public void testLabel()
            {
                assertEquals("", view.label.getValue());
            }

            @Nested
            @DisplayName("When field value is changed")
            class WhenChangeFieldValue
            {
                @BeforeEach
                void before()
                {
                    view.field.setValue("Value");
                }

                @Test
                @DisplayName("Field should be changed")
                public void testField()
                {
                    assertEquals("Value", view.field.getValue());
                }

                @Test
                @DisplayName("Label should be empty")
                public void testLabel()
                {
                    assertEquals("", view.label.getValue());
                }

                @Nested
                @DisplayName("When button is clicked")
                class WhenClickButton
                {
                    @BeforeEach
                    void before()
                    {
                        view.button.click();
                    }

                    @Test
                    @DisplayName("Field should be same")
                    public void testField()
                    {
                        assertEquals("Value", view.field.getValue());
                    }

                    @Test
                    @DisplayName("Label should be empty")
                    public void testLabel()
                    {
                        assertEquals("", view.label.getValue());
                    }
                }
            }

            @Nested
            @DisplayName("When view model is set to view")
            class WhenSetViewModel
            {
                @BeforeEach
                void before()
                {
                    view.withViewModel(viewModel);
                }

                @Test
                @DisplayName("Field should be empty")
                public void testField()
                {
                    assertEquals("", view.field.getValue());
                }

                @Test
                @DisplayName("Label should be empty")
                public void testLabel()
                {
                    assertEquals("", view.label.getValue());
                }

                @Nested
                @DisplayName("When field value is changed")
                class WhenChangeFieldValue
                {
                    @BeforeEach
                    void before()
                    {
                        view.field.setValue("Value");
                    }

                    @Test
                    @DisplayName("Field should be changed")
                    public void testField()
                    {
                        assertEquals("Value", view.field.getValue());
                    }

                    @Test
                    @DisplayName("Label should be empty")
                    public void testLabel()
                    {
                        assertEquals("", view.label.getValue());
                    }

                    @Nested
                    @DisplayName("When button is clicked")
                    class WhenClickButton
                    {
                        @BeforeEach
                        void before()
                        {
                            view.button.click();
                        }

                        @Test
                        @DisplayName("Field should be correct")
                        public void testField()
                        {
                            assertEquals("Value", view.field.getValue());
                        }

                        @Test
                        @DisplayName("Label should be correct")
                        public void testLabel()
                        {
                            assertEquals("value", view.label.getValue());
                        }
                    }
                }
            }

            @Nested
            @DisplayName("When view is detached")
            class WhenDetach
            {
                @BeforeEach
                void before()
                {
                    view.detach();
                }

                @Test
                @DisplayName("Field should be empty")
                public void testField()
                {
                    assertEquals("", view.field.getValue());
                }

                @Test
                @DisplayName("Label should be empty")
                public void testLabel()
                {
                    assertEquals("", view.label.getValue());
                }

                @Nested
                @DisplayName("When field value is changed")
                class WhenChangeFieldValue
                {
                    @BeforeEach
                    void before()
                    {
                        view.field.setValue("Value");
                    }

                    @Test
                    @DisplayName("Field should be changed")
                    public void testField()
                    {
                        assertEquals("Value", view.field.getValue());
                    }

                    @Test
                    @DisplayName("Label should be empty")
                    public void testLabel()
                    {
                        assertEquals("", view.label.getValue());
                    }

                    @Nested
                    @DisplayName("When button is clicked")
                    class WhenClickButton
                    {
                        @BeforeEach
                        void before()
                        {
                            view.button.click();
                        }

                        @Test
                        @DisplayName("Field should be same")
                        public void testField()
                        {
                            assertEquals("Value", view.field.getValue());
                        }

                        @Test
                        @DisplayName("Label should be empty")
                        public void testLabel()
                        {
                            assertEquals("", view.label.getValue());
                        }
                    }
                }
            }
        }

        @Nested
        @DisplayName("When view model is set to view")
        class WhenSetViewModel
        {
            @BeforeEach
            void before()
            {
                view.withViewModel(viewModel);
            }

            @Test
            @DisplayName("Field should be empty")
            public void testField()
            {
                assertEquals("", view.field.getValue());
            }

            @Test
            @DisplayName("Label should be empty")
            public void testLabel()
            {
                assertEquals("", view.label.getValue());
            }

            @Nested
            @DisplayName("When field value is changed")
            class WhenChangeFieldValue
            {
                @BeforeEach
                void before()
                {
                    view.field.setValue("Value");
                }

                @Test
                @DisplayName("Field should be changed")
                public void testField()
                {
                    assertEquals("Value", view.field.getValue());
                }

                @Test
                @DisplayName("Label should be empty")
                public void testLabel()
                {
                    assertEquals("", view.label.getValue());
                }

                @Nested
                @DisplayName("When button is clicked")
                class WhenClickButton
                {
                    @BeforeEach
                    void before()
                    {
                        view.button.click();
                    }

                    @Test
                    @DisplayName("Field should be same")
                    public void testField()
                    {
                        assertEquals("Value", view.field.getValue());
                    }

                    @Test
                    @DisplayName("Label should be empty")
                    public void testLabel()
                    {
                        assertEquals("", view.label.getValue());
                    }
                }
            }

            @Nested
            @DisplayName("When view is attached")
            class WhenAttach
            {
                @BeforeEach
                void before()
                {
                    view.setParent(UI.getCurrent());
                    view.attach();
                }

                @Test
                @DisplayName("Field should be empty")
                public void testField()
                {
                    assertEquals("", view.field.getValue());
                }

                @Test
                @DisplayName("Label should be empty")
                public void testLabel()
                {
                    assertEquals("", view.label.getValue());
                }

                @Nested
                @DisplayName("When field value is changed")
                class WhenChangeFieldValue
                {
                    @BeforeEach
                    void before()
                    {
                        view.field.setValue("Value");
                    }

                    @Test
                    @DisplayName("Field should be changed")
                    public void testField()
                    {
                        assertEquals("Value", view.field.getValue());
                    }

                    @Test
                    @DisplayName("Label should be empty")
                    public void testLabel()
                    {
                        assertEquals("", view.label.getValue());
                    }

                    @Nested
                    @DisplayName("When button is clicked")
                    class WhenClickButton
                    {
                        @BeforeEach
                        void before()
                        {
                            view.button.click();
                        }

                        @Test
                        @DisplayName("Field should be same")
                        public void testField()
                        {
                            assertEquals("Value", view.field.getValue());
                        }

                        @Test
                        @DisplayName("Label should be correct")
                        public void testLabel()
                        {
                            assertEquals("value", view.label.getValue());
                        }
                    }
                }

                @Nested
                @DisplayName("When view is detached")
                class WhenDetach
                {
                    @BeforeEach
                    void before()
                    {
                        view.detach();
                    }

                    @Test
                    @DisplayName("Field should be empty")
                    public void testField()
                    {
                        assertEquals("", view.field.getValue());
                    }

                    @Test
                    @DisplayName("Label should be empty")
                    public void testLabel()
                    {
                        assertEquals("", view.label.getValue());
                    }

                    @Nested
                    @DisplayName("When field value is changed")
                    class WhenChangeFieldValue
                    {
                        @BeforeEach
                        void before()
                        {
                            view.field.setValue("Value");
                        }

                        @Test
                        @DisplayName("Field should be changed")
                        public void testField()
                        {
                            assertEquals("Value", view.field.getValue());
                        }

                        @Test
                        @DisplayName("Label should be empty")
                        public void testLabel()
                        {
                            assertEquals("", view.label.getValue());
                        }

                        @Nested
                        @DisplayName("When button is clicked")
                        class WhenClickButton
                        {
                            @BeforeEach
                            void before()
                            {
                                view.button.click();
                            }

                            @Test
                            @DisplayName("Field should be same")
                            public void testField()
                            {
                                assertEquals("Value", view.field.getValue());
                            }

                            @Test
                            @DisplayName("Label should be empty")
                            public void testLabel()
                            {
                                assertEquals("", view.label.getValue());
                            }
                        }
                    }
                }
            }
        }
    }
}
