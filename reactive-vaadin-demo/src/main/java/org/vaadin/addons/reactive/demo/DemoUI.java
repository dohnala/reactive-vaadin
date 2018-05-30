package org.vaadin.addons.reactive.demo;

import javax.annotation.Nonnull;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * @author dohnal
 */
@Push
@SpringUI
@Theme("demo")
public class DemoUI extends UI
{
    @Override
    protected void init(final @Nonnull VaadinRequest vaadinRequest)
    {
        setContent(new Label("Reactive Vaadin"));
    }
}
