package org.vaadin.addons.reactive.demo;

import javax.annotation.Nonnull;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.reactive.demo.screen.ReactiveBinderScreen;
import org.vaadin.addons.reactive.demo.screen.ReactivePropertyScreen;

/**
 * @author dohnal
 */
@Push
@SpringUI
@Theme("demo")
@SpringViewDisplay
public class DemoUI extends UI implements ViewDisplay
{
    private Panel springViewDisplay;

    @Override
    protected void init(final @Nonnull VaadinRequest request)
    {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(DemoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponents(
                createNavigationButton("Reactive binder", ReactiveBinderScreen.SCREEN_NAME),
                createNavigationButton("Reactive property", ReactivePropertyScreen.SCREEN_NAME));

        root.addComponent(navigationBar);

        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();

        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1.0f);

        getUI().getNavigator().navigateTo(ReactiveBinderScreen.SCREEN_NAME);

        setContent(root);
    }

    @Override
    public void showView(final @Nonnull View view)
    {
        springViewDisplay.setContent((Component) view);
    }

    @Nonnull
    private Button createNavigationButton(final @Nonnull String caption,
                                          final @Nonnull String viewName)
    {
        final Button button = new Button(caption);

        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));

        return button;
    }
}
