package org.vaadin.addons.reactive.demo.component;

import javax.annotation.Nonnull;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.commons.text.StringEscapeUtils;
import org.vaadin.addons.reactive.demo.DemoTheme;

/**
 * Window which displays section code highlighted
 *
 * @author dohnal
 */
@JavaScript("https://cdn.rawgit.com/google/code-prettify/master/loader/run_prettify.js?lang=java")
public class CodeWindow extends Window
{
    public CodeWindow(final @Nonnull String sectionName,
                      final @Nonnull String viewCode,
                      final @Nonnull String viewModelCode)
    {
        super(sectionName);

        final TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.addStyleName(DemoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(DemoTheme.TABSHEET_PADDED_TABBAR);

        tabSheet.addTab(createCodePanel(viewCode), "View");
        tabSheet.addTab(createCodePanel(viewModelCode), "ViewModel");

        tabSheet.addSelectedTabChangeListener(event ->
                Page.getCurrent().getJavaScript().execute("PR.prettyPrint()"));

        addAttachListener(event ->
                Page.getCurrent().getJavaScript().execute("PR.prettyPrint()"));

        setContent(tabSheet);

        setHeight(600, Unit.PIXELS);
        setWidth(1000, Unit.PIXELS);

        center();
    }

    @Nonnull
    private Panel createCodePanel(final @Nonnull String code)
    {
        final VerticalLayout layout = new VerticalLayout(new Label("<pre class=\"prettyprint\">" +
                StringEscapeUtils.escapeHtml4(code) + "</pre>", ContentMode.HTML));

        final Panel panel = new Panel(layout);

        panel.setSizeFull();

        return panel;
    }
}
