package org.vaadin.addons.reactive.demo.component;

import javax.annotation.Nonnull;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.text.StringEscapeUtils;
import org.vaadin.addons.reactive.demo.DemoTheme;

/**
 * @author dohnal
 */
@JavaScript("https://cdn.rawgit.com/google/code-prettify/master/loader/run_prettify.js?lang=java")
public class CodePanel extends Panel
{
    public CodePanel(final @Nonnull String caption, final @Nonnull String code)
    {
        setCaption(caption);

        setContent(new VerticalLayout(new Label("<pre class=\"prettyprint\">" +
                StringEscapeUtils.escapeHtml4(code) + "</pre>", ContentMode.HTML)));

        addStyleName(DemoTheme.COLOR_DARK);
        addStyleName(DemoTheme.PANEL_STRAIGHT_BORDER);
        setSizeFull();

        addAttachListener(event -> Page.getCurrent().getJavaScript().execute("PR.prettyPrint()"));
    }
}
