package org.vaadin.addons.reactive.demo.screen;

import javax.annotation.Nonnull;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * @author dohnal
 */
public abstract class AbstractScreen extends VerticalLayout implements View
{
    protected abstract void initScreen();

    public final void enter(final @Nonnull ViewChangeListener.ViewChangeEvent event)
    {
        initScreen();
    }
}
