package org.vaadin.addons.reactive.demo;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addons.reactive.demo.screen.ReactiveBinderScreen;
import org.vaadin.addons.reactive.demo.screen.ReactiveCommandScreen;
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
    private CssLayout contentArea;

    private AtomicReference<Button> selectedMenuItem;

    @Override
    protected void init(final @Nonnull VaadinRequest request)
    {
        Responsive.makeResponsive(this);
        addStyleName(DemoTheme.UI_WITH_MENU);

        selectedMenuItem = new AtomicReference<>();

        final HorizontalLayout root = new HorizontalLayout();
        root.setSizeFull();
        root.setSpacing(false);

        final CssLayout menuLayout = new CssLayout();
        menuLayout.setPrimaryStyleName(DemoTheme.MENU_ROOT);
        menuLayout.addComponent(buildMenu());

        root.addComponent(menuLayout);

        contentArea = new CssLayout();
        contentArea.setSizeFull();

        root.addComponent(contentArea);
        root.setExpandRatio(contentArea, 1.0f);

        setContent(root);

        if (StringUtils.isEmpty(Page.getCurrent().getUriFragment()))
        {
            getNavigator().navigateTo(
                    ReactiveBinderScreen.SCREEN_NAME + "/" + ReactiveBinderScreen.SECTION_1);
        }
    }

    @Override
    public void showView(final @Nonnull View view)
    {
        contentArea.removeAllComponents();
        contentArea.addComponent((Component) view);
    }

    @Nonnull
    public CssLayout buildMenu()
    {
        final CssLayout menu = new CssLayout();

        menu.addStyleName(DemoTheme.MENU_PART);

        final HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(false);
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName(DemoTheme.MENU_TITLE);

        final Label title = new Label("<h3><strong>Reactive</strong> Vaadin Demo</h3>", ContentMode.HTML);
        title.setSizeUndefined();
        top.addComponent(title);
        top.setExpandRatio(title, 1);

        menu.addComponent(top);

        final CssLayout menuItemsLayout = new CssLayout();
        menu.addComponent(menuItemsLayout);

        menuItemsLayout.addComponent(createMenuSubTitle("Reactive binder"));
        menuItemsLayout.addComponent(createMenuItem("Binding to observables",
                ReactiveBinderScreen.SCREEN_NAME + "/" + ReactiveBinderScreen.SECTION_1));
        menuItemsLayout.addComponent(createMenuItem("Binding to async observables",
                ReactiveBinderScreen.SCREEN_NAME + "/" + ReactiveBinderScreen.SECTION_2));
        menuItemsLayout.addComponent(createMenuItem("Binding custom properties",
                ReactiveBinderScreen.SCREEN_NAME + "/" + ReactiveBinderScreen.SECTION_3));
        menuItemsLayout.addComponent(createMenuItem("One-way binding",
                ReactiveBinderScreen.SCREEN_NAME + "/" + ReactiveBinderScreen.SECTION_4));
        menuItemsLayout.addComponent(createMenuItem("Two-way binding",
                ReactiveBinderScreen.SCREEN_NAME + "/" + ReactiveBinderScreen.SECTION_5));
        menuItemsLayout.addComponent(createMenuItem("Handling errors",
                ReactiveBinderScreen.SCREEN_NAME + "/" + ReactiveBinderScreen.SECTION_6));
        menuItemsLayout.addComponent(createMenuItem("Handling subscriptions",
                ReactiveBinderScreen.SCREEN_NAME + "/" + ReactiveBinderScreen.SECTION_7));

        menuItemsLayout.addComponent(createMenuSubTitle("Reactive property"));
        menuItemsLayout.addComponent(createMenuItem("Capturing field values",
                ReactivePropertyScreen.SCREEN_NAME + "/" + ReactivePropertyScreen.SECTION_1));
        menuItemsLayout.addComponent(createMenuItem("Default value",
                ReactivePropertyScreen.SCREEN_NAME + "/" + ReactivePropertyScreen.SECTION_2));
        menuItemsLayout.addComponent(createMenuItem("Updating the value",
                ReactivePropertyScreen.SCREEN_NAME + "/" + ReactivePropertyScreen.SECTION_3));
        menuItemsLayout.addComponent(createMenuItem("Merging properties",
                ReactivePropertyScreen.SCREEN_NAME + "/" + ReactivePropertyScreen.SECTION_4));
        menuItemsLayout.addComponent(createMenuItem("Combining properties",
                ReactivePropertyScreen.SCREEN_NAME + "/" + ReactivePropertyScreen.SECTION_5));
        menuItemsLayout.addComponent(createMenuItem("Suppressing notifications",
                ReactivePropertyScreen.SCREEN_NAME + "/" + ReactivePropertyScreen.SECTION_6));
        menuItemsLayout.addComponent(createMenuItem("Delaying notifications",
                ReactivePropertyScreen.SCREEN_NAME + "/" + ReactivePropertyScreen.SECTION_7));

        menuItemsLayout.addComponent(createMenuSubTitle("Reactive command"));
        menuItemsLayout.addComponent(createMenuItem("Executing command",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_1));
        menuItemsLayout.addComponent(createMenuItem("Binding result",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_2));
        menuItemsLayout.addComponent(createMenuItem("Handling success",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_3));
        menuItemsLayout.addComponent(createMenuItem("Handling errors",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_4));
        menuItemsLayout.addComponent(createMenuItem("Controlling executability",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_5));
        menuItemsLayout.addComponent(createMenuItem("Execution count",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_6));
        menuItemsLayout.addComponent(createMenuItem("Input parameters",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_7));
        menuItemsLayout.addComponent(createMenuItem("Long-running command",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_8));
        menuItemsLayout.addComponent(createMenuItem("Asynchronous command",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_9));
        menuItemsLayout.addComponent(createMenuItem("Execution state",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_10));
        menuItemsLayout.addComponent(createMenuItem("Progress",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_11));
        menuItemsLayout.addComponent(createMenuItem("Automatic execution",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_12));
        menuItemsLayout.addComponent(createMenuItem("Composite commands",
                ReactiveCommandScreen.SCREEN_NAME + "/" + ReactiveCommandScreen.SECTION_13));

        return menu;
    }

    @Nonnull
    private Label createMenuSubTitle(final @Nonnull String name)
    {
        final Label label = new Label(name, ContentMode.HTML);
        label.setPrimaryStyleName(DemoTheme.MENU_SUBTITLE);
        label.addStyleName(DemoTheme.LABEL_H4);
        label.setSizeUndefined();

        return label;
    }

    @Nonnull
    private Button createMenuItem(final @Nonnull String name, final @Nonnull String navigationState)
    {
        final Button button = new Button(name);

        button.addClickListener(event -> {
            getUI().getNavigator().navigateTo(navigationState);

            Optional.ofNullable(selectedMenuItem.get()).ifPresent(b -> b.removeStyleName("selected"));

            button.addStyleName("selected");

            selectedMenuItem.set(button);
        });

        button.setCaptionAsHtml(true);
        button.setPrimaryStyleName(DemoTheme.MENU_ITEM);
        button.setIcon(VaadinIcons.ANGLE_RIGHT);

        return button;
    }
}
