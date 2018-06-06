package org.vaadin.addons.reactive.demo.screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addons.reactive.demo.DemoTheme;
import org.vaadin.addons.reactive.demo.component.CodePanel;

/**
 * @author dohnal
 */
public abstract class AbstractDemoSectionScreen extends AbstractScreen
{
    private final Demo demo;

    public AbstractDemoSectionScreen(final @Nonnull String demoFile)
    {
        this.demo = loadDemo(demoFile);
    }

    @Nullable
    protected abstract Component createSection(final @Nonnull String section);

    @Override
    protected void initScreen()
    {
        final String uri = Page.getCurrent().getUriFragment();
        final String section = uri.substring(uri.lastIndexOf("/") + 1);

        final Demo.Section demoSection = demo.getSection(section);

        if (demoSection != null)
        {
            final VerticalLayout layout = new VerticalLayout();
            layout.addStyleName("section");
            layout.setSizeFull();
            layout.setSpacing(false);
            layout.setMargin(false);

            final Label name = new Label(demoSection.name, ContentMode.HTML);
            name.addStyleName(DemoTheme.LABEL_H2);
            name.addStyleName("section-name");
            layout.addComponent(name);

            final Label description = new Label(demoSection.description, ContentMode.HTML);
            description.addStyleName("section-description");
            layout.addComponent(description);

            final Component component = createSection(section);

            if (component != null)
            {
                final Panel panel = new Panel(component);
                panel.addStyleName(DemoTheme.PANEL_BORDERLESS);
                panel.addStyleName("section-demo");
                panel.setSizeFull();

                layout.addComponent(panel);
                layout.setExpandRatio(panel, 1.0f);
            }

            final HorizontalSplitPanel splitPanel = new HorizontalSplitPanel(
                    new CodePanel("View", demoSection.viewCode),
                    new CodePanel("ViewModel", demoSection.viewModelCode));
            splitPanel.addStyleName("section-code");
            splitPanel.setSplitPosition(50, Unit.PERCENTAGE);
            splitPanel.setHeight(400, Unit.PIXELS);
            splitPanel.setWidth(100, Unit.PERCENTAGE);
            layout.addComponent(splitPanel);

            addComponent(layout);
        }

        setSizeFull();
        setMargin(false);
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
    private Demo loadDemo(final @Nonnull String demoFile)
    {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try
        {
            return mapper.readValue(getClass().getClassLoader().getResource(demoFile), Demo.class);
        }
        catch (final IOException e)
        {
            throw new RuntimeException("Cannot load demo " + demoFile, e);
        }
    }

    private static class Demo
    {
        @JsonProperty
        private Map<String, Section> sections;

        @Nullable
        public Section getSection(final @Nonnull String sectionName)
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
            private String viewCode;

            @JsonProperty
            private String viewModelCode;
        }
    }
}
