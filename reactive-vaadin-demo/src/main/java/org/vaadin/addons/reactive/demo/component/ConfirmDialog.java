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

package org.vaadin.addons.reactive.demo.component;

import javax.annotation.Nonnull;

import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author dohnal
 */
public class ConfirmDialog extends Window
{
    private final Button ok;

    private final Button cancel;

    private Registration closeListenerRegistration;

    protected ConfirmDialog(final @Nonnull String caption,
                            final @Nonnull String message,
                            final @Nonnull String okCaption,
                            final @Nonnull String cancelCaption)
    {
        setCaption(caption);

        final VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setSpacing(true);
        content.setMargin(true);

        final VerticalLayout panelContent = new VerticalLayout();
        panelContent.setMargin(false);

        final Panel panel = new Panel(panelContent);
        panel.setWidth("100%");
        panel.setHeight("100%");
        panel.setStyleName(ValoTheme.PANEL_BORDERLESS);

        final Label messageLabel = new Label(message, ContentMode.HTML);
        messageLabel.setWidth("100%");
        panelContent.addComponent(messageLabel);

        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        ok = new Button(okCaption);
        ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(event -> {
            if (closeListenerRegistration != null)
            {
                closeListenerRegistration.remove();
            }

            close();
        });

        cancel = new Button(cancelCaption);
        cancel.addClickListener(event -> {
            if (closeListenerRegistration != null)
            {
                closeListenerRegistration.remove();
            }

            close();
        });

        buttons.addComponents(ok, cancel);

        content.addComponent(panel);
        content.setExpandRatio(panel, 1f);

        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.TOP_RIGHT);

        setContent(content);

        setWidth(400, Unit.PIXELS);
        setHeight(200, Unit.PIXELS);
        setModal(true);
        center();
    }

    @Nonnull
    public static ConfirmDialog show(final @Nonnull String caption,
                                     final @Nonnull String message,
                                     final @Nonnull String okCaption,
                                     final @Nonnull String cancelCaption)
    {
        final ConfirmDialog confirmDialog = new ConfirmDialog(caption, message, okCaption, cancelCaption);

        UI.getCurrent().addWindow(confirmDialog);

        return confirmDialog;
    }

    @Nonnull
    public ConfirmDialog withOkListener(final @Nonnull Button.ClickListener listener)
    {
        ok.addClickListener(listener);

        return this;
    }

    @Nonnull
    public ConfirmDialog withCancelListener(final @Nonnull Button.ClickListener listener)
    {
        cancel.addClickListener(listener);

        return this;
    }

    @Nonnull
    public ConfirmDialog withCloseListener(final @Nonnull Window.CloseListener listener)
    {
        closeListenerRegistration = addCloseListener(listener);

        return this;
    }
}
