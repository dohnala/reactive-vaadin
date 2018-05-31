package org.vaadin.addons.reactive.demo.component;

import javax.annotation.Nonnull;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addons.reactive.demo.DemoTheme;

/**
 * Displays information alert
 *
 * @author dohnal
 */
public class InfoAlert extends VerticalLayout
{
    public InfoAlert(final @Nonnull String content)
    {
        final Label contentLabel = new Label(content, ContentMode.HTML);
        contentLabel.setWidth(100, Unit.PERCENTAGE);

        addComponent(contentLabel);
        addStyleName(DemoTheme.INFO_ALERT);
    }
}
