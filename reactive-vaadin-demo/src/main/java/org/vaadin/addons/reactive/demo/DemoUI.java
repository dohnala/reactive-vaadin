package org.vaadin.addons.reactive.demo;

import javax.annotation.Nonnull;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * @author dohnal
 */
@Theme("demo")
public class DemoUI extends UI
{
    @Override
    protected void init(final @Nonnull VaadinRequest vaadinRequest)
    {
        setContent(new Label("Reactive Vaadin"));
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DemoUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet
    {}
}
