package org.vaadin.addons.reactive.demo.screen;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addons.reactive.demo.DemoTheme;
import org.vaadin.addons.reactive.demo.component.CodeWindow;
import org.vaadin.addons.reactive.demo.component.InfoAlert;
import org.vaadin.addons.reactive.mvvm.ReactiveView;

/**
 * @author dohnal
 */
public abstract class AbstractDemoScreen extends VerticalLayout implements View
{
    private final Demo demo;

    public AbstractDemoScreen()
    {
        this.demo = null;
    }

    public AbstractDemoScreen(final @Nonnull String name)
    {
        this.demo = loadDemo(name);

        addComponent(header("<b>" + demo.name + "</b><br/>" + demo.description));
    }

    protected void addSection(final @Nonnull String sectionCode,
                              final @Nonnull ReactiveView view)
    {
        final Demo.Section section = demo != null ? demo.getSection(sectionCode) : null;

        if (section != null)
        {
            addComponent(section(section, view));
        }
    }

    @Nonnull
    protected HorizontalLayout row(final @Nonnull Component... components)
    {
        final HorizontalLayout rowLayout = new HorizontalLayout(components);

        rowLayout.setHeight(100, Unit.PERCENTAGE);

        Arrays.asList(components).forEach(component ->
                rowLayout.setComponentAlignment(component, Alignment.MIDDLE_LEFT));

        return rowLayout;
    }

    @Nonnull
    private InfoAlert header(final @Nonnull String description)
    {
        return new InfoAlert(description);
    }

    @Nonnull
    private Panel section(final @Nonnull Demo.Section section, final @Nonnull ReactiveView view)
    {
        view.addStyleName(DemoTheme.LAYOUT_NO_PADDING);

        final CodeWindow codeWindow = new CodeWindow(section.name, section.view, section.viewModel);
        final Button showCodeButton = new Button("Show code", event -> UI.getCurrent().addWindow(codeWindow));
        showCodeButton.addStyleName("show-code");

        showCodeButton.addStyleName(DemoTheme.BUTTON_LINK);

        final Panel panel = new Panel(section.name,
                new VerticalLayout(new InfoAlert(section.description), view, showCodeButton));

        panel.addStyleName("section");

        return panel;
    }

    @Nonnull
    private Demo loadDemo(final @Nonnull String name)
    {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try
        {
            return mapper.readValue(getClass().getClassLoader().getResource(name), Demo.class);
        }
        catch (final IOException e)
        {
            throw new RuntimeException("Cannot load demo", e);
        }
    }

    private static class Demo
    {
        @JsonProperty
        private String name;

        @JsonProperty
        private String description;

        @JsonProperty
        private Map<String, Section> sections;

        public Demo.Section getSection(final @Nonnull String sectionName)
        {
            return sections.get(sectionName);
        }

        private static class Section
        {
            @JsonProperty
            private String name;

            @JsonProperty
            private String description;

            @JsonProperty
            private String view;

            @JsonProperty
            private String viewModel;
        }
    }
}
